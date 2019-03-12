package controllers.forms.bitmex;

import play.data.validation.Constraints;

public class BitmexForm {

    @Constraints.Required
    public String apiKey;

    @Constraints.Required
    public String apiSecret;

}
