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
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";

    public ResponseEntity<?> createJwtToken(JwtCreateRequest tokenRequest) {
        try {

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
            Instant start = Instant.now();

            claimsBuilder.subject(tokenRequest.getSub());
            if(tokenRequest.getAud() != null)
                claimsBuilder.audience(tokenRequest.getAud());
            if(tokenRequest.getIss() != null)
                claimsBuilder.issuer(tokenRequest.getIss());
            if(tokenRequest.getExp() != null)
                claimsBuilder.expirationTime(new Date(tokenRequest.getExp()));
            if(tokenRequest.getIat() != null)
                claimsBuilder.issueTime(new Date(tokenRequest.getIat()));
            else
                claimsBuilder.issueTime(new Date());
            if(tokenRequest.getNbf() != null)
                claimsBuilder.notBeforeTime(new Date(tokenRequest.getNbf()));

            JWTClaimsSet claimsSet = claimsBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null),
                    claimsSet);
            JWSSigner signer = new MACSigner(secret);
            signedJWT.sign(signer);
            Instant end = Instant.now();

            return ResponseEntity.ok(Map.of(
                    "token", signedJWT.serialize(),
                    "tempo generazione token", (end.toEpochMilli() - start.toEpochMilli()) + " ms"))
            ;
        } catch (
                JOSEException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<?> verifyJwtToken(String token, String subject) {
        Instant start = Instant.now();
        ArrayList<String> errors = new ArrayList<>();
        boolean isValid = true;

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);
            String sub = signedJWT.getJWTClaimsSet().getSubject();

            if (!signedJWT.verify(verifier)) {
                errors.add("Firma del token non valida.");
                isValid = false;
            }

            if (null != subject && !sub.equals(subject)) {
                errors.add("Il soggetto del token non corrisponde a quello richiesto.");
                isValid = false;
            }

            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (null != expiration && new Date().after(expiration)) {
                errors.add("Token scaduto.");
                isValid = false;
            }

            Date issuedAt = signedJWT.getJWTClaimsSet().getIssueTime();
            if (null != issuedAt && new Date().before(issuedAt)) {
                errors.add("Il token riporta data di emissione nel futuro.");
                isValid = false;
            }

            Date notBefore = signedJWT.getJWTClaimsSet().getNotBeforeTime();
            if (null != notBefore && new Date().before(notBefore)) {
                errors.add("Il token non è ancora valido.");
                isValid = false;
            }

            Instant end = Instant.now();
            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

            Map<String, Object> payload = signedJWT.getJWTClaimsSet().getClaims();

            return ResponseEntity.ok()
                    .body(Map.of(
                            "payload", payload,
                            "valido", isValid,
                            "tempo verifica token", timeElapsed + " ms",
                            "errori", errors
                    ));

        } catch (ParseException | JOSEException e) {
            return ResponseEntity.badRequest().body("Errore nella decodifica del token JWT: " + e.getMessage());
        }
    }
}
