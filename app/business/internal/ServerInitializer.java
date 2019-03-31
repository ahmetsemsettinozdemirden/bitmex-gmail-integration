package business.internal;

import business.gmail.GmailService;
import business.handlers.DatabaseHandler;
import business.scheduler.QuartzScheduler;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Initial Class that runs after all injections completed. Since ebean has a problem with constructors, you must not
 * use any ebean queries in constructor. So that after all objects constructed, we initialize server.
 */
@Singleton
public class ServerInitializer {

    private final EbeanConfig ebeanConfig;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;
    private final DatabaseHandler databaseHandler;
    private final QuartzScheduler quartzScheduler;
    private final GmailService gmailService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public ServerInitializer(EbeanConfig ebeanConfig, EbeanDynamicEvolutions ebeanDynamicEvolutions,
                             DatabaseHandler databaseHandler, QuartzScheduler quartzScheduler,
                             GmailService gmailService) {
        this.ebeanConfig = ebeanConfig;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.databaseHandler = databaseHandler;
        this.quartzScheduler = quartzScheduler;
        this.gmailService = gmailService;
        initialize();
    }

    /**
     * Initialization of the server.
     */
    private void initialize() {
        try {
            logger.info("Initializing server...");
            // fix database state
            databaseHandler.start();
            // initialize services
            quartzScheduler.initialize();
            gmailService.initialize();
            logger.info("Server successfully initialized.");
        } catch (Exception e) {
            logger.error("server couldn't initialized!", e);
        }
    }

}
