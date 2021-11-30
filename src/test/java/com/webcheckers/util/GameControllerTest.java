package com.webcheckers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.webcheckers.application.GameController;
import com.webcheckers.application.PlayerController;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

public class GameControllerTest {
    
    private Player p1 = mock(Player.class);
    private Player p2 = mock(Player.class);

    private PlayerController pc = new PlayerController();

    private Player p3 = new Player("Test1");
    private Player p4 = new Player("Test2");
    
    private Game testGame = new Game(p1, p2);
    private Game testGame2 = new Game(p3, p4);
    private GameController gc = new GameController();

    @Test
    void testAddRemoveGame(){
        //Add the game
        gc.addGame(testGame);

        //Make sure the game exists in the list
        assertTrue(gc.getGames().contains(testGame));

        //Remove the game
        gc.removeGame(testGame);

        //Make sure the game no longer exists in the list
        assertFalse(gc.getGames().contains(testGame));
    }

    @Test
    void testIsPlayerPlaying(){
        
        pc.addPlayer(p3);
        pc.addPlayer(p4);

        //Add the game
        gc.addGame(testGame2);

        //Make sure the player is playing
        assertTrue(gc.isPlayerPlaying(p3));
        assertTrue(gc.isPlayerPlaying(p4));

        //Remove the game
        gc.removeGame(testGame2);

        //Make sure the player is not playing
        assertFalse(gc.isPlayerPlaying(p3));
        assertFalse(gc.isPlayerPlaying(p4));
    }

    @Test
    void testGetGameOfPlayer(){
        pc.addPlayer(p3);
        pc.addPlayer(p4);

        //Add the game
        gc.addGame(testGame2);

        //Make sure the game is the correct game
        assertSame(testGame2, gc.getGameOfPlayer(p3));
        assertSame(testGame2, gc.getGameOfPlayer(p4));

        //Remove the game
        gc.removeGame(testGame2);

        //Make sure the game is null
        assertSame(null, gc.getGameOfPlayer(p3));
        assertSame(null, gc.getGameOfPlayer(p4));
    }
}
