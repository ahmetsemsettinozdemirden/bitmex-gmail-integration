package controllers.forms;

import play.data.validation.Constraints;

public class GmailForm {

    /**
     * Gmail account.
     */
    @Constraints.Required
    @Constraints.Email
    public String email;

    /**
     * Credentials from Gmail Developer Console for given Gmail account.
     */
    @Constraints.Required
    public String credentials;

}
