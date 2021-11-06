package com.webcheckers.util.gamehelpers;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Piece;
import com.webcheckers.util.Player;
import com.webcheckers.util.Position;
import com.webcheckers.util.Row;
import com.webcheckers.util.Space;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class ValidateMoveRoute implements Route {

    //Create a Gson object to convert the Message object to a JSON string
    private final Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(ValidateMoveRoute.class.getName());

    /**
     * Flag for turning on verbose deubgging/console logging
     */
    private final boolean verboseDebug = false;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public ValidateMoveRoute() {
        LOG.config("ValidateMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.info("ValidateMoveRoute is invoked.");

        //Get the data from the request
        String rawUri = request.body();

        //Dedoded data will be formatted in json:
        //actionData={"start":{"row":5,"cell":0},"end":{"row":4,"cell":1}}
        String decodedData = "";
        try{
            decodedData = java.net.URLDecoder.decode(rawUri, "UTF-8");
            //Remove the "actionData=" from the String, as this causes problems with Json parsing
            decodedData = decodedData.replace("actionData=", "");

            if(verboseDebug) LOG.info("Decoded data: " + decodedData);
            
        }
        catch (UnsupportedEncodingException ex){
            LOG.warning("UnsupportedEncodingException: " + ex.getMessage());
        }

        //Create a JsonReader to read the decoded data
        JsonReader reader = new JsonReader(new java.io.StringReader(decodedData));
        reader.setLenient(true);

        //Create a JsonParser to parse the read data
        JsonParser parser = new JsonParser();

        //Parse the data
        JsonElement jsonTree = parser.parse(reader).getAsJsonObject();

        //Determine whether or not the json params exist - If they don't, error out immediately
        if(jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("row").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("cell").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("row").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("cell").equals(JsonNull.INSTANCE)){
                return gson.toJson(Message.error("Invalid move"));
           }

        //Get the start and end positions
        int rowNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("row").getAsInt();
        int colNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("cell").getAsInt();
        Position start = new Position(rowNumStart, colNumStart);

        if(verboseDebug) LOG.info("Start position: " + rowNumStart + ", " + colNumStart);

        int rowNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("row").getAsInt();
        int colNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("cell").getAsInt();
        Position end = new Position(rowNumEnd, colNumEnd);

        Move combinedMove = new Move(start, end);

        if(verboseDebug) LOG.info("End position: " + rowNumEnd + ", " + colNumEnd);

        //Calculate the difference between the start and end positions
        int changeX = rowNumEnd - rowNumStart;
        int changeY = colNumEnd - colNumStart;

        if(verboseDebug) LOG.info("Change in X: " + changeX + ", Change in Y: " + changeY);

        //Get the Player object from the request, and determine the Game object
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        if(verboseDebug) LOG.info("CurrentUser from AJax call: " + player.toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //Get the current player's Piece
        Row startRow = gameBoard.getBoard().getRow(rowNumStart);
        Space startSpace = startRow.getSpace(colNumStart);
        Piece startPiece = startSpace.getPiece();

        Message responseMessage;

        if(gameBoard.isMoveValid(combinedMove, startPiece)){
            gameBoard.setPendingMove(new Move(new Position(rowNumStart, colNumStart), new Position(rowNumEnd, colNumEnd)));
            responseMessage = Message.info("OK");
        } 
        else responseMessage = Message.error("Invalid move");

        return gson.toJson(responseMessage);
    }
    
}
