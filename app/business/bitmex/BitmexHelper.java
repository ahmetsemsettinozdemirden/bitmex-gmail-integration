package business.bitmex;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import com.fasterxml.jackson.databind.JsonNode;
import models.BitmexCredentials;
import play.Logger;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BitmexHelper {

    private final BitmexRepository bitmexRepository;
    private final BitmexService bitmexService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public BitmexHelper(BitmexRepository bitmexRepository, BitmexService bitmexService) {
        this.bitmexRepository = bitmexRepository;
        this.bitmexService = bitmexService;
    }

    public List<BitmexPosition> getPositions() throws ServerException {

        BitmexCredentials bitmexCredentials = bitmexRepository.getCredentials();
        String data = "{}";
        String verb = "GET";
        String path = "/api/v1/position";

        try {
            WSResponse wsResponse = bitmexService.createRequest(bitmexCredentials, data, verb, path).execute()
                    .toCompletableFuture().get();
            JsonNode response = wsResponse.asJson();
            logger.debug("getPositions response: {}.", wsResponse.getBody());

            if (wsResponse.getStatus() >= 400)
                throw new ServerException("httpError", new Exception(wsResponse.getBody()));

            List<BitmexPosition> positions = new ArrayList<>();
            for (JsonNode positionNode: response) {
                positions.add(new BitmexPosition(positionNode.get("symbol").asText(),
                        positionNode.get("currentQty").asInt()));
            }
            return positions;
        } catch (InterruptedException | ExecutionException | ClientException e) {
            throw new ServerException("executeRequest", e);
        }
    }

    public void makeMarketOrder(BitmexPosition bitmexPosition) throws ServerException {

        BitmexCredentials bitmexCredentials = bitmexRepository.getCredentials();
        String data = "{\"symbol\":\"" + bitmexPosition.getSymbol() + "\"," +
                "\"orderQty\":" + bitmexPosition.getQuantity() + "," +
                "\"ordType\":\"Market\"}";
        String verb = "POST";
        String path = "/api/v1/order";

        try {
            WSResponse wsResponse = bitmexService.createRequest(bitmexCredentials, data, verb, path).execute()
                    .toCompletableFuture().get();
            logger.debug("makeOrder response: {}.", wsResponse.getBody());

            if (wsResponse.getStatus() >= 400)
                throw new ServerException("httpError", new Exception(wsResponse.getBody()));
        } catch (InterruptedException | ExecutionException | ClientException e) {
            throw new ServerException("executeRequest", e);
        }
    }

}
