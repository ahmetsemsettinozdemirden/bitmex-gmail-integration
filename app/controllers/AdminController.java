package controllers;

import business.admin.AdminHelper;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.handlers.ErrorHandler;
import controllers.forms.admin.AdminForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.inject.Inject;

import static play.mvc.Http.Context.Implicit.request;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Results.ok;

@BodyParser.Of(BodyParser.Json.class)
public class AdminController {

    private final FormFactory formFactory;
    private final AdminHelper adminHelper;
    private final ErrorHandler errorHandler;

    @Inject
    public AdminController(FormFactory formFactory, AdminHelper adminHelper, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.adminHelper = adminHelper;
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
            return ok(Json.toJson(adminHelper.signIn(adminBody.username, adminBody.password)));
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
            return ok(Json.toJson(adminHelper.signUp(adminBody.username, adminBody.password)));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-signUp-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (ServerException e) {
            return errorHandler.onServerError("admin-signUp-" + e.getErrorCode(), e,
                    request().method() + " " + request().uri());
        }
    }

}
