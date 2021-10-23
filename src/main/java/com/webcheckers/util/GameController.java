package com.webcheckers.util;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    
    //Store list of all active games
    List<Game> gameList;

    public GameController(){
        gameList = new ArrayList<>();
    }

    /**
     * @param g the new game to be tracked by the controller
     */
    public void addGame(Game g){
        this.gameList.add(g);
    }

    /**
     * @param g the game to remove (no longer active)
     */
    public void removeGame(Game g){
        this.gameList.remove(g);
    }

    /**
     * 
     * @param queryPlayerName the player to inquire about
     * @return a true/false value based on whether or not the user has an active game
     */
    public boolean isPlayerPlaying(String queryPlayerName){

        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            //Check if the player list contains the queried player
            for(Player p : gPlayerList){
                if(p.getName().equals(queryPlayerName)) return true;
            }
        }

        return false;

    }

}
