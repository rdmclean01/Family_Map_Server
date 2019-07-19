package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataAccessObjects.DataBaseException;
import DataTransferObjects.ClearResult;
import Service.ClearService;

/**
 * Created by rdmcl on 2/8/2018.
 */

public class ClearHandler implements HttpHandler{


    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException{
        logger.entering("ClearHandler","handle");

        boolean success = false;
        try {
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                //Do I need the headers here?

                // There is not request, so just make a result
                ClearResult response = new ClearService().clearService();

                // Write the good response back to the http exchange object
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
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
            logger.log(Level.SEVERE,e.getMessage(),e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            exchange.getResponseBody().close();
        } catch (DataBaseException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }


        logger.exiting("ClearHandler","handle");

    }
}
