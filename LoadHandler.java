package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataTransferObjects.LoadRequest;
import DataTransferObjects.LoadResponse;
import Service.LoadService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class LoadHandler implements HttpHandler{
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * handles load requests from the client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("EventHandler","handle");
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoadRequest request = (LoadRequest) new Encoder().decode(exchange.getRequestBody(),LoadRequest.class);
                LoadResponse response = new LoadService().loadService(request); // Create a response from the service
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);  // Create the correct response body
                String result = new Encoder().encode(response);
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();

                success = true;
            }
            if (!success) {
                // Request was invalid, so return a "bad request"
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Send any response body you need to
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        logger.exiting("EventHandler","handle");
    }
}
