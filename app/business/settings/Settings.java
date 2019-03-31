package business.settings;

public class Settings {

    private String bitmexUri;
    private String fromMail;
    private String timeInterval;

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
