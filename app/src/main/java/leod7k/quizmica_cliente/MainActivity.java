package leod7k.quizmica_cliente;

import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
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

    PrintWriter out = null;
    BufferedReader in = null;
    Socket socket = null;

    @Click
    public void button() {
        out.println("cliquei");
        out.flush();
    }

    @AfterViews
    public void aoCriar() {
        conectar();
    }

    @Background
    protected void conectar() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        String serverHostName = IpGetter.getIp(ip);

        try {
            // Connect to Server
            socket = new Socket(serverHostName, 2002);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            Log.i("jaba", "Connected to server " + serverHostName + ":"
                    + SERVER_PORT);
        } catch (IOException ioe) {
            Log.i("jaba", "Can not establish connection to " + serverHostName
                    + ":" + SERVER_PORT);
            ioe.printStackTrace();
            finish();
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
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
