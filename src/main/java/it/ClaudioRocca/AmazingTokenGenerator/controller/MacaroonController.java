package it.ClaudioRocca.AmazingTokenGenerator.controller;


import com.github.nitram509.jmacaroons.CaveatPacket;
import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;
import it.ClaudioRocca.AmazingTokenGenerator.service.MacaroonService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/token/macaroon")
public class MacaroonController{

    @Autowired
    private MacaroonService macaroonService;

    @PostMapping("/create")
    public ResponseEntity<?> createMacaroon(@RequestParam String location, @RequestParam String identifier, @RequestBody Map<String, String> caveats) {
        return macaroonService.createMacaroon(location, identifier, caveats);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyMacaroon(@RequestParam String macaroon) {
         return macaroonService.verifyMacaroon(macaroon);

    }
}
