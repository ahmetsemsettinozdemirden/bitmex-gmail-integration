package controllers;

import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    private void authBitmex() {

        String apiKey = "";
        String apiSecret = "";

        String verb = "POST";
        String path = "/api/v1/order";
        long expires = Math.round(new Date().getTime() / 1000) + 60;
        JsonNode data = JSON.parse("{symbol:\"XBTUSD\",orderQty:20,price:590,ordType:\"Limit\"}");

        String signature = "signature";// crypto.createHmac('sha256', apiSecret).update(verb + path + expires + postBody).digest('hex');

        ws.get().addHeader("content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("api-expires", expires)
                .addHeader("api-key", apiKey)
                .addHeader("api-signature",signature)
                .body(data)
                .post("https://testnet.bitmex.com" + path);

    }

}
