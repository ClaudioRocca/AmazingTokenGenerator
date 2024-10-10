package it.ClaudioRocca.AmazingTokenGenerator.utils;

import com.nimbusds.jose.shaded.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PasetoUtils {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static boolean checkIat(JsonObject jsonPayload) {
        String iat = jsonPayload.get("iat").getAsString();
        try {
            Date iatDate = formatter.parse(iat);
            Date now = new Date();
            return now.after(iatDate);
        }
        catch (Exception e) {
            return false;
        }

    }

    public static boolean checkNbf(JsonObject jsonPayload) {
        String nbf = jsonPayload.get("nbf").getAsString();
        try {
            Date nbfDate = formatter.parse(nbf);
            Date now = new Date();
            return now.after(nbfDate);
        }
        catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkExpiration(JsonObject jsonPayload) {
        String exp = jsonPayload.get("exp").getAsString();
        try {
            Date expDate = formatter.parse(exp);
            Date now = new Date();
            return now.before(expDate);
        }
        catch (Exception e) {
            return false;
        }
    }
}
