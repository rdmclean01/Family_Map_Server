package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.AuthTokenDAO;
import DataAccessObjects.DataBaseException;
import DataTransferObjects.RegisterRequest;
import DataTransferObjects.RegisterResponse;
import Service.RegisterService;

/**
 * Handles the register Service
 */

public class RegisterHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    /**
     * handles register requests from the client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        logger.entering("RegisterHandler","handle");
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                // Create a register request object from the request Body
                Encoder conversion = new Encoder();
                RegisterRequest request = (RegisterRequest) conversion.decode(exchange.getRequestBody(),RegisterRequest.class);

                // Send the request into the service class
                RegisterService service = new RegisterService();
                RegisterResponse response = service.registerService(request);

                // Take the response and write it to the response Body
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String result = conversion.encode(response);
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
            logger.log(Level.SEVERE,e.getMessage(),e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        logger.exiting("RegisterHandler","handle");
    }

}
