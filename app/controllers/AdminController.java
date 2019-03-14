package controllers;

import business.admin.AdminService;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.handlers.ErrorHandler;
import controllers.forms.admin.AdminForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

@BodyParser.Of(BodyParser.Json.class)
public class AdminController extends Controller {

    private final FormFactory formFactory;
    private final AdminService adminService;
    private final ErrorHandler errorHandler;

    @Inject
    public AdminController(FormFactory formFactory, AdminService adminService, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.adminService = adminService;
        this.errorHandler = errorHandler;
    }

    public Result signIn() {

        Form<AdminForm> adminForm = formFactory.form(AdminForm.class).bind(request().body().asJson());

        if (adminForm.hasErrors()) {
            ValidationError validationError = adminForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "admin-signIn-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        AdminForm adminBody = adminForm.get();

        try {
            return ok(Json.toJson(adminService.signIn(adminBody.username.trim(), adminBody.password.trim())));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-signIn-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (ServerException e) {
            return errorHandler.onServerError("admin-signIn-" + e.getErrorCode(), e,
                    request().method() + " " + request().uri());
        }
    }

    public Result signUp() {

        Form<AdminForm> adminForm = formFactory.form(AdminForm.class).bind(request().body().asJson());

        if (adminForm.hasErrors()) {
            ValidationError validationError = adminForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "admin-signUp-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        AdminForm adminBody = adminForm.get();

        try {
            return ok(Json.toJson(adminService.signUp(adminBody.username.trim(), adminBody.password.trim())));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-signUp-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (ServerException e) {
            return errorHandler.onServerError("admin-signUp-" + e.getErrorCode(), e,
                    request().method() + " " + request().uri());
        }
    }

}
