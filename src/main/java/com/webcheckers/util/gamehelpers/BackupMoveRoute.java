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
 * The UI Controller to back up a move.
 *
 * @author David Authur Cole
 */
public class BackupMoveRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(BackupMoveRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     */
    public BackupMoveRoute() {
        LOG.config("BackupMoveRoute is initialized.");
    }

    /**
     * Render the updated WebCheckers game page by backing up a move.
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
        if(WebServer.DEBUG_FLAG) LOG.info("BackupMoveRoute is invoked.");
        
        //Get the player object, and their game
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //Undo the last move, will never error out
        gameBoard.undoLastMove();
        return gson.toJson(Message.info("Move has been undone."));
    }
    
}
