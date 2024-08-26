package com.github.jcestaro.authorizer.configuration.advice;

import com.github.jcestaro.authorizer.configuration.exception.business.BusinessException;
import com.github.jcestaro.authorizer.configuration.exception.business.account.balance.InsufficientFundsException;
import com.github.jcestaro.authorizer.configuration.response.AuthorizerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Optei por deixar o controller advice como responsável por devolver os códigos de erro
 * dependendo da exceção que é lançada, dessa forma criando uma forma simples de adicionar novos
 * códigos para exceptions diferentes e usando slf4j para loggar exceptions não esperadas.
 */
@RestControllerAdvice
public class RestControllerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerConfiguration.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<AuthorizerResponse> handleUnexpected(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return getDefaultRejectResponse();
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<AuthorizerResponse> handleExpected(BusinessException e) {
        LOGGER.warn(e.getMessage(), e);
        return getDefaultRejectResponse();
    }

    @ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<AuthorizerResponse> handleExpected(InsufficientFundsException e) {
        return ResponseEntity
                .ok()
                .body(AuthorizerResponse.insufficientFundsResponse());
    }

    private ResponseEntity<AuthorizerResponse> getDefaultRejectResponse() {
        return ResponseEntity
                .ok()
                .body(AuthorizerResponse.rejectedResponse());
    }

}
