package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import com.webcheckers.model.Move;
import com.webcheckers.model.Position;

import org.junit.jupiter.api.Test;

public class JsonToMoveConverterTest {
    
    @Test
    public void testValidMove(){

        String moveData = "{\"start\":{\"row\":2,\"cell\":5},\"end\":{\"row\":0,\"cell\":3}}";
        Move knownMove = new Move(new Position(2, 5), new Position(0, 3));
        String moveDataURI = "";

        try{
            moveDataURI = java.net.URLEncoder.encode(moveData, "UTF-8");
        }
        catch(UnsupportedEncodingException ex){
            
        }

        assertEquals(knownMove.toString(), JsonToMoveConverter.convert(moveDataURI).toString());
    }

    @Test
    public void testInvalidMoves(){
        String moveData = "{\"start\":{\"row\":2,\"cell\":5}";
        String moveDataURI = "";

        try{
            moveDataURI = java.net.URLEncoder.encode(moveData, "UTF-8");
        }
        catch(UnsupportedEncodingException ex){}

        assertEquals(null, JsonToMoveConverter.convert(moveDataURI));

        String moveData2 = "{\"start\":{\"row\":2,\"cell\":5},\"end\":{\"row\":null,\"cell\":null}}";
        String moveDataURI2 = "";

        try{
            moveDataURI2 = java.net.URLEncoder.encode(moveData2, "UTF-8");
        }
        catch(UnsupportedEncodingException ex){}

        assertEquals(null, JsonToMoveConverter.convert(moveDataURI2));
    }

    @Test
    public void testInvalidURI(){
        String moveDataURI = "";

        assertEquals(null, JsonToMoveConverter.convert(moveDataURI));
    }

}
