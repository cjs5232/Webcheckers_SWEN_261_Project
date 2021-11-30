package com.webcheckers.model;

/**
 * The move class handles checkers moves.
 *
 * @author Connor James Stange
 */
public class Move {

    /**
     * Declaration for the starting and ending positions of a move
     */
    private Position start;
    private Position end;

    private int distance;

    /**
     * Initialize a move given a starting position and an ending position
     * @param start The position of the piece before the move
     * @param end The position of the piece after the move
     *
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
        this.distance = Math.abs(start.getRow() - end.getRow()) + Math.abs(start.getCell() - end.getCell());
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

    public int getDistance() {
        return distance;
    }

    public void inverseForWhite(){
        this.start.inverseForWhite();
        this.end.inverseForWhite();
    }

    @Override
    public String toString(){
        return(start.toString() + " -> " + end.toString());
    }
}
