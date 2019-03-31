import business.internal.ServerInitializer;
import business.jwt.JwtHelper;
import business.jwt.JwtHelperImpl;
import business.jwt.JwtValidator;
import business.jwt.JwtValidatorImpl;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

/**
 * @see <a href="https://www.playframework.com/documentation/2.6.x/Modules">Play Framework, Modules Docs</a>
 */
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
