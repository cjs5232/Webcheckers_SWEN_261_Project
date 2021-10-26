package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardView implements Iterable<Row> {

    /**
     * Declaration for rows within the board
     */
    private ArrayList<Row> rows;

    /**
     * The amount of row to generate.
     * - Davis Pitts (dep2550)
     */
    private static final int ROW_COUNT = 8;

    /**
     * Initialize a row given an index and spaces
     * @param rows The rows contained within the board
     *
     */
    public BoardView(ArrayList<Row> rows){
        for(int i = 0; i < ROW_COUNT; i++){
            rows.add(new Row(i, new ArrayList<>()));
        }
        this.rows = rows;
    }

    /**
     * @param index The index of a row
     *
     * @return The row at a given index
     */
    public Row getRow(int index){ return rows.get(index); }

    /**
     * @return An iteration of rows
     */
    @Override
    public Iterator<Row> iterator() { return rows.iterator(); }
}
