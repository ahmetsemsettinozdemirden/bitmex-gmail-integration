package controllers.forms;

import play.data.validation.Constraints;

public class SettingsForm {

    /**
     * Base URI for Bitmex rest api. It should be main or testnet environment. See Bitmex docs for more details.
     */
    @Constraints.Required
    public String bitmexUri;

    /**
     * Mail account that sends TradingView tips.
     */
    @Constraints.Required
    public String fromMail;

    /**
     * Time interval of mail checks for TradingView tips.
     */
    @Constraints.Required
    public String timeInterval;

}
