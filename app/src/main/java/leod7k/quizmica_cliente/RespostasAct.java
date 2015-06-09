package leod7k.quizmica_cliente;

import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.respostas)
public class RespostasAct extends ActionBarActivity {

    @Click
    public void buttonA() {
        App.inst().enviarResposta("A");
    }

    @Click
    public void buttonB() {
        App.inst().enviarResposta("B");
    }

    @Click
    public void buttonC() {
        App.inst().enviarResposta("C");
    }

    @Click
    public void buttonD() {
        App.inst().enviarResposta("D");
    }

}
