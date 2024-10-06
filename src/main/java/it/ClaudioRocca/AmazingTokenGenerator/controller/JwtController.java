package it.ClaudioRocca.AmazingTokenGenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/token/jwt")
public class JwtController {

    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d"; // Deve essere di almeno 256 bit per HMAC SHA-256
    private final Base64.Decoder decoder = Base64.getDecoder();

    @Autowired
    public JwtController() {
    }

    @PostMapping("/create")
    public ResponseEntity<String> createJwtToken(@RequestBody Map<String, Object> data) {
        try {
            Date iat = new Date(124, Calendar.OCTOBER, 29, 0, 0, 0);
            Date exp = new Date(124, Calendar.OCTOBER, 29, 23, 59, 59);
            // Definisce il payload del JWT
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .issueTime(iat)
                    .expirationTime(exp);

            // Aggiunge i dati passati come payload
            data.forEach(claimsBuilder::claim);

            JWTClaimsSet claimsSet = claimsBuilder.build();

            // Crea un oggetto SignedJWT con algoritmo HMAC
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);

            // Firma il token
            JWSSigner signer = new MACSigner(secret);
            signedJWT.sign(signer);

            // Ritorna il token JWT serializzato
            return ResponseEntity.ok(signedJWT.serialize());

        } catch (JOSEException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore nella creazione del token JWT.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyJwtToken(@RequestParam String token) {
        Instant start = Instant.now();

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            // Verifica la firma
            if (!signedJWT.verify(verifier)) {
                return ResponseEntity.badRequest().body("Firma del token non valida.");
            }

            // Controlla se il token è scaduto
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expiration)) {
                return ResponseEntity.badRequest().body("Token JWT scaduto.");
            }

            Instant end = Instant.now();
            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

            // Recupera il payload
            Map<String, Object> payload = signedJWT.getJWTClaimsSet().getClaims();

            return ResponseEntity.ok()
                    .body(Map.of(
                            "decoded_payload", payload,
                            "time_elapsed", timeElapsed + " ms"
                    ));

        } catch (ParseException | JOSEException e) {
            return ResponseEntity.badRequest().body("Errore nella decodifica del token JWT: " + e.getMessage());
        }
    }
}
