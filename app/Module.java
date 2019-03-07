import business.bitmex.BitmexHelper;
import business.gmail.GmailHelper;
import business.internal.ServerInitializer;
import business.jwt.JwtHelper;
import business.jwt.JwtHelperImpl;
import business.jwt.JwtValidator;
import business.jwt.JwtValidatorImpl;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {

        // jwt
        bind(JwtValidator.class).to(JwtValidatorImpl.class);
        bind(JwtHelper.class).to(JwtHelperImpl.class);

        // internal
        bind(ServerInitializer.class).asEagerSingleton();
    }

}
