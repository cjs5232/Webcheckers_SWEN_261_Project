package com.webcheckers.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

import com.webcheckers.ui.WebServer;

public class Queue {
    
    private static final Logger LOG = Logger.getLogger(Queue.class.getName());

    //Create a queue for players
    private Deque<Player> playerQueue;

    //Constructor
    public Queue(){
        playerQueue = new ArrayDeque<>();
    }

    /**
     * Adds a player to the queue
     * @param player the player to add
     */
    public void addPlayer(Player player){
        if(!playerQueue.contains(player)){
            playerQueue.add(player);
        } 
    }

    /**
     * Removes a player from the queue
     * @param player the player to remove
     */
    public void removePlayer(Player player){
        if(playerQueue.contains(player)){
            playerQueue.remove(player);
        }
    }

    /**
     * @return the playerQueue size
     */
    public int getNumberOfPlayers(){
        return playerQueue.size();
    }

    public Game createNewGame(){
        if(playerQueue.size() < 2){
            return null;
        }
        //Grab 2 players from the queue, and create a new game
        if(WebServer.DEBUG_FLAG) LOG.info("Queue size before pops: " + playerQueue.size());
        Player redPlayer = playerQueue.pop();
        Player whitePlayer = playerQueue.pop();
        if(WebServer.DEBUG_FLAG) LOG.info("Queue size after pops: " + playerQueue.size());

        //Log game creation
        if(WebServer.DEBUG_FLAG) LOG.info("New game is being created between " + redPlayer + " and " + whitePlayer);

        //Create a new game
        Game newGame = new Game(redPlayer, whitePlayer);

        //Add the game to the controller, and return it
        WebServer.GLOBAL_GAME_CONTROLLER.addGame(newGame);
        return newGame;
    }

}
