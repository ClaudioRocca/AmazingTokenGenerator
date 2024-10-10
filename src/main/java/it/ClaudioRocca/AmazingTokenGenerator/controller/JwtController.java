package it.ClaudioRocca.AmazingTokenGenerator.controller;

import it.ClaudioRocca.AmazingTokenGenerator.service.JwtService;
import it.ClaudioRocca.AmazingTokenGenerator.models.JwtCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/jwt")
@Validated
public class JwtController {
    @Autowired
    private JwtService jwtService;


    @PostMapping("/create")
    public ResponseEntity<?> createJwtToken(@RequestBody @Valid JwtCreateRequest tokenRequest) {
        return jwtService.createJwtToken(tokenRequest);

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyJwtToken(@RequestParam String token) {
        return jwtService.verifyJwtToken(token);
    }

}
