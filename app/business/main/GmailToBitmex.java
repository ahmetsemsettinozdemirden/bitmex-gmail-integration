package business.main;

import business.bitmex.BitmexHelper;
import business.gmail.GmailHelper;
import play.Logger;

import javax.inject.Inject;

public class GmailToBitmex {

    private final GmailHelper gmailHelper;
    private final BitmexHelper bitmexHelper;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GmailToBitmex(GmailHelper gmailHelper, BitmexHelper bitmexHelper) {
        this.gmailHelper = gmailHelper;
        this.bitmexHelper = bitmexHelper;
    }

    public void execute() {
        logger.info("GmailToBitmexJob is starting...");

        logger.debug("checking gmail...");
        try {
            gmailHelper.getThreads();
        } catch (Exception e) {}

        logger.debug("buy/sell on bitmex...");
        // TODO: buy/sell on bitmex

        logger.info("GmailToBitmexJob is completed successfully.");
    }

}
