package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.webcheckers.model.Move;
import com.webcheckers.model.Position;

import org.junit.jupiter.api.Test;

public class MoveTest {
  
    Position start = new Position(0, 0);
    Position end = new Position(1, 1);

    @Test
    public void initMove(){

        Move move = new Move(start, end);
        assertEquals(start, move.getStart());
        assertEquals(end, move.getEnd());
        assertEquals(2, move.getDistance());
        assertEquals("(0, 0) -> (1, 1)", move.toString());
    }

}
