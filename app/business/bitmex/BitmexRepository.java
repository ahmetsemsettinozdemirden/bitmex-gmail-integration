package business.bitmex;

import business.exceptions.ClientException;
import models.BitmexCredentials;

public class BitmexRepository {

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

    public BitmexCredentials getCredentials() {
        return BitmexCredentials.finder.query().where().setMaxRows(1).findOne();
    }

}
