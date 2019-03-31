package filters;

import akka.stream.Materializer;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Logs any endpoint activity.
 */
public class AuditLoggerFilter extends Filter {

    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public AuditLoggerFilter(Materializer mat) {
        super(mat);
    }

    /**
     * Saves start time and logs endpoint activity with request path, verb, request time and request status.
     * @param nextFilter Following filter provided by Filters class.
     * @param requestHeader Header part of the http request.
     * @return Request with `Request-Time` header.
     */
    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, Http.RequestHeader requestHeader) {
        long startTime = System.currentTimeMillis();
        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;
            logger.info(String.format("%-60s %5sms code=%-4s", requestHeader.toString(), requestTime, result.status()));
            return result.withHeader("Request-Time", "" + requestTime);
        });
    }

}
