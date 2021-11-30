package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;

import com.webcheckers.model.BoardView;

import org.junit.jupiter.api.Test;

public class BoardViewTest {
    
    BoardView board= new BoardView(new ArrayList<>());

    @Test
    public void testInitialize(){
        assertEquals(Arrays.toString(new BoardView(new ArrayList<>()).getRows().toArray()), Arrays.toString(board.getRows().toArray()));
    }

    @Test
    public void testCopyConstructor(){
        BoardView copy = new BoardView(board);
        assertEquals(Arrays.toString(board.getRows().toArray()), Arrays.toString(copy.getRows().toArray()));
    }

    @Test
    public void testGetRow(){
        assertEquals(board.getRow(0), board.getRows().get(0));
    }
    
    @Test
    public void testBoardIterator(){
        assertSame(board.iterator().next(), board.getRows().iterator().next());
    }

}
