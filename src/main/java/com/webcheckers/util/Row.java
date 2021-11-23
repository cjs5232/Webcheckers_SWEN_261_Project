package com.webcheckers.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The row class handles checkers rows.
 *
 * @author Connor James Stange
 */
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
    private List<Space> spaces;

    /**
     * Initialize a row given an index and spaces
     * @param index The index of the row
     * @param spaces The spaces contained within the row
     *
     */
    public Row(int index, List<Space> spaces){
        this.index = index;
        for(int i = 0; i < SPACE_COUNT; i++){
            spaces.add(new Space(i, null));
        }
        
        this.spaces = spaces;
    }

    /**
     * @param index The index of a space
     *
     * @return The space at a given index
     */
    public Space getSpace(int index){ return spaces.get(index); }

    public List<Space> getSpaces(){ return spaces; }

    /**
     * @return The index of the row
     */
    public int getIndex(){
        return this.index;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for(Space space : spaces){
            sb.append(space).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return An iteration of spaces
     */
    @Override
    public Iterator<Space> iterator() { return spaces.iterator(); }
}
