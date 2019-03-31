package db.repository;

import business.exceptions.ClientException;
import db.models.GmailCredentials;

import java.util.List;

/**
 * Gmail credentials repository that includes create, fetch, update and delete methods.
 */
public class GmailRepository {

    /**
     * Creates new Gmail credentials with given data.
     * @param email Gmail credentials email.
     * @param credentials Gmail credentials credentials.
     * @return Created Gmail credentials.
     * @throws ClientException Gmail credentials already exists.
     */
    public GmailCredentials addCredentials(String email, String credentials) throws ClientException {

        if (email == null || email.equals(""))
            throw new ClientException("nullEmail", "email can not be null or empty.");

        if (credentials == null || credentials.equals(""))
            throw new ClientException("nullApiSecret", "apiSecret can not be null or empty.");

        if (getCredentials(email) != null)
            throw new ClientException("emailExists", "email already exists!");

        GmailCredentials gmailCredentials = new GmailCredentials(email, credentials);
        gmailCredentials.save();
        return gmailCredentials;
    }

    /**
     * Fetches Gmail credentials by given id.
     * @param id Gmail credential id.
     * @return Gmail credentials.
     */
    public GmailCredentials getCredentials(Long id) {
        return GmailCredentials.finder.byId(id);
    }

    /**
     * Fetches Gmail credentials by given email.
     * @param email Gmail credential email.
     * @return Gmail credentials.
     */
    public GmailCredentials getCredentials(String email) {
        return GmailCredentials.finder.query().where().eq("email", email).findOne();
    }

    /**
     * Fetches all Gmail credentials.
     * @return All Gmail credentials.
     */
    public List<GmailCredentials> getCredentials() {
        return GmailCredentials.finder.all();
    }

    /**
     * Updates Gmail credentials by given id and data.
     * @param id Gmail credentials id.
     * @param email Gmail credentials email.
     * @param credentials Gmail credentials credentials.
     * @return Updated Gmail credentials.
     * @throws ClientException Gmail credentials does not exist.
     */
    public GmailCredentials updateCredentials(Long id, String email, String credentials) throws ClientException {

        GmailCredentials gmailCredentials = getCredentials(id);

        if (gmailCredentials == null)
            throw new ClientException("credentialsNotFound", "credentials could not found!");

        gmailCredentials.setEmail(email);
        gmailCredentials.setCredentials(credentials);
        gmailCredentials.save();
        return gmailCredentials;
    }

    /**
     * Deletes Gmail credentials by given.
     * @param id Gmail credentials id.
     * @return Deleted Gmail credentials.
     * @throws ClientException Gmail credentials does not exist.
     */
    public boolean deleteCredentials(Long id) throws ClientException {

        GmailCredentials gmailCredentials = getCredentials(id);

        if (gmailCredentials == null)
            throw new ClientException("credentialsNotFound", "credentials could not found!");

        return gmailCredentials.delete();
    }

}
