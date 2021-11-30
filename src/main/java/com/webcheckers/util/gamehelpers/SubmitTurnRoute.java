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
import com.webcheckers.util.Position;

/**
 * The UI Controller to submit a turn.
 *
 * @author David Authur Cole
 */
public class SubmitTurnRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(SubmitTurnRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
<<<<<<< HEAD
     *
=======
>>>>>>> d01c9e9badf12cbc373c370509b6936c7e68935d
     */
    public SubmitTurnRoute() {
        LOG.config("SubmitTurnRoute is initialized.");
    }

    /**
     * Render the updated WebCheckers game page by submitting a turn
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
        if(WebServer.DEBUG_FLAG) LOG.info("SubmitTurnRoute is invoked.");

        //Get the Player object from the request, and determine the Game object
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        if(WebServer.DEBUG_FLAG) LOG.info("CurrentUser from AJax call: " + player.toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        Position lastMoveEndPos = gameBoard.getLastMoveEndPosition();

        if(WebServer.DEBUG_FLAG) LOG.info("LastMoveEndPos: " + lastMoveEndPos.toString());

        //See WaitingForTurnValidationState.js for why this should not always be true
        boolean hasMoreMoves = gameBoard.positionHasOtherMoves(lastMoveEndPos); 

        if(WebServer.DEBUG_FLAG) LOG.info("HasMoreMoves: " + hasMoreMoves);

        if(!hasMoreMoves){
            //Remove all captured pieces from the board
            gameBoard.removeDeadPieces();
            //End the player's turn
            gameBoard.swapActiveColor();
        }

        return(hasMoreMoves ? gson.toJson(Message.error("You have more moves to make.")) : gson.toJson(Message.info("Move submitted successfully.")));
    }
    
}
