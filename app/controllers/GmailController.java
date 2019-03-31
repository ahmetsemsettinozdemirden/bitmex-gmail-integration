package controllers;

import business.exceptions.ClientException;
import db.repository.GmailRepository;
import business.gmail.GmailService;
import business.handlers.ErrorHandler;
import controllers.forms.GmailForm;
import db.models.GmailCredentials;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GmailController extends Controller {

    private final FormFactory formFactory;
    private final GmailRepository gmailRepository;
    private final GmailService gmailService;
    private final ErrorHandler errorHandler;

    @Inject
    public GmailController(FormFactory formFactory, GmailRepository gmailRepository,
                           GmailService gmailService, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.gmailRepository = gmailRepository;
        this.gmailService = gmailService;
        this.errorHandler = errorHandler;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {

        Form<GmailForm> gmailForm = formFactory.form(GmailForm.class).bind(request().body().asJson());

        if (gmailForm.hasErrors()) {
            ValidationError validationError = gmailForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "gmail-create-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        GmailForm gmailBody = gmailForm.get();

        GmailCredentials gmailCredentials;
        try {
            gmailCredentials = gmailRepository.addCredentials(gmailBody.email, gmailBody.credentials);
            gmailService.initialize();
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-create-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (IOException | GeneralSecurityException e) {
            return errorHandler.onServerError("gmail-create", e,
                    request().method() + " " + request().uri());
        }

        return created(Json.toJson(gmailCredentials));
    }

    public Result getAll() {
        return ok(Json.toJson(gmailRepository.getCredentials()));
    }

    public Result get(Long id) {
        GmailCredentials gmailCredentials = gmailRepository.getCredentials(id);

        if (gmailCredentials == null)
            return notFound();
        else
            return ok(Json.toJson(gmailCredentials));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(Long id) {

        Form<GmailForm> gmailForm = formFactory.form(GmailForm.class).bind(request().body().asJson());

        if (gmailForm.hasErrors()) {
            ValidationError validationError = gmailForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "gmail-create-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        GmailForm gmailBody = gmailForm.get();

        try {
            GmailCredentials credentials = gmailRepository.updateCredentials(id, gmailBody.email, gmailBody.credentials);
            gmailService.initialize();
            return ok(Json.toJson(credentials));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-delete-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (IOException | GeneralSecurityException e) {
            return errorHandler.onServerError("gmail-create", e,
                    request().method() + " " + request().uri());
        }
    }

    public Result delete(Long id) {
        try {
            gmailRepository.deleteCredentials(id);
            gmailService.initialize();
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-delete-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        } catch (IOException | GeneralSecurityException e) {
            return errorHandler.onServerError("gmail-create", e,
                    request().method() + " " + request().uri());
        }
        return ok();
    }
}
