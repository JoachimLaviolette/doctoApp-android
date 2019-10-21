package if26.android.doctoapp.Services;

import java.security.MessageDigest;
import java.util.UUID;

public class EncryptionService {
    /**
     * Convert the given data into hex
     * @param data Data to convert to hex
     * @return The converted data
     */
    private static String ConvertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();

        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;

            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }

        return buf.toString();
    }

    /**
     * SHA1 the given string representing the clear password
     * @param clearPwd The clear password
     * @return The password SHA1'd
     */
    public static String SHA1(String clearPwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] textBytes = clearPwd.getBytes("iso-8859-1");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();

            return ConvertToHex(sha1hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create a salt from the given encrypted password
     * @return The created salt
     */
    public static String SALT() {
        return SHA1(UUID.randomUUID().toString());
    }
}
