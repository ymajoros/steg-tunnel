/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.stegano;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author Yannick
 */
public class SteganoOutputStream extends OutputStream {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static final int CYCLES;
    //
    private final Random random;
    private final OutputStream outputStream;

    static {
        CYCLES = WordByte.BYTE_WORDS.length / 256;
    }

    public SteganoOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        random = new Random();
    }

    @Override
    public void write(int b) throws IOException {
        int c = random.nextInt(CYCLES);
        String byteString = WordByte.BYTE_WORDS[b * c].toLowerCase(Locale.FRENCH) + " ";
        byte[] bytes = byteString.getBytes(CHARSET_UTF8);
        outputStream.write(bytes);
    }
}
