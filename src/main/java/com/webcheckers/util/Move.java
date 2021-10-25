package com.webcheckers.util;

public class Move {

    /**
     * Declaration for the starting and ending positions of a move
     */
    private Position start;
    private Position end;

    /**
     * Initialize a move given a starting position and an ending position
     * @param start The position of the piece before the move
     * @param end The position of the piece after the move
     *
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }

    /**
     * @return The starting position of the move
     */
    public Position getStart() {
        return start;
    }

    /**
     * @return The ending position of the move
     */
    public Position getEnd() {
        return end;
    }
}
