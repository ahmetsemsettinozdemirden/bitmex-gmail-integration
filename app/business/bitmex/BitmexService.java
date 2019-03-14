package business.bitmex;

import business.exceptions.ServerException;
import models.BitmexCredentials;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BitmexService {

    private final WSClient wsClient;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public BitmexService(WSClient wsClient) {
        this.wsClient = wsClient;
    }

    public WSRequest createRequest(BitmexCredentials bitmexCredentials, String data, String verb, String path)
            throws ServerException {

        if (bitmexCredentials == null)
            throw new RuntimeException("bitmexCredentials can not be null.");

        if (data == null || data.equals(""))
            throw new RuntimeException("data can not be null or empty.");

        if (!"get".equalsIgnoreCase(verb) && !"post".equalsIgnoreCase(verb) &&
                !"put".equalsIgnoreCase(verb) && !"delete".equalsIgnoreCase(verb))
            throw new RuntimeException("verb should be an http request method, see: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods");

        if (path == null || path.equals(""))
            throw new RuntimeException("path can not be null or empty.");

        String signature;
        long expires = LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        try {
            signature = calculateHash(bitmexCredentials.getApiSecret(), verb, path, expires, data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ServerException("hash-calculation", e);
        }

        return wsClient.url("https://testnet.bitmex.com" + path)
                .addHeader("content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("api-expires", Long.toString(expires))
                .addHeader("api-key", bitmexCredentials.getApiKey())
                .addHeader("api-signature", signature)
                .setContentType("application/json")
                .setBody(data)
                .setMethod(verb.toUpperCase());
    }

    private String calculateHash(String apiSecret, String verb, String path, long expires, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {

        String message = verb + path + expires + data;
        logger.debug("message: {}", message);

        Mac hasher = Mac.getInstance("HmacSHA256");
        hasher.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));

        byte[] hash = hasher.doFinal(message.getBytes());

        // to lowercase hexits
        String signature = DatatypeConverter.printHexBinary(hash);
        logger.debug("signature: {}", signature);

        return signature;
    }

}
