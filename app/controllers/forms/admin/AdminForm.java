package controllers.forms.admin;

import play.data.validation.Constraints;

public class AdminForm {

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

}
