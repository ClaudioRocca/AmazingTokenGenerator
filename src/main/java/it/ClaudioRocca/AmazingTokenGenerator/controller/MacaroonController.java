package it.ClaudioRocca.AmazingTokenGenerator.controller;


import com.github.nitram509.jmacaroons.CaveatPacket;
import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token/macaroon")
public class MacaroonController {
    private final String secret = "yJojIqZismADFUmhEjgB9NJxh20JpP4d";

    @PostMapping("/create")
    public String createMacaroon(@RequestParam String location, @RequestParam String identifier) {
        Macaroon macaroon = Macaroon.create(location, secret, identifier);

        MacaroonsBuilder builder = Macaroon.builder(macaroon);
//                .addCaveat("before 2021-12-31T23:59:59");


        return builder.build().serialize();

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
