package db.repository;

import db.models.Entry;

/**
 * Entry fetch and set for settings.
 */
public class EntryRepository {

    /**
     * Fetches an entry.
     * @param key Entry key.
     * @return Entry.
     */
    public Entry getEntry(String key) {
        return Entry.finder.query()
                .where()
                .eq("key", key)
                .findOne();
    }

    /**
     * Updates an entry.
     * @param key Entry key.
     * @param value New entry value.
     * @return Updated Entry.
     */
    public Entry setEntry(String key, String value) {
        Entry entry = getEntry(key);
        if (entry == null) {
            entry = new Entry(key, value);
        } else {
            entry.setValue(value);
        }
        entry.save();
        return entry;
    }

}
