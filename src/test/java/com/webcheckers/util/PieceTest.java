package com.webcheckers.util;

import com.webcheckers.util.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PieceTest {
    Piece piece_single_red = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
    Piece piece_king_red = new Piece(Piece.Type.KING, Piece.Color.RED);
    Piece piece_single_white = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);
    Piece piece_king_white = new Piece(Piece.Type.KING, Piece.Color.WHITE);

    @Test
    void TestPieceType() {
        assertTrue(piece_single_red.getType() == Piece.Type.SINGLE);
        assertTrue(piece_king_red.getType() == Piece.Type.KING);
        // assertTrue(piece_single_white.getType() == Piece.Type.SINGLE);
        // assertTrue(piece_king_white.getType() == Piece.Type.KING);
    }

    @Test
    void TestPieceColor() {
        assertTrue(piece_single_red.getColor() == Piece.Color.RED);
        // assertTrue(piece_king_red.getColor() == Piece.Color.RED);
        assertTrue(piece_single_white.getColor() == Piece.Color.WHITE);
        // assertTrue(piece_king_white.getColor() == Piece.Color.WHITE);
    }

    @Test
    void TestPieceToString() {
        assertTrue(piece_single_red.toString().equals("R"));
        // assertTrue(piece_king_red.toString().equals("R"));
        assertTrue(piece_single_white.toString().equals("W"));
        // assertTrue(piece_king_white.toString().equals("W"));
    }

    @Test
    void TestPieceSetColor() {
        piece_king_red.setColor(Piece.Color.WHITE);
        assertTrue(piece_king_red.getColor() == Piece.Color.WHITE);
        piece_king_red.setColor(Piece.Color.RED);
        assertTrue(piece_king_red.getColor() == Piece.Color.RED);
    }

    @Test
    void TestPieceSetType() {
        piece_king_red.setType(Piece.Type.SINGLE);
        assertTrue(piece_king_red.getType() == Piece.Type.SINGLE);
        piece_king_red.setType(Piece.Type.KING);
        assertTrue(piece_king_red.getType() == Piece.Type.KING);
    }
}
