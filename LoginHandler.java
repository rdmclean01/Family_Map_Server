package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataTransferObjects.LoginRequest;
import DataTransferObjects.LoginResponse;
import Service.LoginService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class LoginHandler implements HttpHandler{
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * handles Login requests from the client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("Login Handler","handle");
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoginRequest request = (LoginRequest) new Encoder().decode(exchange.getRequestBody(),LoginRequest.class);
                LoginResponse response = new LoginService().loginService(request); // Create a response from the service
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
        logger.exiting("LoginHandler","handle");
    }
}
