package business.main;

import business.bitmex.BitmexHelper;
import business.bitmex.BitmexPosition;
import business.exceptions.ServerException;
import business.gmail.GmailHelper;
import play.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<BitmexPosition> desiredPositions = new ArrayList<>();
        try {
            List<String> tips = gmailHelper.getTradingViewTip();
            for (String tip: tips) {
                String[] parts = tip.split(",");

                if (parts.length != 2)
                    throw new RuntimeException("tip must include 2 parts, tip: " + tip);

                // if there are multiple tips for the same symbol, only add the latest tip
                if (!containsSymbol(desiredPositions, parts[0]))
                    desiredPositions.add(new BitmexPosition(parts[0], Integer.parseInt(parts[1])));
            }
            logger.debug("{} tips found.", desiredPositions.size());
        } catch (IOException e) {
            logger.error("email error", e);
            return;
        }

        if (desiredPositions.isEmpty()) {
            logger.debug("no bitmex updates...");
        } else {
            logger.debug("buy/sell on bitmex...");
            for (BitmexPosition desiredPosition: desiredPositions) {
                try {
                    BitmexPosition calculatedPosition = new BitmexPosition(desiredPosition.getSymbol(), desiredPosition.getQuantity());
                    List<BitmexPosition> currentPositions = bitmexHelper.getPositions();
                    for (BitmexPosition currentPosition: currentPositions) {
                        if (currentPosition.getSymbol().equals(desiredPosition.getSymbol())) {
                            calculatedPosition.setQuantity(desiredPosition.getQuantity() - currentPosition.getQuantity());
                        }
                    }
                    bitmexHelper.makeMarketOrder(calculatedPosition);
                } catch (ServerException e) {
                    logger.error("bitmex make order error!", e);
                }
            }
        }

        logger.info("GmailToBitmexJob is completed successfully.");
    }

    private boolean containsSymbol(List<BitmexPosition> positions, String symbol) {
        for (BitmexPosition position: positions)
            if (position.getSymbol().equals(symbol))
                return true;
        return false;
    }

}
