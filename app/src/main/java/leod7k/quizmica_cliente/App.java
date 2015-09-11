package leod7k.quizmica_cliente;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.PrintWriter;

public class App extends Application {

    static App inst;

    private PrintWriter out;
    String ip, nome;

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public void initOut(PrintWriter printWriter) {
        out = printWriter;
    }

    private void enviar(String mensagem) {
        out.println(mensagem);
        out.flush();
    }

    public void enviarResposta(String resposta) {
        enviar("r;".concat(resposta));
    }

    public void closeOut() {
        out.close();
    }

    public void enviarAuth() {
        enviar("a;".concat(nome));
    }

    public static void setIn(Boolean value) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("in", value);
        editor.apply();
    }

    public static boolean getIn() {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        return mSettings.getBoolean("in", false);
    }

    public static String getNome() {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        return mSettings.getString("nome", "");
    }

    public static void salvaNome() {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("nome", inst().nome);
        editor.apply();
    }

    public static String pegaUltimoIP() {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        return mSettings.getString("ultmoip", "");
    }

    public static void salvarUltimoIP(String ip) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(App.inst());
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("ultmoip", ip);
        editor.apply();
    }
}
