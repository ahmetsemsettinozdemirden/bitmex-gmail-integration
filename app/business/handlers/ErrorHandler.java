package business.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    private Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public ErrorHandler(Config config, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(Results.status(
                statusCode , jsonError("clienterror", message, request.method() + " " + request.uri())));
    }

    public CompletionStage<Result> onClientErrorCompletionStage(RequestHeader request, int statusCode, String errorCode, String message) {
        return CompletableFuture.completedFuture(Results.status(
                statusCode , jsonError(errorCode, message, request.method() + " " + request.uri())));
    }

    public Result onClientError(int statusCode, String errorCode, String message, String description) {
        return Results.status(statusCode , jsonError(errorCode, message, description));
    }

    public Result onServerError(String errorCode, Throwable e, String description) {
        long contextId = Http.Context.current().id();
        logger.error("[{}] onServerError {}, msg: {}", contextId, errorCode, e.getMessage());
        logger.error("[" + contextId + "] onServerError", e);
        return Results.status(500 , jsonError(errorCode, e.getMessage(), description));
    }

    @Override
    public CompletionStage<Result> onDevServerError(RequestHeader request, UsefulException exception) {
        long contextId = Http.Context.current().id();
        logger.error("[{}] onDevError internalservererror, title: {}, desc: {}, msg: {}", contextId, exception.title, exception.description, exception.getMessage());
        logger.error("[" + contextId + "] onDevError", exception);
        return CompletableFuture.completedFuture(Results.internalServerError(jsonError("internalservererror", exception.title,"uri" + request.uri() + " " + exception.description)));
    }

    @Override
    public CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {
        long contextId = Http.Context.current().id();
        logger.error("[{}] onProdError internalservererror, title: {}, desc: {}, msg: {}",
                contextId, exception.title, exception.description, exception.getMessage());
        logger.error("[" + contextId + "] onProdError", exception);
        return CompletableFuture.completedFuture(Results.internalServerError(
                jsonError("internalservererror", exception.title,
                        "uri" + request.uri() + " " + exception.description)));
    }

    private JsonNode jsonError(String code, String message, String description) {
        return Json.newObject()
                .put("code", code)
                .put("message", message)
                .put("description", description);
    }
}