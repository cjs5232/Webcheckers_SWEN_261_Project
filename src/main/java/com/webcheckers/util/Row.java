package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;

public class Row implements Iterable<Space>{

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
