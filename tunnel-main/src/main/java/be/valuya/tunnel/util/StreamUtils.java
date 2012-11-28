/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Yannick
 */
public class StreamUtils {

    public static final int BUFFER_SIZE = 1024;

    public static Thread copyStreamAsync(final InputStream inputStream, final OutputStream outputStream) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    copyStream(inputStream, outputStream);
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
        thread.start();
        return thread;
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE]; // Adjust if you want

        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead < 0) {
                break;
            }
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
