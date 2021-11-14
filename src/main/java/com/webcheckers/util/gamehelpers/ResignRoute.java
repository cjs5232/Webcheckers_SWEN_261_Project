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
public class ResignRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(ResignRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public ResignRoute() {
        LOG.config("ResignRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        if(WebServer.DEBUG_FLAG) LOG.config("ResignRoute is invoked.");
        
        //Get the player object and their game
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //This should never happen, but just in case
        if(refGame.isOver()){
            return(gson.toJson(Message.error("Cannot resign. Game is over.")));
        }
        else{
            refGame.resign(player);
            return(gson.toJson(Message.info("You have resigned.")));
        }
    }
    
}
