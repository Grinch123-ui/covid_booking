package booking;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * This class produces a dummy QR code and URL for home testing. The generated values might not be
 * usable in reality.
 */
public class DummyGenerator {
    /**
     * Generate a random 10 digit number as a QR code.
     * @return the dummy QR code.
     */
    public String generateQR(){
        int m = (int) Math.pow(10, 10 - 1);
        int dummyQR = m + new Random().nextInt(9 * m);
        return Integer.toString(dummyQR);
    }

    /**
     * Generates a random dummy URL.
     * @return the dummy URL
     */
    public String generateRandomURL() {
        // the random string would be a static value of size 8.
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);

        return "https://" + generatedString + ".com";
    }
}
