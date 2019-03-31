package controllers.forms;

import play.data.validation.Constraints;

public class BitmexForm {

    /**
     * Api Key from Bitmex dashboard.
     */
    @Constraints.Required
    public String apiKey;

    /**
     * Api Secret from Bitmex dashboard.
     */
    @Constraints.Required
    public String apiSecret;

}
