package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MessageTest {
    
    Message m1;
    Message m2;

    @Test
    public void testInit(){

        m1 = Message.info("Test message");
        assertEquals("Test message", m1.toString());
        assertEquals("{Msg " + Message.Type.INFO + " '" + "Test message" + "'}", m1.toStringVerbose());
        assertEquals(Message.Type.INFO, m1.getType());
        assertTrue(m1.isSuccessful());

        m2 = Message.error("Test error");
        assertEquals("Test error", m2.toString());
        assertEquals("{Msg " + Message.Type.ERROR + " '" + "Test error" + "'}", m2.toStringVerbose());
        assertEquals(Message.Type.ERROR, m2.getType());
        assertFalse(m2.isSuccessful());
    }

}
