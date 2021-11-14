package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DisappearingMessageTest {
    
    Player p1 = new Player("p1");
    DisappearingMessage dm1;
    DisappearingMessage dm2;

    @Test
    public void testInitialize(){
        dm1 = DisappearingMessage.info("Test message", 1);
        assertEquals("Test message", dm1.toString());
    }

    @Test
    public void testIncrement(){
        dm1 = DisappearingMessage.info("Test message", 2);
        p1.addDisappearingMessage(dm1);

        assertEquals(1, dm1.getRemainingDisplays());
        assertEquals(0, dm1.getRemainingDisplays());
    }

}
