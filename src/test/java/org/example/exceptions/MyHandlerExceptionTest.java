package org.example.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author luigi
 * 16/09/2023
 */
class MyHandlerExceptionTest {
    @Test
    void myHandlerExceptionTest(){
        assertDoesNotThrow(() -> new MyHandlerException(new Exception()));
    }

}