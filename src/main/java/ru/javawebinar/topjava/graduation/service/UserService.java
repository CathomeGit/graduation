package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.graduation.AuthorizedUser;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.repository.CrudUserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");
    private final CrudUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(CrudUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(@NotNull User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    User getByEmail(@NotBlank String email) {
        return checkNotFound(repository.getByEmail(email), "email = " + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(@NotNull User user) {
        User updated = get(user.getId());
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(updated.getPassword());
        }
        user.setEnabled(updated.isEnabled());
        user.setRegistered(updated.getRegistered());
        repository.save(prepareToSave(user, passwordEncoder));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(prepareToSave(user, passwordEncoder));
    }

    public User getWithVotes(int id) {
        return checkNotFoundWithId(repository.getWithVotes(id), id);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}