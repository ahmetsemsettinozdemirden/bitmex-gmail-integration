package business.entry;

import models.Entry;

public class EntryRepository {

    public Entry getEntry(String key) {
        return Entry.finder.query()
                .where()
                .eq("key", key)
                .findOne();
    }

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
