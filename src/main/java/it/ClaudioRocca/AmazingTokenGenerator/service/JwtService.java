package it.ClaudioRocca.AmazingTokenGenerator.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.ClaudioRocca.AmazingTokenGenerator.models.JwtCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";

    public ResponseEntity<String> createJwtToken(JwtCreateRequest tokenRequest) {
        try {

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();

            claimsBuilder.subject(tokenRequest.getSub());
            if(tokenRequest.getAud() != null)
                claimsBuilder.audience(tokenRequest.getAud());
            if(tokenRequest.getIss() != null)
                claimsBuilder.issuer(tokenRequest.getIss());
            if(tokenRequest.getExp() != null)
                claimsBuilder.expirationTime(new Date(tokenRequest.getExp()));
            if(tokenRequest.getIat() != null)
                claimsBuilder.issueTime(new Date(tokenRequest.getIat()));
            if(tokenRequest.getNbf() != null)
                claimsBuilder.notBeforeTime(new Date(tokenRequest.getNbf()));

            JWTClaimsSet claimsSet = claimsBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null),
                    claimsSet);
            JWSSigner signer = new MACSigner(secret);
            signedJWT.sign(signer);

            return ResponseEntity.ok(signedJWT.serialize());
        } catch (
                JOSEException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<?> verifyJwtToken(String token) {
        Instant start = Instant.now();

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            if (!signedJWT.verify(verifier)) {
                return ResponseEntity.badRequest().body("Firma del token non valida.");
            }

            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expiration)) {
                return ResponseEntity.badRequest().body("Token JWT scaduto.");
            }

            Instant end = Instant.now();
            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

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
