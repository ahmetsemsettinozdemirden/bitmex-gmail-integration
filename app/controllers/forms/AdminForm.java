package controllers.forms;

import play.data.validation.Constraints;

public class AdminForm {

    /**
     * Username of the admin.
     */
    @Constraints.Required
    public String username;

    /**
     * Password of the admin.
     */
    @Constraints.Required
    public String password;

}
