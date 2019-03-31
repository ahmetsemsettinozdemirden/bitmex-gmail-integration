package db.repository;

import business.exceptions.ClientException;
import db.models.BitmexCredentials;

/**
 * This repository manages singular Bitmex Credentials.
 */
public class BitmexRepository {

    /**
     * Fetches Bitmex credentials.
     * @return Bitmex credentials.
     */
    public BitmexCredentials getCredentials() {
        return BitmexCredentials.finder.query().where().setMaxRows(1).findOne();
    }

    /**
     * Checks and updates singular Bitmex credentials.
     * @param apiKey Api key of Bitmex account.
     * @param apiSecret Api secret of Bitmex account.
     * @return Updated Bitmex credentials.
     * @throws ClientException ApiKey or ApiSecret is invalid.
     */
    public BitmexCredentials setCredentials(String apiKey, String apiSecret) throws ClientException {

        if (apiKey == null || apiKey.equals(""))
            throw new ClientException("apiKey", "apiKey can not be null or empty.");

        if (apiSecret == null || apiSecret.equals(""))
            throw new ClientException("apiSecret", "apiSecret can not be null or empty.");

        BitmexCredentials bitmexCredentials = getCredentials();
        bitmexCredentials.setApiKey(apiKey);
        bitmexCredentials.setApiSecret(apiSecret);
        bitmexCredentials.save();
        return bitmexCredentials;
    }

}
