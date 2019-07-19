package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataTransferObjects.EventRequest;
import DataTransferObjects.EventResponse;
import Service.EventService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class EventHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * Handles requests given by the client
     *
     * @param exchange object with all the stuff
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("EventHandler", "handle");
        boolean success = false;
        String authToken = null;
        String eventID = null;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();          // Get HTTP request headers
                if (reqHeaders.containsKey("Authorization")) {          // Check for the authorization header
                    authToken = reqHeaders.getFirst("Authorization");   // Extract the value
                }

                String path = exchange.getRequestURI().getPath();           // Extract the URL
                String[] parts = path.split("\\/");                     // Split the path up according to the '/' character
                if (parts.length > 2) {                                    // Extract eventID if exists
                    eventID = parts[2];
                }

                EventRequest request = new EventRequest();
                request.setAuthToken(authToken);
                request.setEventID(eventID);

                EventResponse response;
                if (eventID != null) {
                    response = new EventService().eventServiceWithID(request);
                } else {
                    response = new EventService().eventService(request);
                }

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // Fill in the response body
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

        logger.exiting("EventHandler", "handle");
    }
}

