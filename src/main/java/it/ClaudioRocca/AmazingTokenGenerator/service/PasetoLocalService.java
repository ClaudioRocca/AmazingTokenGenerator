package it.ClaudioRocca.AmazingTokenGenerator.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import it.ClaudioRocca.AmazingTokenGenerator.utils.PasetoUtils;
import org.paseto4j.commons.SecretKey;
import org.paseto4j.commons.Version;
import org.paseto4j.version3.Paseto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static it.ClaudioRocca.AmazingTokenGenerator.utils.PasetoUtils.*;

@Service
public class PasetoLocalService {

    private final String key = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";
    private final SecretKey secretKey = new SecretKey(key.getBytes(), Version.V3);
    Base64.Decoder decoder = Base64.getDecoder();

    public PasetoLocalService() {
    }

    public ResponseEntity<?> verifyToken (String token, String subject) {
        ArrayList<String> errors = new ArrayList<>();
        Instant start = Instant.now();

        try {
            String footer = "";
            String[] tokenParts = token.split("\\.");
            if(tokenParts.length == 4) {
                String base64EncodedFooter = tokenParts[3];
                footer = new String(decoder.decode(base64EncodedFooter));
            }
            String decodedPayload = Paseto.decrypt(secretKey, token, footer);
            Gson gson = new Gson();
            JsonObject jsonPayload = gson.fromJson(decodedPayload, JsonObject.class);
            Boolean isValid = true;
            if(jsonPayload.has("exp") && !checkExpiration(jsonPayload)) {
                errors.add("Token scaduto");
                isValid = false;
            }
            if(jsonPayload.has("nbf") && !checkNbf(jsonPayload)){
                isValid = false;
                errors.add("Token non ancora valido");
            }
            if(jsonPayload.has("iat") && !checkIat(jsonPayload)) {
                isValid = false;
                errors.add("Il token riporta data di emissione nel futuro");
            }

            if(jsonPayload.has("sub") && !jsonPayload.get("sub").getAsString().equals(subject)) {
                isValid = false;
                errors.add("Il token non è associato al soggetto richiesto");
            }

            Instant end = Instant.now();

            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

            return ResponseEntity.ok()
                    .body(Map.of(
                            "decoded_payload", decodedPayload,
                            "footer", footer,
                            "valido", isValid,
                            "errors", errors,
                            "tempo verifica token", timeElapsed + " ms"
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Token decoding failed: " + e.getMessage());
        }
    }

    public ResponseEntity<?> createToken(String data, String footer) {
        try {
            if(footer == null) {
                footer = "";
            }
            Instant start = Instant.now();
            String token = Paseto.encrypt(secretKey, data, footer);
            Instant end = Instant.now();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "token", token,
                            "tempo generazione token", (end.toEpochMilli() - start.toEpochMilli()) + " ms"
                    ));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid data format or error creating token.");
        }
    }




}
