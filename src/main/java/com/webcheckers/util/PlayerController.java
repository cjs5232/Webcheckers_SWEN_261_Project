package com.webcheckers.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class should be instatiated once when the web server is started, and will handle tracking logged in players
 */
public class PlayerController {
    
    Map<Integer, Player> playerMap;

    public PlayerController(){
        this.playerMap = new HashMap<>();
    }

    public boolean addPlayer(Player newPlayer){
        
        int hashId = newPlayer.getId();

        //TODO: Convert to playerMap.computeIfAbsent()
        if(!playerMap.containsKey(hashId)){
            playerMap.put(hashId, newPlayer);
            
            return true;
        }
        return false;
    }

    public boolean removePlayer(Player remPlayer){

        int hashId = remPlayer.getId();

        if(playerMap.containsKey(hashId)){
            playerMap.remove(hashId);
            return true;
        }
        return false;
    }

}
