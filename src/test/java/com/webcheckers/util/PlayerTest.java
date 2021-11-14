package com.webcheckers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PlayerTest {
    final String name_tester1 = "Tester1";
    final String name_tester2 = "Tester2";

    Player tester1 = new Player(name_tester1);
    Player tester2 = new Player(name_tester2);

    @Test
    void TestBasics() {
        assertFalse(tester1.getIsPlaying());
        assertFalse(tester2.getIsPlaying());

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
