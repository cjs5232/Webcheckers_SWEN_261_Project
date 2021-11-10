package com.webcheckers.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.webcheckers.ui.WebServer;

public class GameController {
    
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    
    //Store list of all active games
    private List<Game> gameList;

    private List<Integer> takenIds;

    public GameController(){
        gameList = new ArrayList<>();
        takenIds = new ArrayList<>();
    }

    /**
     * @param g the new game to be tracked by the controller
     */
    public void addGame(Game g){

        //Get the id of the game to be added
        int newGameId = g.getId();

        //Change it until it's unique
        while(takenIds.contains(newGameId)){
            g.setNewId();
        }

        //Add the ID to the list of taken IDs
        takenIds.add(g.getId());

        //Add the game to the list of games
        this.gameList.add(g);
    }

    /**
     * @param g the game to remove (no longer active)
     */
    public void removeGame(Game g){
        //Remove the ID and the game if it's in the controller
        if(gameList.contains(g)){
            takenIds.remove(Integer.valueOf(g.getId()));
            gameList.remove(g);
        }
    }

    /**
     * @return the list of all active games
     */
    public List<Game> getGames(){
        return this.gameList;
    }

    /**
     * 
     * @param queryPlayerName the player to inquire about
     * @return a true/false value based on whether or not the user has an active game
     */
    public boolean isPlayerPlaying(Player queryPlayer){

        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            //Check if the player list contains the queried player
            for(Player p : gPlayerList){
                if(p.getName().equals(queryPlayer.toString())) return true;
            }
        }
        return false;
    }

    public void handlePlayerExitGame(int id, Player p){
        Game toRemove = null;
        for(Game g : gameList){
            if(g.getId() == id){
                g.playerExited(p);
            }
            if(g.getPlayersExited().size() == 2){
                toRemove = g;
            }
        }
        if(toRemove != null){
            if(WebServer.DEBUG_FLAG) LOG.config("Game " + toRemove.getId() + " has been removed from the controller");
            removeGame(toRemove);
        }
    }

    /**
     * @param queryPlayerName the name of the player who's game you wish to retrieve
     * @return the active game of the queried player
     */
    public Game getGameOfPlayer(Player queryPlayer){
        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            for(Player p : gPlayerList){
                //Return the game if the player is in the list
                if(p.getName().equals(queryPlayer.toString())) return g;
            }
        }

        return null;
    }
}
