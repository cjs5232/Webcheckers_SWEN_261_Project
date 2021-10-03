package com.webcheckers.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class should be instatiated once when the web server is started, and will handle tracking logged in players
 */
public class PlayerController {
    
    private static final Logger LOG = Logger.getLogger(PlayerController.class.getName());

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

    public boolean removePlayer(String playerName){

        Iterator<Map.Entry<Integer, Player> > iterator = playerMap.entrySet().iterator();

        while(iterator.hasNext()){

            Map.Entry<Integer, Player> entry = iterator.next();

            if(entry.getValue().toString().equals(playerName)){
                
                iterator.remove();
                return true;
            }
        }

        return false;

    }

}
