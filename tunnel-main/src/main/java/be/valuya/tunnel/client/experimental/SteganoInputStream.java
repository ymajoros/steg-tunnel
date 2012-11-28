/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client.experimental;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Yannick
 */
public class SteganoInputStream extends InputStream {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static final Map<String, Byte> byteMap = new HashMap<>();
    //
    private final InputStream wrappedInputStream;

    static {
        byte b = 0;
        for (String byteWord : WordByte.BYTE_WORDS) {
            byteMap.put(byteWord, b);
            b++;
        }
    }

    public SteganoInputStream(InputStream wrappedInputStream) {
        this.wrappedInputStream = wrappedInputStream;
    }

    @Override
    public int read() throws IOException {
        byte[] byteBuffer = new byte[4];
        int read = wrappedInputStream.read(byteBuffer);
        if (read != 5) {
            return -1;
        }
        String byteString = new String(byteBuffer, CHARSET_UTF8).trim().toUpperCase(Locale.FRENCH);
        return byteMap.get(byteString);
    }
}
