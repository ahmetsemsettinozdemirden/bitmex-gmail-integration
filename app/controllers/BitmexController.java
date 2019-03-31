package controllers;

import db.repository.BitmexRepository;
import business.exceptions.ClientException;
import business.handlers.ErrorHandler;
import controllers.forms.BitmexForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class BitmexController extends Controller {

    private final FormFactory formFactory;
    private final BitmexRepository bitmexRepository;
    private final ErrorHandler errorHandler;

    @Inject
    public BitmexController(FormFactory formFactory, BitmexRepository bitmexRepository, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.bitmexRepository = bitmexRepository;
        this.errorHandler = errorHandler;
    }

    public Result get() {
        return ok(Json.toJson(bitmexRepository.getCredentials()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update() {

        Form<BitmexForm> bitmexForm = formFactory.form(BitmexForm.class).bind(request().body().asJson());

        if (bitmexForm.hasErrors()) {
            ValidationError validationError = bitmexForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "bitmex-update-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        BitmexForm bitmexBody = bitmexForm.get();

        try {
            bitmexRepository.setCredentials(bitmexBody.apiKey.trim(), bitmexBody.apiSecret.trim());
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "bitmex-update-" + e.getErrorCode(), e.getMessage(),
                    request().method() + " " + request().uri());
        }

        return ok();
    }

}
