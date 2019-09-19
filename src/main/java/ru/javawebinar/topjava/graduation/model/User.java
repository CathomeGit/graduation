package ru.javawebinar.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.graduation.model.converters.RolesConverter;
import ru.javawebinar.topjava.graduation.web.Post;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 100, groups = Persist.class)
    @Size(min = 5, max = 20, groups = Post.class)
    @NotBlank(groups = Persist.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private @NotNull LocalDateTime registered = LocalDateTime.now();

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Column(name = "roles", nullable = false, columnDefinition = "integer default 1")
    @NotNull
    // https://thoughts-on-java.org/jpa-21-how-to-implement-type-converter/
    @Convert(converter = RolesConverter.class)
    // EnumSet must be declared explicitly because interface reference yields HashSet during JSON conversion
    private EnumSet<Role> roles;

    @OneToMany(targetEntity = Vote.class, fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("date DESC")
    @JsonManagedReference("user_votes")
    private List<Vote> votes;

    public User() {
    }

    public User(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword(),
                user.isEnabled(), user.getRegistered(), user.getRoles());
    }

    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, true, LocalDateTime.now(), EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password,
                boolean enabled, @NotNull LocalDateTime registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(@NotNull LocalDateTime registered) {
        this.registered = registered;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", registered=" + registered +
                ", roles=" + roles +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}