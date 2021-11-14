package com.webcheckers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PieceTest {
    Piece piece_single_red = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
    Piece piece_king_red = new Piece(Piece.Type.KING, Piece.Color.RED);
    Piece piece_single_white = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);
    Piece piece_king_white = new Piece(Piece.Type.KING, Piece.Color.WHITE);

    @Test
    void TestPieceType() {
        assertSame(Piece.Type.SINGLE, piece_single_red.getType());
        assertSame(Piece.Type.KING, piece_king_red.getType());
        // assertTrue(piece_single_white.getType() == Piece.Type.SINGLE);
        // assertTrue(piece_king_white.getType() == Piece.Type.KING);
    }

    @Test
    void TestPieceColor() {
        assertSame(Piece.Color.RED, piece_single_red.getColor());
        // assertTrue(piece_king_red.getColor() == Piece.Color.RED);
        assertSame(Piece.Color.WHITE, piece_single_white.getColor());
        // assertTrue(piece_king_white.getColor() == Piece.Color.WHITE);
    }

    @Test
    void TestPieceToString() {
        assertEquals("R", piece_single_red.toString());
        // assertTrue(piece_king_red.toString().equals("R"));
        assertEquals("W", piece_single_white.toString());
        // assertTrue(piece_king_white.toString().equals("W"));
    }

    @Test
    void TestPieceSetColor() {
        piece_king_red.setColor(Piece.Color.WHITE);
        assertSame(Piece.Color.WHITE, piece_king_red.getColor());
        piece_king_red.setColor(Piece.Color.RED);
        assertSame(Piece.Color.RED, piece_king_red.getColor());
    }

    @Test
    void TestPieceSetType() {
        piece_king_red.setType(Piece.Type.SINGLE);
        assertSame(Piece.Type.SINGLE, piece_king_red.getType());
        piece_king_red.setType(Piece.Type.KING);
        assertSame(Piece.Type.KING, piece_king_red.getType());
    }
}
