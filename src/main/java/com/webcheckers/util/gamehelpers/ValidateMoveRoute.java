package com.webcheckers.util.gamehelpers;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Game;
import com.webcheckers.util.JsonToMoveConverter;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Piece;
import com.webcheckers.util.Player;
import com.webcheckers.util.Position;
import com.webcheckers.util.Row;
import com.webcheckers.util.Space;

/**
 * The UI Controller to validate a move.
 *
 * @author David Authur Cole
 */
public class ValidateMoveRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(ValidateMoveRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public ValidateMoveRoute() {
        LOG.config("ValidateMoveRoute is initialized.");
    }

    /**
     * Render the updated WebCheckers game page by validating a move
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
        if(WebServer.DEBUG_FLAG) LOG.info("ValidateMoveRoute is invoked.");

        Message responseMessage;

        //Get the data from the request
        String rawUri = request.body();

        //Parse the data from JSON to a Move object
        Move combinedMove = JsonToMoveConverter.convert(rawUri);
        if(combinedMove == null) { 
            return gson.toJson(Message.error("Invalid move."));
        }

        //Get the individual move aspects from the combined move
        int rowNumStart = combinedMove.getStart().getRow();
        int colNumStart = combinedMove.getStart().getCell();
        int rowNumEnd = combinedMove.getEnd().getRow();
        int colNumEnd = combinedMove.getEnd().getCell();

        if(WebServer.DEBUG_FLAG) {
            LOG.info("Start Position: " + rowNumStart + ", " + colNumStart);
            LOG.info("End position: " + rowNumEnd + ", " + colNumEnd);
        }

        //Calculate the difference between the start and end positions
        int changeX = rowNumEnd - rowNumStart;
        int changeY = colNumEnd - colNumStart;

        if(WebServer.DEBUG_FLAG) LOG.info("Change in X: " + changeX + ", Change in Y: " + changeY);

        //Get the Player object from the request, and determine the Game object
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        if(WebServer.DEBUG_FLAG) LOG.info("CurrentUser from AJax call: " + player.toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //Get the current player's Piece
        Row startRow = gameBoard.getBoard().getRow(rowNumStart);
        Space startSpace = startRow.getSpace(colNumStart);
        Piece startPiece = startSpace.getPiece();

        //Generate a response message based on the validity of the move
        responseMessage = gameBoard.isMoveValid(combinedMove, startPiece);
        if(responseMessage.getType() == Message.Type.INFO) {
            gameBoard.executeMove(new Move(new Position(rowNumStart, colNumStart), new Position(rowNumEnd, colNumEnd)));
        }
        return gson.toJson(responseMessage);
    }
    
}
