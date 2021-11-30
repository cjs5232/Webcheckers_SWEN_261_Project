package com.webcheckers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.webcheckers.model.DisappearingMessage;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Piece.Color;

public class PlayerTest {
    final String name_tester1 = "Tester1";
    final String name_tester2 = "Tester2";
    final String name_tester3 = "Tester3";

    Player tester1 = new Player(name_tester1);
    Player tester2 = new Player(name_tester2);
    Player tester3 = new Player(name_tester3);

    Game testGame = new Game(tester1, tester2);

    @Test
    void testConstruct(){
        assertEquals(tester1.toString(), name_tester1);
        assertEquals(0, tester1.getPlayerStatus());
        assertNull(tester1.getOpponent());
        assertFalse(tester1.getIsPlaying());

        assertEquals(false, tester1.isSpectating());
        assertNull(tester1.getSpectatingGame());
    }

    @Test
    void testDisappearingMessagesFunctions(){

        DisappearingMessage dm1 = DisappearingMessage.info("Test Message 1", 1);
        DisappearingMessage dm2 = DisappearingMessage.info("Test Message 2", 2);

        List<DisappearingMessage> dmList = tester1.getDisappearingMessages();

        tester1.addDisappearingMessage(dm1);
        tester1.addDisappearingMessage(dm2);

        assertEquals(2, tester1.getDisappearingMessages().size());

        tester1.removeDisappearingMessages(dmList);

        assertEquals(0, tester1.getDisappearingMessages().size());
    }

    @Test
    void testGamePromptFunctions(){

        tester1.promptForGame(tester2);
        assertEquals(1, tester1.getPrompts().size());

        assertEquals(tester1, tester2.getWaitingOn());
        tester2.clearWaitingOn();
        assertNotEquals(tester1, tester2.getWaitingOn());

        tester1.removePrompt(tester2);
        assertEquals(0, tester1.getPrompts().size());

        tester1.promptForGame(tester2);

        List<DisappearingMessage> prompts = tester1.getPrompts();
        tester1.removeOldPrompts(prompts);
        assertEquals(0, tester1.getPrompts().size());
    }

    @Test
    void testReplayFunctions(){

        tester1.setReplayGameID(100);
        assertEquals(100, tester1.getReplayGameID());

        Deque<Move> moveHistory = new ArrayDeque<>();
        moveHistory.add(new Move(new Position(0,0), new Position(0,1)));

        tester1.setMoveHistoryForReplay(moveHistory);
        assertEquals(Arrays.toString(moveHistory.toArray()), Arrays.toString(tester1.getMoveHistoryForReplay().toArray()));
    }

    @Test
    void testSpectatingFunctions(){

        tester3.setSpectatingGame(testGame);
        assertEquals(testGame, tester3.getSpectatingGame());

        tester3.setSpectating(true);
        assertTrue(tester3.isSpectating());

        tester3.setLastKnownTurnColor(Color.WHITE);
        assertEquals(Color.WHITE, tester3.getLastKnownTurnColor());

    }

    @Test
    void TestBasics() {
        assertFalse(tester1.getIsPlaying());
        assertFalse(tester2.getIsPlaying());

        tester1.setIsPlaying(true);
        assertTrue(tester1.getIsPlaying());
        tester2.setIsPlaying(true);
        assertTrue(tester2.getIsPlaying());

        tester1.setOpponent(tester2);

        assertSame(tester1.getOpponent(), tester2);
        assertNotSame(tester2.getOpponent(), tester1);

        tester2.setOpponent(tester1);

        assertSame(tester1.getOpponent(), tester2);
        assertSame(tester2.getOpponent(), tester1);

        tester1.setPlayerStatus(tester1.getId());
        assertEquals(tester1.getPlayerStatus(), tester1.getId());

        tester2.setPlayerStatus(tester2.getId());
        assertEquals(tester2.getPlayerStatus(), tester2.getId());
    }
}
