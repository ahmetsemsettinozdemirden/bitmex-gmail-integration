package business.bitmex;

import com.fasterxml.jackson.databind.JsonNode;
import models.BitmexCredentials;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

public class BitmexHelper {

    private final BitmexRepository bitmexRepository;
    private final WSClient wsClient;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public BitmexHelper(BitmexRepository bitmexRepository, WSClient wsClient) {
        this.bitmexRepository = bitmexRepository;
        this.wsClient = wsClient;
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

    private void createRequest() {

        BitmexCredentials bitmexCredentials = bitmexRepository.getCredentials();
        String signature;

        String data = "{\"symbol\":\"XBTUSD\",\"orderQty\":50,\"ordType\":\"Market\"}";

        String verb = "POST";
        String path = "/api/v1/order";
        long expires = LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        JsonNode jsonBody = Json.parse(data);

        try {
            signature = calculateHash(bitmexCredentials.getApiSecret(), verb, path, expires, data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // TODO: throw server error
            logger.error("hash calculation error.", e);
            return;
        }

        try {
            WSResponse response = wsClient.url("https://testnet.bitmex.com" + path)
                    .addHeader("content-type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("api-expires", Long.toString(expires))
                    .addHeader("api-key", bitmexCredentials.getApiKey())
                    .addHeader("api-signature", signature)
                    .post(data)
                    .toCompletableFuture()
                    .get();
            logger.info("response: {}", response.getBody());
        } catch (ExecutionException | InterruptedException e) {
            logger.error("bitmex request error.", e);
        }

    }

}
