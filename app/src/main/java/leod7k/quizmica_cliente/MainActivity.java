package leod7k.quizmica_cliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int SERVER_PORT = 2002;

    BufferedReader in;
    Socket socket;
    Receiver receiver;

    @ViewById
    Button conectar_bt;

    @ViewById
    EditText nome_et;

    @AfterViews
    public void afterViews() {
        App.setIn(false);
        nome_et.setText(App.getNome());
    }

    @Click
    public void conectar_bt() {
        String nome = nome_et.getText().toString();
        if (nome.isEmpty())
            return;
        App.inst().nome = nome;

        new AutoConectaAsync(this).execute();
    }

    private class AutoConectaAsync extends AsyncTask<Void, Void, Boolean> {
        private final Activity act;
        private ProgressDialog pd;

        AutoConectaAsync(Activity paramAct) {
            act = paramAct;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(act, "", "Conectando", true, true);
        }

        protected Boolean doInBackground(Void... arg0) {
            String ip = App.pegaUltimoIP();
            if (!ip.isEmpty() && conectar(ip))
                return true;
            ip = pegarIp();
            return ip != null && conectar(ip);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();

            if (result) {
                Toast.makeText(act, "Conectado", Toast.LENGTH_LONG).show();
                somebotoes();
                App.salvaNome();
            } else {
                final EditText input = new EditText(act);
                input.setText("192.168.0.13");
                new AlertDialog.Builder(act)
                        .setTitle("Digite o IP do servidor")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new ConectaAsync(act, input.getText().toString()).execute();
                            }
                        })
                        .setNegativeButton("Cancelar", null).show();
            }
        }
    }

    private class ConectaAsync extends AsyncTask<Void, Void, Boolean> {
        private final Activity act;
        private final String ip;
        private ProgressDialog pd;

        ConectaAsync(Activity paramAct, String paramIp) {
            act = paramAct;
            ip = paramIp;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(act, "", "Conectando", true, true);

        }

        protected Boolean doInBackground(Void... arg0) {
            return conectar(ip);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();

            if (result) {
                somebotoes();
                App.salvaNome();
            }
            Toast.makeText(act, result ? "Conectado" : "Servidor não encontrado", Toast.LENGTH_LONG).show();
        }
    }

    private void somebotoes() {
        nome_et.setVisibility(View.GONE);
        conectar_bt.setVisibility(View.GONE);
    }

    private String pegarIp() {
        String ip = pegarProprioIp();

        return IpGetter.getIp(ip);
    }

    private String pegarProprioIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        App.inst().ip = ip;
        return ip;
    }

    private boolean conectar(String ip) {
        try {
            // Connect to Server
            socket = new Socket(ip, SERVER_PORT);

            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            App.inst().initOut(new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")));
            Log.i("jaba", "Connected to server " + ip + ":"
                    + SERVER_PORT);
        } catch (Exception ioe) {
            Log.i("jaba", "Can not establish connection to " + ip
                    + ":" + SERVER_PORT);
            return false;
        }

        receiver = new Receiver(this, in);
        //receiver.setDaemon(true);
        receiver.start();

        App.inst().enviarAuth();

        int r = 0;
        do {
            SystemClock.sleep(2000);
        } while (r++ < 3 && !App.getIn());

        if (App.getIn()) {
            App.salvarUltimoIP(ip);
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (receiver != null)
                receiver.interrupt();
            App.inst().closeOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
