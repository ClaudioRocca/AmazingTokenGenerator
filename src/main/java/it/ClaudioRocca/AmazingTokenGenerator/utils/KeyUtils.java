package it.ClaudioRocca.AmazingTokenGenerator.utils;

import org.paseto4j.commons.PublicKey;
import org.paseto4j.commons.Version;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {


    public static PrivateKey readPrivateKey(String filePath) throws Exception {
        try (InputStream keyStream = KeyUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (keyStream == null) {
                throw new FileNotFoundException("Private key file not found in classpath.");
            }
            byte[] keyBytes = keyStream.readAllBytes();
            String privateKeyPEM = new String(keyBytes)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN EC PRIVATE KEY-----", "")
                    .replace("-----END EC PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);

            // Specifica PKCS8EncodedKeySpec per chiavi PKCS#8
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");

            return keyFactory.generatePrivate(spec);
        }
    }


}

