package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rdmcl on 2/22/2018.
 */

public class DefaultHandler implements HttpHandler {
    private static Logger logger;

    static {
        logger = Logger.getLogger("familymap");
    }

    private String DIRECTORY = "web";

    /**
     * Handles the test Web API page for debugging before android client
     * @param exchange stuffs
     * @throws IOException #failure
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.entering("DefaultHandler", "handle");
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/")) {
                    path = "/index.html";
                }
                path = DIRECTORY + path;

                File file = new File(path);
                if (file.exists() && file.canRead()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respbody = exchange.getResponseBody();
                    Path filePath = FileSystems.getDefault().getPath(path);
                    Files.copy(filePath,exchange.getResponseBody());
                    respbody.close();
                    success = true;
                }
            }
            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
        }
        logger.exiting("DefaultHandler","handle");
    }
}

