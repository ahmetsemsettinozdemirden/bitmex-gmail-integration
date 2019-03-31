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
 * Bitmex credentials consist of api key and secret.
 */
@Entity
public class BitmexCredentials extends Model {

    @Id
    private Long id;

    @NotNull
    private String apiKey;

    @NotNull
    private String apiSecret;

    @CreatedTimestamp
    private LocalDateTime createdAt;

    @UpdatedTimestamp
    private LocalDateTime updatedAt;

    public static final Finder<Long, BitmexCredentials> finder = new Finder<>(BitmexCredentials.class);

    /**
     * Creates a Bitmex credentials.
     * @param apiKey Api key from Bitmex.
     * @param apiSecret Api secret from Bitmex.
     */
    public BitmexCredentials(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
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
