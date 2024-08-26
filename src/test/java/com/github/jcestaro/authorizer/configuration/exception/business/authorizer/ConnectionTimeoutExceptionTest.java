package com.github.jcestaro.authorizer.configuration.exception.business.authorizer;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionTimeoutExceptionTest {

    ConnectionTimeoutException exception;

    @Test
    @DisplayName("Connection timeout exception must have clear message")
    public void test01() {
        whenCreateNewConnectionTimeoutException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewConnectionTimeoutException() {
        exception = new ConnectionTimeoutException(new BusinessException("test"));
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "Connection timeout: test";
        assertEquals(expectedMessage, exception.getMessage());
    }

}