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
 * Entry for settings.
 */
@Entity
public class Entry extends Model {

    @Id
    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    @CreatedTimestamp
    private LocalDateTime createdAt;

    @UpdatedTimestamp
    private LocalDateTime updatedAt;

    public static final Finder<Long, Entry> finder = new Finder<>(Entry.class);

    /**
     * Creates a Settings Entry.
     * @param key Key of Entry.
     * @param value Value of Entry.
     */
    public Entry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
