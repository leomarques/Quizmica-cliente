package leod7k.quizmica_cliente;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class Receiver extends Thread {
    private BufferedReader in;

    public Receiver(BufferedReader paramIn) {
        in = paramIn;
    }

    public void run() {
        try {
            // Read messages from the server and print them
            String message;
            while ((message = in.readLine()) != null) {
                Log.i("jaba", message + "\n");
            }
        } catch (SocketException ioe) {
            //Client quit, close thread
            return;
        } catch (IOException ioe) {
            Log.i("jab", "Connection to server broken.\n");
        }
        Log.i("jab", "Server closed.\n");
    }
}