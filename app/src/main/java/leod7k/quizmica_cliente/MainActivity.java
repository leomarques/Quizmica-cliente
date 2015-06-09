package leod7k.quizmica_cliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    public static final int SERVER_PORT = 2002;

    BufferedReader in;
    Socket socket;

    @ViewById
    EditText editText;

    @Click
    public void btnConectar() {
        App.inst().nome = editText.getText().toString();

        final EditText input = new EditText(this);
        input.setText("192.168.0.13");
        final Activity act = this;
        new AlertDialog.Builder(this)
                .setTitle("Digite o IP do servidor")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new ConectaAsync(act, input.getText().toString()).execute();
                    }
                })
                .setNegativeButton("Cancelar", null).show();
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
            String ip = pegarIp();
            return ip != null && conectar(ip);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();

            if (result) {
                Toast.makeText(act, "Conectou", Toast.LENGTH_SHORT).show();

                RespostasAct_.intent(act).start();
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
            pegarProprioIp();
            return conectar(ip);
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();

            if (result) {
                RespostasAct_.intent(act).start();

                App.inst().enviarNome(App.inst.nome);
            }

            Toast.makeText(act, result ? "Conectou" : "Servidor não encontrado", Toast.LENGTH_SHORT).show();
        }
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
                    socket.getOutputStream())));
            Log.i("jaba", "Connected to server " + ip + ":"
                    + SERVER_PORT);
        } catch (IOException ioe) {
            Log.i("jaba", "Can not establish connection to " + ip
                    + ":" + SERVER_PORT);
            return false;
        }

        Receiver receiver = new Receiver(in);
        //receiver.setDaemon(true);
        receiver.start();

        return true;
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
