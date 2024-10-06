package it.ClaudioRocca.AmazingTokenGenerator.controller;

import org.paseto4j.version3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.paseto4j.commons.SecretKey;

import org.paseto4j.commons.Version;

@RestController
@RequestMapping("/token/paseto/local")
public class PasetoLocalController {
    private final String key = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";
    private final SecretKey secretKey = new SecretKey(key.getBytes(), Version.V3);
    Base64.Decoder decoder = Base64.getDecoder();


    @Autowired
    public PasetoLocalController() {

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {
        Instant start = Instant.now();

        try {
            String base64EncodedFooter = Optional.of(token.split("\\.")[3]).orElse("");
            String footer = new String(decoder.decode(base64EncodedFooter));

            String decodedPayload = Paseto.decrypt(secretKey, token, footer);
            Instant end = Instant.now();

            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

            return ResponseEntity.ok()
                    .body(Map.of(
                            "decoded_payload", decodedPayload,
                            "footer", footer,
                            "time_elapsed", timeElapsed + " ms"
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Token decoding failed: " + e.getMessage());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<String> createLocalToken(@RequestBody String data,
                                                  @RequestParam(required = false) String footer) {
        try {
            if(footer == null) {
                footer = "";
            }
            String token = Paseto.encrypt(secretKey, data, footer);
            return ResponseEntity.ok(token);

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid data format or error creating token.");
        }
    }
}
