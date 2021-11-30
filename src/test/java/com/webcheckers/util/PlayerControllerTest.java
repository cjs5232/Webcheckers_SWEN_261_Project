package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.webcheckers.application.PlayerController;
import com.webcheckers.model.Player;

import org.junit.jupiter.api.Test;

class PlayerControllerTest {
    PlayerController pc = new PlayerController();

    Player player = new Player("Name");
    Player player2 = new Player("Name2");
    Player player3 = new Player("Name3");

    @Test
    //Verify that players can be added and found by name
    void testAddPlayer() {
        
        pc.addPlayer(player);
        assertNotNull(pc.getPlayerByName("Name"));
    }

    @Test
    //Verify that players cannot be added if the name is already taken
    void testDuplicateName(){

        //Add the first player
        pc.addPlayer(player);

        //Attempt to add the second player - this should fail
        assertEquals("User with this name already exists.", pc.addPlayer(player)); 

        //Remove the first player
        pc.removePlayer("Name");

        //Attempt to add the second player - this should succeed
        assertEquals("", pc.addPlayer(player2));
    }

    @Test
    void testGetPlayers(){

        pc.addPlayer(player);
        pc.addPlayer(player2);

        assertEquals(2, pc.getPlayers().size());
    }

    @Test
    void testGetPlayersExcept(){
        
        pc.addPlayer(player);
        pc.addPlayer(player2);
        pc.addPlayer(player3);

        //Size should be 2 players
        assertEquals(2, pc.getPlayersExcept("Name").size());

        //List should not contain excluded player
        assertFalse(pc.getPlayersExcept("Name").contains(pc.getPlayerByName("Name")));
    }

    @Test
    void testGetPlayerByName(){
        pc.addPlayer(player);
        assertEquals(player, pc.getPlayerByName("Name"));
    }
}
