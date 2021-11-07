package com.webcheckers.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.webcheckers.ui.WebServer;

public class JsonToMoveConverter {

    private static final Logger LOG = Logger.getLogger(JsonToMoveConverter.class.getName());

    public static Move convert(String rawUri) {
        //Dedoded data will be formatted in json:
        //actionData={"start":{"row":5,"cell":0},"end":{"row":4,"cell":1}}
        String decodedData = "";
        try{
            decodedData = java.net.URLDecoder.decode(rawUri, "UTF-8");
            //Remove the "actionData=" from the String, as this causes problems with Json parsing
            decodedData = decodedData.replace("actionData=", "");

            if(WebServer.DEBUG_FLAG) LOG.info("Decoded data: " + decodedData);
            
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

        return (new Move(start, end));
    }

}
