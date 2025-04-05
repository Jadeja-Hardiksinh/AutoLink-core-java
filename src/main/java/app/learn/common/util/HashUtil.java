package app.learn.common.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String createHash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public static boolean compareHash(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}
