package it.ClaudioRocca.AmazingTokenGenerator.controller;

import it.ClaudioRocca.AmazingTokenGenerator.utils.KeyUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.paseto4j.commons.PrivateKey;
import org.paseto4j.commons.PublicKey;
import org.paseto4j.commons.SecretKey;
import org.paseto4j.version3.Paseto;
import org.paseto4j.commons.Version;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/token/paseto/public")
public class PasetoPublicController {

    static {
        // Aggiunge il provider BouncyCastle se non è già presente
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    // Percorsi dei file PEM delle chiavi
    private final String privateKeyPath = "keys/private_key_pkcs8.pem";
    private final String publicKeyPath = "keys/public_key.pem";


    private PrivateKey loadPrivateKey() throws Exception {

        java.security.PrivateKey privateKey = KeyUtils.readPrivateKey(privateKeyPath);

        return new PrivateKey(privateKey, Version.V3);
    }


    private org.paseto4j.commons.PublicKey loadPublicKey() throws Exception {
        try (InputStream keyStream = getClass().getClassLoader().getResourceAsStream(publicKeyPath)) {
            if (keyStream == null) {
                throw new FileNotFoundException("Private key file not found in classpath.");
            }
            byte[] keyBytes = keyStream.readAllBytes();
            String privateKeyPEM = new String(keyBytes)
                    .replace("-----BEGINPUBLICKEY-----", "")
                    .replace("-----ENDPUBLICKEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
            return new PublicKey(decodedKey, Version.V3);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPublicPaseto(@RequestBody String data, @RequestParam(required = false) String footer) {
        try {
            PrivateKey secretKey = loadPrivateKey(); // Carica la chiave privata
            if (footer == null) {
                footer = "";
            }
            // Firma il token PASETO con la chiave privata
            String token = Paseto.sign(secretKey, data, footer);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore nella creazione del token PASETO pubblico.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPublicPaseto(@RequestParam String token) {
        Instant start = Instant.now();
        try {
            PublicKey publicKey = loadPublicKey(); // Carica la chiave pubblica

            String base64EncodedFooter = Optional.of(token.split("\\.")[3]).orElse("");
            String footer = new String(Base64.getDecoder().decode(base64EncodedFooter));

            // Verifica il token PASETO con la chiave pubblica
            String decodedPayload = Paseto.parse(publicKey, token, footer);

            Instant end = Instant.now();
            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();

            return ResponseEntity.ok()
                    .body(Map.of(
                            "decoded_payload", decodedPayload,
                            "footer", footer,
                            "time_elapsed", timeElapsed + " ms"
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore nella verifica del token PASETO pubblico: " + e.getMessage());
        }
    }
}
