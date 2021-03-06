package com.webcheckers.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

/**
 * The game controller class for checkers.
 *
 * @author David Authur Cole
 */
public class GameController {
    
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    
    //Store list of all active games
    private List<Game> gameList;

    //Store list of completed games
    private List<Game> completedGameList;

    private List<Integer> takenIds;

    public GameController(){
        gameList = new ArrayList<>();
        completedGameList = new ArrayList<>();
        takenIds = new ArrayList<>();
    }

    /**
     * Adds a game to the completed game list
     * @param g the game to be added
     */
    public void addCompletedGame(Game g){
        if(!completedGameList.contains(g)){
            completedGameList.add(g);
        }
    }

    /**
     * @return the list of completed games
     */
    public List<Game> getCompletedGameList(){
        return completedGameList;
    }

    /**
     * @return the list of past games for a Player
     * @param p the player to be checked
     */
    public List<Game> getCompletedGameListForUser(Player p){
        List<Game> completedGames = new ArrayList<>();
        for(Game g : completedGameList){
            if( Arrays.asList(g.getPlayers()).contains(p)){
                completedGames.add(g);
            }
        }
        return completedGames;
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

    public Game getGameById(int id){
        for(Game g : gameList){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }

    /**
     * 
     * @param queryPlayer the player to inquire about
     * @return a true/false value based on whether or not the user has an active game
     */
    public boolean isPlayerPlaying(Player queryPlayer){

        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            //Check if the player list contains the queried player
            for(Player p : gPlayerList){
                if(p.toString().equals(queryPlayer.toString())) return true;
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
     * @param queryPlayer the name of the player who's game you wish to retrieve
     * @return the active game of the queried player
     */
    public Game getGameOfPlayer(Player queryPlayer){
        //For each game in the list
        for(Game g : gameList){
            //Get all players in said game
            Player[] gPlayerList = g.getPlayers();
            for(Player p : gPlayerList){
                //Return the game if the player is in the list
                if(p.toString().equals(queryPlayer.toString())) return g;
            }
        }

        return null;
    }
}
