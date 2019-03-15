package filters;

import akka.stream.Materializer;
import business.admin.AdminRepository;
import business.handlers.ErrorHandler;
import business.jwt.JwtAttrs;
import business.jwt.JwtValidator;
import business.jwt.VerifiedJwt;
import models.Admin;
import play.libs.F;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.Router;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.UNAUTHORIZED;

public class JwtFilter extends Filter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ERR_ROUTE_NOT_FOUND = "ERR_ROUTE_NOT_FOUND";
    private static final String ERR_ADMIN_NOT_FOUND = "ERR_ADMIN_NOT_FOUND";
    private static final String ERR_TOKEN_MISMATCH = "ERR_TOKEN_MISMATCH";

    private final AdminRepository adminRepository;
    private final JwtValidator jwtValidator;
    private final ErrorHandler errorHandler;

    @Inject
    public JwtFilter(Materializer mat, AdminRepository adminRepository, JwtValidator jwtValidator, ErrorHandler errorHandler) {
        super(mat);
        this.adminRepository = adminRepository;
        this.jwtValidator = jwtValidator;
        this.errorHandler = errorHandler;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter, Http.RequestHeader requestHeader) {

        if(!requestHeader.attrs().containsKey(Router.Attrs.HANDLER_DEF))
            return errorHandler.onClientErrorCompletionStage(requestHeader, NOT_FOUND, "ERR_ROUTE_NOT_FOUND",  ERR_ROUTE_NOT_FOUND);

        Optional<String> authHeader = requestHeader.getHeaders().get(HEADER_AUTHORIZATION);

        if (authHeader.filter(ah -> ah.contains(BEARER)).isPresent()) {

            String token = authHeader.map(ah -> ah.replace(BEARER, "")).orElse("");
            F.Either<business.jwt.JwtValidator.Error, VerifiedJwt> res = jwtValidator.verify(token);

            if (res.left.isPresent()) {
                return errorHandler.onClientErrorCompletionStage(requestHeader, UNAUTHORIZED, "UNAUTHORIZED", res.left.get().toString());
            }

            VerifiedJwt verifiedJwt = res.right.get();

            Admin admin = adminRepository.get(verifiedJwt.getUsername());

            if (admin == null)
                return errorHandler.onClientErrorCompletionStage(requestHeader, BAD_REQUEST, "ERR_ADMIN_NOT_FOUND", ERR_ADMIN_NOT_FOUND);

            if (!admin.getToken().equals(token))
                return errorHandler.onClientErrorCompletionStage(requestHeader, BAD_REQUEST, "ERR_TOKEN_MISMATCH", ERR_TOKEN_MISMATCH);

            return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()
                    .put(JwtAttrs.VERIFIED_ADMIN, admin)));
        }

        return nextFilter.apply(requestHeader.withAttrs(requestHeader.attrs()));
    }
}