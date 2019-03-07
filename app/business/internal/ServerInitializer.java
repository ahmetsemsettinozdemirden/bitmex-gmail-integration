package business.internal;

import business.handlers.DatabaseHandler;
import business.scheduler.QuartzScheduler;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerInitializer {

    private final EbeanConfig ebeanConfig;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;
    private final DatabaseHandler databaseHandler;
    private final QuartzScheduler quartzScheduler;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public ServerInitializer(EbeanConfig ebeanConfig, EbeanDynamicEvolutions ebeanDynamicEvolutions,
                             DatabaseHandler databaseHandler, QuartzScheduler quartzScheduler) {
        this.ebeanConfig = ebeanConfig;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.databaseHandler = databaseHandler;
        this.quartzScheduler = quartzScheduler;
        initialize();
    }

    private void initialize() {
        try {
            logger.info("Initializing server...");
            databaseHandler.start();
            quartzScheduler.initialize();
            logger.info("Server successfully initialized.");
        } catch (Exception e) {
            logger.error("server couldn't initialized!", e);
        }
    }

}
