package db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.UpdatedTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Gmail credentials consist of email address and credentials from Google Developer Console.
 */
@Entity
public class GmailCredentials extends Model {

    @Id
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Length(800)
    private String credentials;

    @CreatedTimestamp
    private LocalDateTime createdAt;

    @UpdatedTimestamp
    private LocalDateTime updatedAt;

    public static final Finder<Long, GmailCredentials> finder = new Finder<>(GmailCredentials.class);

    /**
     * Creates a Gmail credentials.
     * @param email Gmail address.
     * @param credentials Credentials of the account from Google Developer Console.
     */
    public GmailCredentials(String email, String credentials) {
        this.email = email;
        this.credentials = credentials;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
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
