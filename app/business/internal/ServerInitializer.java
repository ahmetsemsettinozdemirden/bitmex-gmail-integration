package business.internal;

import business.handlers.DatabaseHandler;
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
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public ServerInitializer(EbeanConfig ebeanConfig, EbeanDynamicEvolutions ebeanDynamicEvolutions,
                             DatabaseHandler databaseHandler) {
        this.ebeanConfig = ebeanConfig;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.databaseHandler = databaseHandler;
        initialize();
    }

    private void initialize() {
        try {
            databaseHandler.start();
            logger.info("server successfully initialized.");
        } catch (Exception e) {
            logger.error("server couldn't initialized!", e);
        }
    }

}
