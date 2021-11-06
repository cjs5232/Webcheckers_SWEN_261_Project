package com.webcheckers.util.gamehelpers;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class SubmitTurnRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(SubmitTurnRoute.class.getName());

    /**
     * Flag for turning on verbose deubgging/console logging
     */
    private final boolean verboseDebug = true;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public SubmitTurnRoute() {
        LOG.config("SubmitTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.info("SubmitTurnRoute is invoked.");

        //Get the Player object from the request, and determine the Game object
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        if(verboseDebug) LOG.info("CurrentUser from AJax call: " + player.toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);
        
        if(gameBoard.getPendingMove() == null) {
            LOG.warning("No pending move to submit for player: " + player.toString());
            return(gson.toJson(Message.error("No pending move to submit.")));
        } 
        else{
            LOG.info("Submitting turn for player: " + player.toString());
            boolean success = gameBoard.validateAndMove(gameBoard.getPendingMove(), player);
            request.session().attribute("currentUser", player.toString());
            return(success ? gson.toJson(Message.info("Move submitted successfully.")) : gson.toJson(Message.error("Pending move was not valid.")));
        }
    }
    
}
