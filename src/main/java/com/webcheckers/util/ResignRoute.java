package com.webcheckers.util;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.*;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

/**
 * The UI Controller to resign the game.
 *
 * @author David Authur Cole
 */
public class ResignRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(ResignRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     */
    public ResignRoute() {
        LOG.config("ResignRoute is initialized.");
    }

    /**
     * Render the updated WebCheckers game page by resigning the game
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        if(WebServer.DEBUG_FLAG) LOG.info("ResignRoute is invoked.");
        
        //Get the player object and their game
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //This should never happen, but just in case
        if(refGame.isGameOver()){
            return(gson.toJson(Message.error("Cannot resign. Game is over.")));
        }
        else{
            refGame.resign(player);
            return(gson.toJson(Message.info("You have resigned.")));
        }
    }
    
}
