package business.settings;

import business.entry.EntryRepository;
import models.Entry;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SettingsService {

    private EntryRepository entryRepository;

    @Inject
    public SettingsService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Entry getSetting(String key) {
        return entryRepository.getEntry(key);
    }

    public List<Entry> getSettings() {
        List<Entry> settings = new ArrayList<>();
        settings.add(entryRepository.getEntry("bitmexUri"));
        settings.add(entryRepository.getEntry("fromMail"));
        settings.add(entryRepository.getEntry("timeInterval"));
        return settings;
    }

    public void updateSettings(String bitmexUri, String fromMail, String timeInterval) {
        entryRepository.setEntry("bitmexUri", bitmexUri);
        entryRepository.setEntry("fromMail", fromMail);
        entryRepository.setEntry("timeInterval", timeInterval);
    }

}
