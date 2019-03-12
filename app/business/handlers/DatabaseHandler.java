package business.handlers;

import business.admin.AdminService;
import business.admin.AdminRepository;
import business.bitmex.BitmexRepository;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import models.BitmexCredentials;
import play.Logger;

import javax.inject.Inject;

public class DatabaseHandler {

    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final BitmexRepository bitmexRepository;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public DatabaseHandler(AdminRepository adminRepository, AdminService adminService, BitmexRepository bitmexRepository) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.bitmexRepository = bitmexRepository;
    }

    public void start() {
        logger.info("Database Handler starting...");
        createDefaultAdmin();
        createEmptyBitmexCredentials();
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

}
