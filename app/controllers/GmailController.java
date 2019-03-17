package controllers;

import business.exceptions.ClientException;
import business.gmail.GmailRepository;
import business.handlers.ErrorHandler;
import controllers.forms.GmailForm;
import models.GmailCredentials;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class GmailController extends Controller {

    private final FormFactory formFactory;
    private final GmailRepository gmailRepository;
    private final ErrorHandler errorHandler;

    @Inject
    public GmailController(FormFactory formFactory, GmailRepository gmailRepository, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.gmailRepository = gmailRepository;
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
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-create-" + e.getErrorCode(), e.getMessage(),
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
            return ok(Json.toJson(gmailRepository.updateCredentials(id, gmailBody.email, gmailBody.credentials)));
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-delete-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        }
    }

    public Result delete(Long id) {
        try {
            gmailRepository.deleteCredentials(id);
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "gmail-delete-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        }
        return ok();
    }
}
