package controllers.forms;

import play.data.validation.Constraints;

public class SettingsForm {

    @Constraints.Required
    public String bitmexUri;

    @Constraints.Required
    public String fromMail;

    @Constraints.Required
    public String timeInterval;

}
