package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.webcheckers.ui.WebServer;

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

        if(WebServer.EASY_TEST_MODE){
            rows.get(3).getSpace(2).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));
            rows.get(5).getSpace(2).setPiece(new Piece(Piece.Type.SINGLE, Piece.Color.RED));
        }

        this.rows = rows;
    }

    /**
     * Copy constructor
     */
    public BoardView(BoardView template){
        this.rows = new ArrayList<>(template.getRows());
    }

    /**
     * @param index The index of a row
     *
     * @return The row at a given index
     */
    public Row getRow(int index){ return rows.get(index); }

    /**
     * @return The List of rows that represent the board
     */
    public List<Row> getRows(){ return rows; }

    /**
     * @return A visually pleasing string representation of the board as text, may be useful for JUnit testing
     */
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
