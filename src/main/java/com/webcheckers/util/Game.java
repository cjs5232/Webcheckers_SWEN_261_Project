package com.webcheckers.util;

import java.util.ArrayList;

public class Game {
   
    private Player p1;
    private Player p2;

    private BoardView gameBoard;

    public Game(Player p1, Player p2){

        this.p1 = p1;
        this.p2 = p2;

        this.gameBoard = new BoardView(new ArrayList<>());

    }

    /** 
     * @return the board object of the game
     */
    public BoardView getBoard(){
        return this.gameBoard;
    }

    /**
     * @return the two Players playing the game
     */
    public Player[] getPlayers(){
        return new Player[]{p1, p2};
    }

}
