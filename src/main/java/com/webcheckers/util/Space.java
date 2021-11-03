package com.webcheckers.util;

public class Space {

    /**
     * Declaration for space index and piece within space
     */
    private int cellIdx;
    private Piece piece;
    private Boolean isValid;

    /**
     * Initialize a row given an index and spaces
     * @param cellIdx The index of the cell
     * @param piece The piece contained within the space
     *
     */
    public Space(int cellIdx, Piece piece){
        this.cellIdx = cellIdx;
        this.piece = piece;
        isValid = true;
    }

    /**
     * @return The index of the cell
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * Change if a space is valid
     */
    public void setValid(boolean valid) {
        isValid = valid;
    }

    /**
     * @return If it is a valid cell to put a piece in
     */
    public boolean isValid(int row) {
        return (row + cellIdx) % 2 == 1;
    }

    /**
     * @return The piece within the cell
     */
    public Piece getPiece(){
        return this.piece;
    }

    /**
     * @param piece The new piece
     */
    public void setPiece(Piece piece) { this.piece = piece; }
}
