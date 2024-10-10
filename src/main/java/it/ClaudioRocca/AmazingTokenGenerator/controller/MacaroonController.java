package it.ClaudioRocca.AmazingTokenGenerator.controller;


import com.github.nitram509.jmacaroons.CaveatPacket;
import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/token/macaroon")
public class MacaroonController {
    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";

    @PostMapping("/create")
    public ResponseEntity<?> createMacaroon(@RequestParam String location, @RequestParam String identifier, @RequestBody Map<String, String> caveats) {
        Instant start = Instant.now();

        Macaroon macaroon = Macaroon.create(location, secret, identifier);

        MacaroonsBuilder builder = Macaroon.builder(macaroon);

        for(Map.Entry<String, String> entry : caveats.entrySet()) {
            builder.addCaveat(entry.getKey() + " = " + entry.getValue());
        }
        Instant end = Instant.now();

        return ResponseEntity.ok().body(Map.of(
                "macaroon", builder.build().serialize(),
                "time_elapsed", end.toEpochMilli() - start.toEpochMilli() + " ms"
        ));

    }

    @PostMapping("/verify")
    public String verifyMacaroon(@RequestParam String macaroon) {
         MacaroonsVerifier verifier = new MacaroonsVerifier(Macaroon.deserialize(macaroon));
//         for(CaveatPacket caveat : verifier.getMacaroon().caveatPackets) {
//             verifier.satisfyExact("before 2021-12-31T23:59:59");
//         }

        return verifier.isValid(secret) ? "Valid" : "Invalid";

    }
}
