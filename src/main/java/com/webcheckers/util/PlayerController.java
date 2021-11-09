package com.webcheckers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class should be instatiated once when the web server is started, and will handle tracking logged in players
 */
public class PlayerController {

    Map<Integer, Player> playerMap;

    public PlayerController(){
        this.playerMap = new HashMap<>();
    }

    public String addPlayer(Player newPlayer){
        
        int hashId = newPlayer.getId();

        if(!newPlayer.getName().matches("^[a-zA-Z0-9\\s]+$")){
            return "Name contained invalid characters.";
        }

        Player previous = playerMap.putIfAbsent(hashId, newPlayer);

        if (previous != null) {
            return "User with this name already exists.";
        } else {
            return "";
        }
    }

    public boolean removePlayer(String playerName){

        //Create an iterator to move through the entire map
        Iterator<Map.Entry<Integer, Player> > iterator = playerMap.entrySet().iterator();

        while(iterator.hasNext()){

            Map.Entry<Integer, Player> entry = iterator.next();

            //If the entry has the name of the player, remove the player
            if(entry.getValue().toString().equals(playerName)){ 
                iterator.remove();
                return true;
            }
        }

        //The player was not found, or does not exist - this should never be reached by classical UI navigation
        return false;
    }

    /**
     * 
     * @param playerName The name of the player to access
     * @return The Player object associated with the passed name
     */
    public Player getPlayerByName(String playerName){
        //Create an iterator to move through the entire map

        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            if (entry.getValue().toString().equals(playerName)) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * 
     * @return A list of all logged in players
     */
    public List<Player> getPlayers(){

        ArrayList<Player> loggedInPlayers = new ArrayList<>();

        //Create an iterator to move through the entire map

        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            loggedInPlayers.add(entry.getValue());
        }

        return loggedInPlayers;
    }

    /**
     * 
     * @param ignoredPlayer The player to ignore from the list, used to show "otherUsers" in home.ftl
     * @return A list of all logged in players, ignoring the param
     * 
     * @see home.ftl
     */
    public List<Player> getPlayersExcept(String ignoredPlayer){

        ArrayList<Player> loggedInPlayers = new ArrayList<>();

        //Create an iterator to move through the entire map

        for (Map.Entry<Integer, Player> entry : playerMap.entrySet()) {
            if (!entry.getValue().toString().equals(ignoredPlayer)) {
                loggedInPlayers.add(entry.getValue());
            }
        }

        return loggedInPlayers;
    }
}
