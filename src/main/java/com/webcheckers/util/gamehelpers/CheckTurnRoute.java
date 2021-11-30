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
     */
    public CheckTurnRoute() {
        LOG.config("CheckTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        //Invoking not logged due to console spamming
        
        //Get the player object and their game
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());

        Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        if(player.isSpectating()){
            //Get the spectating game
            refGame = player.getSpectatingGame();

            //If the game is over, return the winner as a message
            if(refGame.isOver()){
                return gson.toJson(refGame.getGameOverMessage()); 
            }

            //Otherwise, return a boolean value on whether the turn has changed or not
            return player.getLastKnownTurnColor() == refGame.getActiveColor() ? gson.toJson(Message.info("false")) : gson.toJson(Message.info("true"));
        }

        //Get the list of players in the game
        Player[] players = refGame.getPlayers();
        Player redPlayer = players[0];
        Player whitePlayer = players[1];

        //Boolean logic to determine if it is the player's turn
        return gson.toJson(Message.info(Boolean.toString((player == redPlayer && refGame.getActiveColor() == Color.RED) || (player == whitePlayer && refGame.getActiveColor() == Color.WHITE) || refGame.isOver())));
    }
    
}
