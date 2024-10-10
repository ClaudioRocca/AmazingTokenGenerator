package it.ClaudioRocca.AmazingTokenGenerator.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import it.ClaudioRocca.AmazingTokenGenerator.service.PasetoLocalService;
import org.apache.tomcat.util.json.JSONParser;
import org.paseto4j.version3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.paseto4j.commons.SecretKey;

import org.paseto4j.commons.Version;

@RestController
@RequestMapping("/token/paseto/local")
public class PasetoLocalController {


    @Autowired
    private PasetoLocalService pasetoLocalService;


    @Autowired
    public PasetoLocalController() {

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        return pasetoLocalService.verifyToken(token);

    }


    @PostMapping("/create")
    public ResponseEntity<?> createLocalToken(@RequestBody String data,
                                                  @RequestParam(required = false) String footer) {
        return pasetoLocalService.createToken(data, footer);
    }
}
