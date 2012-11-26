package be.valuya.stegano;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Hello world!
 *
 */
public class ServletClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            String header = "POST /tn/echo HTTP/1.1\n"
                    + "host: localhost\n"
                    + "Content-Type: multipart/form-data\n"
                    + "Content-Length: 10\n"
                    + "Accept: */*\n\n"
                    + "0123456789\n";
            outputStream.write(header.getBytes(Charset.forName("UTF-8")));
//            Thread copyInputToRemoteThread = StreamUtils.copyStreamAsync(inputStream, System.out);
            tempCopyStream(inputStream, System.out);
//            copyInputToRemoteThread.interrupt();
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void tempCopyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want

        while (true) {
            int available = inputStream.available();
            available = Math.max(available, 1);
            available = Math.min(available, 1024);
            int bytesRead = inputStream.read(buffer, 0, available);
            if (bytesRead < 0) {
                break;
            }
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
