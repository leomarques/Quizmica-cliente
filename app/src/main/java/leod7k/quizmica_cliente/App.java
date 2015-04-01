package leod7k.quizmica_cliente;

import android.app.Application;

import java.io.PrintWriter;

public class App extends Application {

    static App inst;

    PrintWriter out;
    String ip;

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }
}
