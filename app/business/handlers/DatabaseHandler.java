package business.handlers;

import business.admin.AdminService;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.settings.Settings;
import business.settings.SettingsService;
import db.models.BitmexCredentials;
import db.repository.AdminRepository;
import db.repository.BitmexRepository;
import play.Logger;

import javax.inject.Inject;

/**
 * Checks Database for initial state.
 */
public class DatabaseHandler {

    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final BitmexRepository bitmexRepository;
    private final SettingsService settingsService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public DatabaseHandler(AdminRepository adminRepository, AdminService adminService,
                           BitmexRepository bitmexRepository, SettingsService settingsService) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.bitmexRepository = bitmexRepository;
        this.settingsService = settingsService;
    }

    /**
     * Checks Admin, BitmexCredentials and Settings to make sure that they are in the correct state.
     * If not makes them in correct state.
     */
    public void start() {
        logger.info("Database Handler starting...");
        createDefaultAdmin();
        createEmptyBitmexCredentials();
        createDefaultSettings();
        logger.info("Database Handler successfully completed.");
    }

    private void createDefaultAdmin() {
        if (adminRepository.getTotalAdminCount() == 0) {
            try {
                adminService.signUp("test", "test");
                logger.info("test admin inserted.");
            } catch (ClientException | ServerException e) {
                logger.error("create default admin error.", e);
            }
        }
    }

    private void createEmptyBitmexCredentials() {
        if (bitmexRepository.getCredentials() == null) {
            new BitmexCredentials("-", "-").save();
        }
    }

    private void createDefaultSettings() {
        if (settingsService.getSetting("bitmexUri") == null) {
            settingsService.updateSettings(new Settings(
                    "https://testnet.bitmex.com",
                    "ahmetozdemirden@std.iyte.edu.tr",
                    "15"));
        }
    }

}
