package com.webcheckers.util;

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
        }
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
