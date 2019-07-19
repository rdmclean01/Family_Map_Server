package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import Handler.ClearHandler;
import Handler.EventHandler;
import Handler.FillHandler;
import Handler.LoadHandler;
import Handler.LoginHandler;
import Handler.PersonHandler;
import Handler.RegisterHandler;
import Handler.DefaultHandler;

/**
 * Opens the server and begins the running procedure
 */


public class Server {
    //The maximum number of waiting connections to queue
    private static final int MAX_WAITING_CONNECTIONS = 12;

    private static Logger logger;

    static {
        try{
            initLog();
        }catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
        }
    }

    private static void initLog() throws IOException {
        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("familymap");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("log.txt", false);
        fileHandler.setLevel(logLevel);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }
    private HttpServer server;

    /**
     * Initialize and run the server
     * @param portNumber    Port number the server will listen on
     */
    private void run(String portNumber){

        logger.info("Initializing HTTP Server");
        try {
            //Create a new HTTP Server object
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            return;
        }

        //Use the default executor
        server.setExecutor(null);

        //Message that the server is creating and installing its HTTP handlers
        logger.info("Creating contexts");

        //User Commands
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/event", new EventHandler());     // Handles both Event services
        server.createContext("/person", new PersonHandler());   // Handles both Person services

        // Database Commands
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());

        // Default to handle the HTML test page
        server.createContext("/", new DefaultHandler());

        logger.info("Starting server");
        server.start();
    }

    public static void main(String[] args){
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
