package business.settings;

import db.repository.EntryRepository;
import db.models.Entry;

import javax.inject.Inject;

public class SettingsService {

    private EntryRepository entryRepository;

    @Inject
    public SettingsService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Entry getSetting(String key) {
        return entryRepository.getEntry(key);
    }

    public Settings getSettings() {
        return new Settings(entryRepository.getEntry("bitmexUri").getValue(),
                            entryRepository.getEntry("fromMail").getValue(),
                            entryRepository.getEntry("timeInterval").getValue());
    }

    public void updateSettings(Settings settings) {
        entryRepository.setEntry("bitmexUri", settings.getBitmexUri());
        entryRepository.setEntry("fromMail", settings.getFromMail());
        entryRepository.setEntry("timeInterval", settings.getTimeInterval());
    }

}
