package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoardView implements Iterable<Row> {

    /**
     * Declaration for rows within the board
     */
    private List<Row> rows = new ArrayList<>();

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
    public BoardView(List<Row> rows){
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

    public String printBoardPretty(){
        StringBuilder sb = new StringBuilder();
        //Column numbers
        sb.append("\n    0 1 2 3 4 5 6 7\n");
        for(Row row : rows){
            sb.append(rows.indexOf(row) + " " + row.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * @return An iteration of rows
     */
    @Override
    public Iterator<Row> iterator() { return rows.iterator(); }
}
