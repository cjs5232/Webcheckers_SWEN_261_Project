package com.webcheckers.model;

/**
 * The position class handles positions on a checkers board.
 *
 * @author Connor James Stange
 */
public class Position {

    /**
     * Declaration for row index and cell (column) index of a position
     */
    private int row; // { 0 to 7 }
    private int cell; // { 0 to 7 }

    /**
     * Initialize a position given an index for the row and the cell (column)
     * @param row The index of the row
     * @param cell The index of the cell
     *
     */
    public Position(int row, int cell){
        if(row >= 0 && row < 8 && cell >=0 && cell < 8) {
            this.row = row;
            this.cell = cell;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void inverseForWhite(){
        this.row = 7 - this.row;
        this.cell = 7 - this.cell;
    }

    @Override
    public String toString(){
        return "(" + row + ", " + cell + ")";
    }

    /**
     * Test if two positions are equivalent
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Position){
            Position p = (Position) o;
            return this.row == p.row && this.cell == p.cell;
        }
        return false;
    }

    /**
     * @return The row index of this position
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The cell (column) index of this position
     */
    public int getCell() {
        return cell;
    }
}
