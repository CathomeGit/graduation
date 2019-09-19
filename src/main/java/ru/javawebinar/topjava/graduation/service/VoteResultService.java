package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.VoteResult;
import ru.javawebinar.topjava.graduation.repository.JpaVoteRepository;
import ru.javawebinar.topjava.graduation.repository.JpaVoteResultRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Service
public class VoteResultService {
    private static final Sort SORT_COUNT = new Sort(Sort.Direction.DESC, "count");

    private final JpaVoteRepository voteRepository;
    private final JpaVoteResultRepository resultRepository;

    @Autowired
    public VoteResultService(JpaVoteRepository voteRepository,
                             JpaVoteResultRepository resultRepository) {
        this.voteRepository = voteRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional
    @Cacheable(value = "results")
    public List<VoteResult> createAndReturn(@NotNull LocalDate date) {
        List<VoteResult> results = resultRepository.findAllByDate(date, SORT_COUNT);
        if (results.size() == 0) {
            results = voteRepository.retrieveVoteResult(date);
            // https://stackoverflow.com/a/39192050/4925022 -->
            for (VoteResult result : results) {
                if (result.getDate() == null) {
                    result.setDate(date);
                }
            }
            // <--
            resultRepository.saveAll(results);
        }
        return results;
    }
}