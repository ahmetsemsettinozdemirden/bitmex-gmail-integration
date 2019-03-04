package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

public class HomeController extends Controller {

    private WSClient wsClient;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public HomeController(WSClient wsClient) {
        this.wsClient = wsClient;
    }

    public Result index() {
        authBitmex();
        return ok(views.html.index.render());
    }

    private void authBitmex() {

        String apiKey = "ws5SHtePQFGMRpHzighepGs4";
        String apiSecret = "LGkqpU2j4cafYGmAxQmtAmXAIq4KfhwVcjexgNaWbFctOZYe";
        String data = "{\"symbol\":\"XBTUSD\",\"orderQty\":20,\"price\":590,\"ordType\":\"Limit\"}";
        String signature;

        String verb = "POST";
        String path = "/api/v1/order";
        long expires = LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        JsonNode jsonBody = Json.parse(data);

        String message = verb + path + expires + data;

//        try {
//            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
//            sha256_HMAC.init(secret_key);
//
//            signature = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
//            logger.debug("signature: {}", signature);
//        } catch (Exception e) {
//            logger.error("encryption error.", e);
//            return;
//        }

        try {

            Mac hasher = Mac.getInstance("HmacSHA256");
            hasher.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));

            byte[] hash = hasher.doFinal(message.getBytes());

            // to lowercase hexits
            signature = DatatypeConverter.printHexBinary(hash);
            logger.debug("signature: {}", signature);

            // to base64
            DatatypeConverter.printBase64Binary(hash);
        } catch (NoSuchAlgorithmException|InvalidKeyException e) {
            logger.error("encryption error.", e);
            return;
        }

        try {
            WSResponse response = wsClient.url("https://testnet.bitmex.com" + path)
                    .addHeader("content-type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("api-expires", Long.toString(expires))
                    .addHeader("api-key", apiKey)
                    .addHeader("api-signature", signature)
                    .post(jsonBody)
                    .toCompletableFuture()
                    .get();
            logger.info("response: {}", response.getBody());
        } catch (ExecutionException|InterruptedException e) {
            logger.error("bitmex request error.", e);
        }

    }

}
