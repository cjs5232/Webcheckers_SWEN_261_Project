package com.webcheckers.util;

public class Piece {
    /**
     * Enumeration for piece Type
     */
    public enum Type {
        SINGLE,
        KING,
    }
    /**
     * Enumueration for piece Color
     */
    public enum Color {
        RED,
        WHITE
    }

    private Type type;
    private Color color;

    /**
     * Initialize a piece given a {@code Color} and {@code Type}
     * @param type The type of the piece
     * @param color The color of the piece
     * 
     * @see Type
     * @see Color
     */
    public Piece (Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    /**
     * @return {@code Type} of piece
     * 
     * @see Type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @return {@code Color} of piece
     * 
     * @see Color
     */
    public Color getColor() {
        return this.color;
    }
}
