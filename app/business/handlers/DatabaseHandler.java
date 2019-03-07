package business.handlers;

import business.admin.AdminHelper;
import business.admin.AdminRepository;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import play.Logger;

import javax.inject.Inject;

public class DatabaseHandler {

    private final AdminRepository adminRepository;
    private final AdminHelper adminHelper;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public DatabaseHandler(AdminRepository adminRepository, AdminHelper adminHelper) {
        this.adminRepository = adminRepository;
        this.adminHelper = adminHelper;
    }

    public void start() {
        logger.info("Database Handler starting...");
        createDefaultAdmin();
        logger.info("Database Handler successfully completed.");
    }

    private void createDefaultAdmin() {
        if (adminRepository.getTotalAdminCount() == 0) {
            try {
                adminHelper.signUp("test", "test");
                logger.info("test admin inserted.");
            } catch (ClientException|ServerException e) {
                logger.error("create default admin error.", e);
            }
        }
    }

}
