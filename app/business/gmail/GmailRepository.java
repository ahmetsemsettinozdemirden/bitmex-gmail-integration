package business.gmail;

import business.exceptions.ClientException;
import models.GmailCredentials;

import java.util.List;

public class GmailRepository {

    public GmailCredentials addCredentials(String email, String credentials) throws ClientException {

        if (email == null || email.equals(""))
            throw new ClientException("email", "email can not be null or empty.");

        if (credentials == null || credentials.equals(""))
            throw new ClientException("apiSecret", "apiSecret can not be null or empty.");

        if (getCredentials(email) != null)
            throw new ClientException("emailexists", "email already exists!");

        GmailCredentials gmailCredentials = new GmailCredentials(email, credentials);
        gmailCredentials.save();
        return gmailCredentials;
    }

    public GmailCredentials getCredentials(Long id) {
        return GmailCredentials.finder.byId(id);
    }

    public GmailCredentials getCredentials(String email) {
        return GmailCredentials.finder.query().where().eq("email", email).findOne();
    }

    public List<GmailCredentials> getCredentials() {
        return GmailCredentials.finder.all();
    }

    public GmailCredentials updateCredentials(Long id, String email, String credentials) throws ClientException {

        GmailCredentials gmailCredentials = getCredentials(id);

        if (gmailCredentials == null)
            throw new ClientException("credentialsdoesnotexist", "credentials does not exist!");

        gmailCredentials.setEmail(email);
        gmailCredentials.setCredentials(credentials);
        gmailCredentials.save();
        return gmailCredentials;
    }

    public boolean deleteCredentials(Long id) throws ClientException {

        GmailCredentials gmailCredentials = getCredentials(id);

        if (gmailCredentials == null)
            throw new ClientException("credentialsdoesnotexist", "credentials does not exist!");

        return gmailCredentials.delete();
    }

}
