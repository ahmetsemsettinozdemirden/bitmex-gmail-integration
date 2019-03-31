package business.settings;

import db.models.Entry;
import db.repository.EntryRepository;

import javax.inject.Inject;

/**
 * Fetches or Updates Settings.
 */
public class SettingsService {

    private EntryRepository entryRepository;

    @Inject
    public SettingsService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    /**
     * Fetches singular setting.
     * @param key Key of setting.
     * @return Setting entry.
     */
    public Entry getSetting(String key) {
        return entryRepository.getEntry(key);
    }

    /**
     * Fetches all settings.
     * @return All settings.
     */
    public Settings getSettings() {
        return new Settings(entryRepository.getEntry("bitmexUri").getValue(),
                            entryRepository.getEntry("fromMail").getValue(),
                            entryRepository.getEntry("timeInterval").getValue());
    }

    /**
     * Updates all settings.
     * @param settings Updated settings.
     */
    public void updateSettings(Settings settings) {
        entryRepository.setEntry("bitmexUri", settings.getBitmexUri());
        entryRepository.setEntry("fromMail", settings.getFromMail());
        entryRepository.setEntry("timeInterval", settings.getTimeInterval());
    }

}
