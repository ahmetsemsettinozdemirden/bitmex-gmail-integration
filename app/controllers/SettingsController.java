package controllers;

import business.handlers.ErrorHandler;
import business.settings.Settings;
import business.settings.SettingsService;
import controllers.forms.SettingsForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * This Controller consists of Get and Update operations of Settings. Since settings are defined in the system user
 * should only update settings, not create new ones. Therefore there is no create or delete Settings.
 */
public class SettingsController extends Controller {

    private final FormFactory formFactory;
    private final SettingsService settingsService;
    private final ErrorHandler errorHandler;

    @Inject
    public SettingsController(FormFactory formFactory, SettingsService settingsService, ErrorHandler errorHandler) {
        this.formFactory = formFactory;
        this.settingsService = settingsService;
        this.errorHandler = errorHandler;
    }

    /**
     * Fetches applied settings.
     * @return Applied settings.
     */
    public Result get() {
        return ok(Json.toJson(settingsService.getSettings()));
    }

    /**
     * Updates settings for given body data.
     * @return Updated settings.
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result update() {

        Form<SettingsForm> settingsForm = formFactory.form(SettingsForm.class).bind(request().body().asJson());

        if (settingsForm.hasErrors()) {
            ValidationError validationError = settingsForm.allErrors().get(0);
            return errorHandler.onClientError(BAD_REQUEST, "settings-update-form-" + validationError.key(),
                    validationError.key() + " - " + validationError.message(),
                    request().method() + " " + request().uri());
        }

        SettingsForm settingsBody = settingsForm.get();
        Settings settings = new Settings(settingsBody.bitmexUri, settingsBody.fromMail, settingsBody.timeInterval);
        settingsService.updateSettings(settings);
        return ok();
    }

}
