package com.webcheckers.util;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.webcheckers.application.*;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;

/**
 * The JsonToMoveConverter class converts a JSON (opponent move) to a move.
 *
 * @author David Authur Cole
 */
public class JsonToMoveConverter {

    private static final Logger LOG = Logger.getLogger(JsonToMoveConverter.class.getName());

    public static Move convert(String rawUri) {
        //Dedoded data will be formatted in json:
        //actionData={"start":{"row":5,"cell":0},"end":{"row":4,"cell":1}}

        if(WebServer.DEBUG_FLAG) LOG.info("Raw URI: " + rawUri);
        String decodedData;
        decodedData = java.net.URLDecoder.decode(rawUri, StandardCharsets.UTF_8);

        if(decodedData.equals("")){
            return null;
        }
        
        //Remove the "actionData=" from the String, as this causes problems with Json parsing
        decodedData = decodedData.replace("actionData=", "");
        //For some reason the gameID is being passed with the call, not sure why, but we need to remove it from the string
        if(decodedData.contains("gameID=")){
            decodedData = decodedData.substring(decodedData.indexOf("{"));
        }

        if(WebServer.DEBUG_FLAG) LOG.info("Decoded data: " + decodedData);

        JsonElement jsonTree;
        //Parse the data
        try{
            jsonTree = JsonParser.parseString(decodedData).getAsJsonObject();
        }
        catch(JsonSyntaxException ex){
            return null;
        }
        
        //Determine whether or not the json params exist - If they don't, error out immediately
        if(jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("row").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("cell").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("row").equals(JsonNull.INSTANCE) ||
           jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("cell").equals(JsonNull.INSTANCE)){
                return null;
           }
        
        //Get the start and end positions
        int rowNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("row").getAsInt();
        int colNumStart = jsonTree.getAsJsonObject().get("start").getAsJsonObject().get("cell").getAsInt();
        Position start = new Position(rowNumStart, colNumStart);

        if(WebServer.DEBUG_FLAG) LOG.info("Start position: " + rowNumStart + ", " + colNumStart);

        int rowNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("row").getAsInt();
        int colNumEnd = jsonTree.getAsJsonObject().get("end").getAsJsonObject().get("cell").getAsInt();
        Position end = new Position(rowNumEnd, colNumEnd);

        if(WebServer.DEBUG_FLAG) LOG.info("End position: " + rowNumEnd + ", " + colNumEnd);

        return (new Move(start, end));
    }

}
