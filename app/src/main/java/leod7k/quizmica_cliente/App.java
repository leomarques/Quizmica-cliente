package leod7k.quizmica_cliente;

import android.app.Application;

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

    public void enviar(String mensagem) {
        out.println(mensagem);
        out.flush();
    }

    public void enviarResposta(String resposta) {
        enviar("r;".concat(resposta));
    }

    public void enviarNome(String nome) {
        enviar("n;".concat(nome));
    }
}
