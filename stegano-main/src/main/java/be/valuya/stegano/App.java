package be.valuya.stegano;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Hello world!
 *
 */
public class App {

    private static final String UTF8 = "UTF-8";
    private static final Charset CHARSET = Charset.forName(UTF8);

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void main(String[] args) {
        try {
            String toEncodeStr = "test 12345";
            byte[] toEncodeBytes = toEncodeStr.getBytes(CHARSET);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(toEncodeBytes);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream outputStream = new SteganoOutputStream(byteArrayOutputStream);
            copyStream(byteArrayInputStream, outputStream);
            String encodedStr = byteArrayOutputStream.toString(UTF8);
            System.out.println(encodedStr);

//            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, Cipher.getInstance("", ""));

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}
