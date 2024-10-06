package it.ClaudioRocca.AmazingTokenGenerator.utils;

import org.paseto4j.commons.Version;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class KeyUtils {

    public static org.paseto4j.commons.PrivateKey loadPrivateKey(String privateKeyPath) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        keyPairGen.initialize(new ECGenParameterSpec("secp384r1"), new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        java.security.PrivateKey privateKey = keyPair.getPrivate();
        //writePrivateKeyToPEM(privateKey, privateKeyPath);

        return new org.paseto4j.commons.PrivateKey(privateKey, Version.V3);
    }

    private static void writePrivateKeyToPEM(java.security.PrivateKey privateKey, String filePath) throws IOException {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");
        pem.append(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        pem.append("\n-----END PRIVATE KEY-----\n");

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(pem.toString());
        }
    }





}

