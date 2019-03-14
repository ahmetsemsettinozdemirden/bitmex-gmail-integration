package controllers.forms.gmail;

import play.data.validation.Constraints;

public class GmailForm {

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String credentials;

}
