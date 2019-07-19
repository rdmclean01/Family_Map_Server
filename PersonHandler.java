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
import DataTransferObjects.PersonRequest;
import DataTransferObjects.PersonResponse;
import Service.PersonService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class PersonHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * handles Person requests from the client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("PersonHandler", "handle");
        String authToken = null;
        String personId = null;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();          // Get HTTP request headers
                if (reqHeaders.containsKey("Authorization")) {          // Check for the authorization header
                    authToken = reqHeaders.getFirst("Authorization");   // Extract the value
                }

                String path = exchange.getRequestURI().getPath();          // Extract the URL
                String[] parts = path.split("\\/");                     // Split the path according to the '/' character
                if (parts.length > 2) {                                    // Extract personId if exists
                    personId = parts[2];
                }

                PersonRequest request = new PersonRequest();               // Create and set up the request
                request.setAuthToken(authToken);
                request.setPersonID(personId);

                PersonResponse response;                                    // Create and set up the response
                if (personId != null) {
                    response = new PersonService().singlePerson(request);   // Service method to return a single person
                } else {
                    response = new PersonService().userAncestors(request);  // Service method to return all ancestors of the user
                }

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // Fill in the response body
                String result = new Encoder().encode(response);
                exchange.getResponseBody().write(result.getBytes());
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
        logger.exiting("PersonHandler", "handle");
    }
}
