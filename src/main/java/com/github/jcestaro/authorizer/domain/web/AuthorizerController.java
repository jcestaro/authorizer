package com.github.jcestaro.authorizer.domain.web;

import com.github.jcestaro.authorizer.configuration.response.AuthorizerResponse;
import com.github.jcestaro.authorizer.domain.facade.AuthorizerFacade;
import com.github.jcestaro.authorizer.domain.web.form.AuthorizerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authorize")
public class AuthorizerController {

    private final AuthorizerFacade authorizerFacade;

    @Autowired
    public AuthorizerController(AuthorizerFacade authorizerFacade) {
        this.authorizerFacade = authorizerFacade;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<AuthorizerResponse> authorize(@RequestBody AuthorizerForm authorizerForm) {
        authorizerFacade.authorize(authorizerForm);

        return ResponseEntity
                .ok()
                .body(AuthorizerResponse.approvedResponse());
    }

}
