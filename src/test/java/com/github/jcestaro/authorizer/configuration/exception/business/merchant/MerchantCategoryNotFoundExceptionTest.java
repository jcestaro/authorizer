package com.github.jcestaro.authorizer.configuration.exception.business.merchant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MerchantCategoryNotFoundExceptionTest {

    MerchantCategoryNotFoundException exception;

    @Test
    @DisplayName("Merchant category not found exception must have clear message")
    public void test01() {
        whenCreateNewMerchantCategoryNotFoundException();
        thenShouldHaveExpectedMessage();
    }

    private void whenCreateNewMerchantCategoryNotFoundException() {
        exception = new MerchantCategoryNotFoundException();
    }

    private void thenShouldHaveExpectedMessage() {
        String expectedMessage = "Merchant category not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

}