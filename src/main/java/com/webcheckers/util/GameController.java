package com.webcheckers.util;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    
    //Store list of all active games
    List<Game> gameList;
    List<Player> queue;

    public GameController(){
        gameList = new ArrayList<>();
        queue = new ArrayList<>();
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

    /**
     * @param newPlayer the Player class of the new player
     * add the player to the queue
     */
    public void putInQueue(Player newPlayer) {
        queue.add(newPlayer);
    }

    /**
     * @param player the Player class of the player
     * see if the player is in the queue and check if they can be removed
     */
    public boolean inQueue(Player player) {
        if (queue.size() > 1 && queue.get(0) == player) {
            Player opponent = queue.get(1);
            //Update status of players
            opponent.setPlayerStatus(2);
            player.setPlayerStatus(2);
            //Remove players from queue
            queue.remove(opponent);
            queue.remove(player);
        }
        //check if in queue
        if (queue.contains(player)) {
            return true;
        }
        return false;
    }


    /**
     * @param queryPlayerName the name of the player who's game you wish to retrieve
     * @return the active game of the queried player
     */
    public Game getGameOfPlayer(String queryPlayerName){
        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            for(Player p : gPlayerList){
                //Return the game if the player is in the list
                if(p.getName().equals(queryPlayerName)) return g;
            }
        }

        return null;
    }
}
