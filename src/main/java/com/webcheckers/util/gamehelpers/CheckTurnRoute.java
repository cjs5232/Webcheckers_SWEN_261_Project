package com.webcheckers.util.gamehelpers;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Game;
import com.webcheckers.util.Player;
import com.webcheckers.util.Piece.Color;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class CheckTurnRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(CheckTurnRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public CheckTurnRoute() {
        LOG.config("CheckTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        //Invoking not logged due to console spamming
        
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        Player[] players = gameBoard.getPlayers();
        Player redPlayer = players[0];
        Player whitePlayer = players[1];

        return gson.toJson((player == redPlayer && gameBoard.getActiveColor() == Color.RED) || (player == whitePlayer && gameBoard.getActiveColor() == Color.WHITE));
    }
    
}
