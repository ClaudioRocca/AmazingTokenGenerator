package it.ClaudioRocca.AmazingTokenGenerator.service;

import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class MacaroonService {
    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";
    public MacaroonService() {
    }


    public ResponseEntity<?> createMacaroon(String location, String identifier, Map<String, String> caveats) {
        Instant start = Instant.now();

        Macaroon macaroon = Macaroon.create(location, secret, identifier);

        MacaroonsBuilder builder = Macaroon.builder(macaroon);

        for(Map.Entry<String, String> entry : caveats.entrySet()) {
            builder.addCaveat(entry.getKey() + " = " + entry.getValue());
        }
        Instant end = Instant.now();

        return ResponseEntity.ok().body(Map.of(
                "macaroon", builder.build().serialize(),
                "tempo creazione token", end.toEpochMilli() - start.toEpochMilli() + " ms"
        ));

    }

    public ResponseEntity<?> verifyMacaroon(String macaroon) {
        Instant start = Instant.now();
        Macaroon macaroon1 = Macaroon.deserialize(macaroon);
        MacaroonsVerifier verifier = new MacaroonsVerifier(macaroon1);

        boolean isValid = verifier.isValid(secret);

        Instant end = Instant.now();

        return ResponseEntity.ok().body(Map.of(
                "valid", isValid,
                "tempo verifica token", end.toEpochMilli() - start.toEpochMilli() + " ms"
        ));
    }
}
