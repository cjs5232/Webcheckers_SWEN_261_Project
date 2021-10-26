package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;

public class Row implements Iterable<Space>{

    /**
     * The amount of spaces in a row.
     * - Davis Pitts (dep2550)
     */
    private static final int SPACE_COUNT = 8;

    /**
     * Declaration for row index and spaces within the row
     */
    private int index;
    private ArrayList<Space> spaces;

    /**
     * Initialize a row given an index and spaces
     * @param index The index of the row
     * @param spaces The spaces contained within the row
     *
     */
    public Row(int index, ArrayList<Space> spaces){
        this.index = index;
        for(int i = 0; i < SPACE_COUNT; i++){
            if(index % 2 == i % 2 )
                spaces.add(new Space(i, null));
            else if(index == 3 || index == 4)
                spaces.add(new Space(i, null));
            else if(index < 3)
                spaces.add(new Space(i, new Piece(Piece.Type.SINGLE, Piece.Color.WHITE)));
            else
                spaces.add(new Space(i, new Piece(Piece.Type.SINGLE, Piece.Color.RED)));
        }
        this.spaces = spaces;
    }

    /**
     * @return The index of the row
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * @return An iteration of spaces
     */
    @Override
    public Iterator<Space> iterator() { return spaces.iterator(); }
}
