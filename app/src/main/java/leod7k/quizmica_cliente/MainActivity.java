package leod7k.quizmica_cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.EditText;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    public static final int SERVER_PORT = 2002;

    @Click
    public void btnConectar() {
        String ip = pegarIp();
        if (ip == null) {
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Digite o IP do servidor")
                    .setView(input)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    conectar(input.getText().toString());
                                }})
                    .setNegativeButton("Cancelar", null).show();
        } else {
            conectar(ip);
        }

        //out.println("cliquei");
        //out.flush();
    }

    private String pegarIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return IpGetter.getIp(ip);
    }

    @Background
    protected void conectar(String ip) {
        PrintWriter out;
        BufferedReader in;
        Socket socket;

        try {
            // Connect to Server
            socket = new Socket(ip, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            Log.i("jaba", "Connected to server " + ip + ":"
                    + SERVER_PORT);
        } catch (IOException ioe) {
            Log.i("jaba", "Can not establish connection to " + ip
                    + ":" + SERVER_PORT);
            return;
        }

        Receiver receiver = new Receiver(in);
        //receiver.setDaemon(true);
        receiver.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            //socket.close();
            //in.close();
            //out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
