package controllers;

import business.admin.AdminService;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.handlers.ErrorHandler;
import controllers.forms.AdminForm;
import db.models.Admin;
import db.repository.AdminRepository;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * This Controller consists of SignIn, SignUp and CRUD operations of admin account.
 */
public class AdminController extends Controller {

    private final FormFactory formFactory;
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final ErrorHandler errorHandler;

    @Inject
    public AdminController(FormFactory formFactory, AdminService adminService,
                           AdminRepository adminRepository, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.errorHandler = errorHandler;
    }

    /**
     * Sign-in Admin, check username and password, generate and return token.
     * @return Admin account with Berarer token.
     */
    @BodyParser.Of(BodyParser.Json.class)
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

    /**
     * Sign-up Admin, check username and password, check username existance, generate token, save and return admin
     * account.
     * @return Created Admin with Bearer token.
     */
    @BodyParser.Of(BodyParser.Json.class)
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

    /**
     * Fetches all admin accounts.
     * @return All admin accounts.
     */
    public Result getAll() {
        return ok(Json.toJson(adminRepository.getAll()));
    }

    /**
     * Fetches an Admin account by id.
     * @param id Admin id.
     * @return Admin account.
     */
    public Result get(Long id) {
        Admin admin = adminRepository.get(id);

        if (admin == null)
            return notFound();
        else
            return ok(Json.toJson(admin));
    }

    /**
     * Updates an Admin account for given body data by id.
     * @param id Admin id.
     * @return Updated admin.
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result update(Long id) {

        Form<AdminForm> adminForm = formFactory.form(AdminForm.class).bind(request().body().asJson());

        if (adminForm.hasErrors()) {
            ValidationError validationError = adminForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "admin-update-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        AdminForm adminBody = adminForm.get();

        try {
            return ok(Json.toJson(adminRepository.update(id, adminBody.username.trim(), adminBody.password.trim())));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-update-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        }
    }

    /**
     * Deletes an admin account by id.
     * @param id Admin id.
     * @return Deleted admin.
     */
    public Result delete(Long id) {
        try {
            adminRepository.delete(id);
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "admin-delete-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        }
        return ok();
    }
}
