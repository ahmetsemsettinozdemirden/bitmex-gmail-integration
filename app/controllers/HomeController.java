package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

public class HomeController extends Controller {

    private WSClient wsClient;

    @Inject
    public HomeController(WSClient wsClient) {
        this.wsClient = wsClient;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    private void authBitmex() {

        String apiKey = "";
        String apiSecret = "";

        String verb = "POST";
        String path = "/api/v1/order";
        long expires = LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        JsonNode data = Json.parse("{symbol:\"XBTUSD\",orderQty:20,price:590,ordType:\"Limit\"}");

        String signature = "signature";// crypto.createHmac('sha256', apiSecret).update(verb + path + expires + postBody).digest('hex');

        try {
            wsClient.url("https://testnet.bitmex.com" + path)
                    .addHeader("content-type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("api-expires", Long.toString(expires))
                    .addHeader("api-key", apiKey)
                    .addHeader("api-signature",signature)
                    .post(data)
                    .toCompletableFuture()
                    .get();
        } catch (ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }

    }

}
