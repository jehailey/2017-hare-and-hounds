package com.oose2017.jlee562.hareandhounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.*;
import java.util.HashMap;


public class Main {

    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8080;

    private static final Logger logger = LoggerFactory.getLogger( Main.class);

    public static void main(String[] args) throws Exception {

        //Specify the IP address and Port at which the server should be run
        ipAddress(IP_ADDRESS);
        port(PORT);

        //Specify the sub-directory from which to serve static resources (like html and css)
        staticFileLocation("/public");

        //Create the model instance and then configure and start the web service
        try {
            HashMap< String, Game> games = new HashMap<>();
            GameService model = new GameService(games);
            new GameController(model);
        } catch (GameService.GameServiceException ex) {
            logger.error("Failed to create a GameService instance. Aborting");
        }
    }

}
