package business.gmail;

import akka.stream.Materializer;
import com.typesafe.config.ConfigFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.Helpers;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;

public class GmailServiceTest {

    private static Application app;
    private static Materializer mat;

    public static Application getApp() {
        if(app == null) {
            app = new GuiceApplicationBuilder().loadConfig(ConfigFactory.load("application.conf")).in(Mode.TEST).build();
            Helpers.start(app);
        }
        return app;
    }

    @BeforeClass
    public static void startPlay() {
        app = getApp();
        mat = app.getWrappedApplication().materializer();
    }

    @Test
    public void testGmailService() {
        GmailService gmailService = app.injector().instanceOf(GmailService.class);
        assertNotNull(gmailService.getGmail());
    }

    @Test
    public void testGmailHelper() throws IOException {
        GmailHelper gmailHelper = app.injector().instanceOf(GmailHelper.class);
        gmailHelper.getTradingViewTip();
    }

}
