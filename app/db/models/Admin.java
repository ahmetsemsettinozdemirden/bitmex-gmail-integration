package db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.UpdatedTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Admin account entity.
 */
@Entity
public class Admin extends Model {

    @Id
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String token;

    @CreatedTimestamp
    private LocalDateTime createdAt;

    @UpdatedTimestamp
    private LocalDateTime updatedAt;

    public static final Finder<Long, Admin> finder = new Finder<>(Admin.class);

    /**
     * Creates an admin account.
     * @param username Admin's username.
     * @param password Admin's password.
     * @param token Admin's Bearer token.
     */
    public Admin(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonIgnore
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonIgnore
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}