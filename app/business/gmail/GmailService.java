package business.gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Singleton
public class GmailService {

    // if you change SCOPES, delete tokens inside `gmail/tokens`
    private static final Set<String> SCOPES = Collections.singleton(GmailScopes.MAIL_GOOGLE_COM);
    private static final String APPLICATION_NAME = "bitmex-api"; // TODO: fetch from configs
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final Environment environment;
    private final Logger.ALogger logger = Logger.of(this.getClass());
    private Gmail gmail;

    @Inject
    public GmailService(Environment environment) {
        this.environment = environment;
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        logger.info("credential path: {}", environment.rootPath() + "/gmail/credentials.json");
        InputStream in = new FileInputStream(environment.rootPath() + "/gmail/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(environment.rootPath() + "/gmail/tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void initialize() throws IOException, GeneralSecurityException {
        if (gmail == null) {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            this.gmail = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } else {
            throw new RuntimeException("Gmail instance is already initialized!");
        }
    }

    public Gmail getGmail() {
        return gmail;
    }

}
