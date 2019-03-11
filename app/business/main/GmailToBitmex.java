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
        String tip = null;
        try {
            tip = gmailHelper.getTradingViewTip();
        } catch (Exception e) {
//            logger.error("email error", e);
            return;
        }
        if (tip == null)
            throw new RuntimeException("tip format problem, tip: " + tip);

        logger.debug("buy/sell on bitmex...");

        String[] parts = tip.split(",");

        if (parts.length != 2)
            throw new RuntimeException("tip must include 2 parts, tip: " + tip);

        String currency = parts[0];
        int orderQty = Integer.parseInt(parts[1]);

        bitmexHelper.createRequest(currency, orderQty);

        logger.info("GmailToBitmexJob is completed successfully.");
    }

}
