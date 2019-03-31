package business.settings;

/**
 * Includes settings for server.
 */
public class Settings {

    private String bitmexUri;
    private String fromMail;
    private String timeInterval;

    /**
     * Creates a Settings object.
     * @param bitmexUri Base URI for Bitmex rest api. It should be main or testnet environment. See Bitmex docs for more details.
     * @param fromMail Mail account that sends TradingView tips.
     * @param timeInterval Time interval of mail checks for TradingView tips.
     */
    public Settings(String bitmexUri, String fromMail, String timeInterval) {
        this.bitmexUri = bitmexUri;
        this.fromMail = fromMail;
        this.timeInterval = timeInterval;
    }

    public String getBitmexUri() {
        return bitmexUri;
    }

    public String getFromMail() {
        return fromMail;
    }

    public String getTimeInterval() {
        return timeInterval;
    }
}
