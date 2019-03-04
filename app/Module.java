import com.google.inject.AbstractModule;
import business.jwt.JwtHelper;
import business.jwt.JwtHelperImpl;
import business.jwt.JwtValidator;
import business.jwt.JwtValidatorImpl;
import play.libs.akka.AkkaGuiceSupport;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {

        // jwt
        bind(JwtValidator.class).to(JwtValidatorImpl.class);
        bind(JwtHelper.class).to(JwtHelperImpl.class);

    }

}
