package controllers.forms;

import play.data.validation.Constraints;

public class AdminForm {

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

}
