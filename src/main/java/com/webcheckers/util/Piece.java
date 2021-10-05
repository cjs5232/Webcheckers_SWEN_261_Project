package com.webcheckers.util;

public class Piece {
    public enum Type {
        SINGLE,
        KING,
    }
    public enum Color {
        RED,
        WHITE
    }

    public Type type;
    public Color color;

    public Piece (Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
