package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardView implements Iterable<Row> {

    /**
     * Declaration for rows within the board
     */
    private ArrayList<Row> rows;

    /**
     * Initialize a row given an index and spaces
     * @param rows The rows contained within the board
     *
     */
    public BoardView(ArrayList<Row> rows){
        for(int i = 0; i < 8; i++){
            rows.add(new Row(i, new ArrayList<>()));
        }
        this.rows = rows;
    }

    /**
     * @return An iteration of rows
     */
    @Override
    public Iterator<Row> iterator() { return rows.iterator(); }
}
