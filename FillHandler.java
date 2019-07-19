package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataTransferObjects.FillRequest;
import DataTransferObjects.FillResponse;
import Service.FillService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class FillHandler implements HttpHandler {
    private static Logger logger;
    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * handles Fill requests from the client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("FillHandler", "handle");
        boolean success = false;
        String username = null;
        String generations = null;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String path = exchange.getRequestURI().getPath();               // Extract the URL
                String[] parts = path.split("\\/");                         // Split the path up according to the '/' character
                assert path.substring(0,1 ) == "/";
                username = parts[2];    // Extract the username
                if(parts.length > 3){                                           // Extract the generations if exists
                    generations = parts[3];
                }
                else{                                                           // Default is 4 if user does not specify
                    generations = "4";
                }

                FillRequest request = new FillRequest(username, generations);   // Create and populate the request
                FillResponse response = new FillService().fillService(request); // Create a response from the service
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
            e.printStackTrace();
        }
        logger.exiting("FillHandler", "handle");
    }
}
