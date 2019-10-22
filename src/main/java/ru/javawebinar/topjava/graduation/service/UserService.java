package ru.javawebinar.topjava.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.graduation.AuthorizedUser;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.repository.JpaUserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class UserService implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JpaUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(JpaUserRepository repository, PasswordEncoder passwordEncoder) {
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
        logger.info("create user {}", user);
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        logger.info("delete user {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User get(int id) {
        logger.info("get user {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    User getByEmail(@NotBlank String email) {
        logger.info("get user by email {}", email);
        return checkNotFound(repository.getByEmail(email), "email = " + email);
    }

    @Cacheable("users")
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> getAll() {
        logger.info("get all users");
        return repository.findAll(SORT_NAME_EMAIL);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(@NotNull User user) {
        logger.info("update user {}", user);
        User updated = get(user.getId());
        user.setEnabled(updated.isEnabled());
        user.setRegistered(updated.getRegistered());
        prepareToSave(user, passwordEncoder);
        // to allow update user without submitting password
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(updated.getPassword());
        }
        repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void enable(int id, boolean enabled) {
        logger.info("set user {} enabled = {}", id, enabled);
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(prepareToSave(user, passwordEncoder));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("get authorized user by email {}", email);
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}