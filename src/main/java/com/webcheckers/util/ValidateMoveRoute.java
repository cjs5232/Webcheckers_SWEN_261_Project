package com.webcheckers.util;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Piece.Color;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class ValidateMoveRoute implements Route {

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

            LOG.info("Decoded data: " + decodedData);
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

        //Get the start and end positions
        int rowNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("row").getAsInt();
        int colNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("cell").getAsInt();

        LOG.info("Start position: " + rowNumStart + ", " + colNumStart);

        int rowNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("row").getAsInt();
        int colNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("cell").getAsInt();

        LOG.info("End position: " + rowNumEnd + ", " + colNumEnd);

        //Calculate the difference between the start and end positions
        int changeX = rowNumEnd - rowNumStart;
        int changeY = colNumEnd - colNumStart;

        //Get the Player object from the request, and determine the Game object
        Player player = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(request.session().attribute("currentUser").toString());
        LOG.info("CurrentUser from AJax call: " + player.toString());
        Game gameBoard = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(player);

        //Get the current player's Piece
        Row startRow = gameBoard.getBoard().getRow(rowNumStart);
        Space startSpace = startRow.getSpace(colNumStart);
        Piece startPiece = startSpace.getPiece();
        Color pieceColor = startPiece.getColor();

        //Determine inversion ratio
        int inversion = pieceColor == Piece.Color.RED ? 1 : -1;

        if( (changeX == 1 || changeX == -1 )  && ( (changeY == -1*inversion) || ( (changeY == 1*inversion) && (startPiece.getType() == Piece.Type.KING) ) )) response.body("{\"message\":{\"type\":\"OK\"}}");
        else response.body("{\"message\":{\"type\":\"ERROR\"}}");

        return response;
    }
    
}
