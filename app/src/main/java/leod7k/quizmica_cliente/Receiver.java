package leod7k.quizmica_cliente;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class Receiver extends Thread {
    private final MainActivity mainActivity;
    private BufferedReader in;

    public Receiver(MainActivity paramMainActivity, BufferedReader paramIn) {
        mainActivity = paramMainActivity;
        in = paramIn;
    }

    public void run() {
        try {
            // Read messages from the server and print them
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("in")) {
                    App.setIn(true);
                }

                if (message.contains("comecou")) {
                    RespostasAct_.intent(mainActivity).start();
                }

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