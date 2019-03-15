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
import models.GmailCredentials;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Singleton
public class GmailService {

    // if you change SCOPES, delete tokens inside `gmail/tokens`
    private static final Set<String> SCOPES = Collections.singleton(GmailScopes.MAIL_GOOGLE_COM);
    private static final String APPLICATION_NAME = "bitmex-api"; // TODO: fetch from configs
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final Environment environment;
    private final GmailRepository gmailRepository;
    private List<Gmail> gmails;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GmailService(Environment environment, GmailRepository gmailRepository) {
        this.environment = environment;
        this.gmailRepository = gmailRepository;
        this.gmails = new ArrayList<>();
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential createCredentials(final NetHttpTransport HTTP_TRANSPORT, final GmailCredentials gmailCredentials) throws IOException {
        // Load client secrets.
        logger.debug("credentials email: {}.", gmailCredentials.getEmail());
        final InputStream in = new ByteArrayInputStream(gmailCredentials.getCredentials().getBytes(StandardCharsets.UTF_8));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(environment.rootPath() + "/gmail/tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // TODO: call initialize when emails are updated
    public void initialize() throws IOException, GeneralSecurityException {
        // free up resources
        for (Gmail gmail: gmails)
            gmail.getRequestFactory().getTransport().shutdown();
        this.gmails = new ArrayList<>();

        // initialize gmail clients
        List<GmailCredentials> gmailCredentialsList = gmailRepository.getCredentials();
        for(GmailCredentials gmailCredentials: gmailCredentialsList) {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            gmails.add(new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, createCredentials(HTTP_TRANSPORT, gmailCredentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build());
        }
    }

    public List<Gmail> getGmails() {
        return gmails;
    }

}
