package leod7k.quizmica_cliente;

import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.respostas)
public class RespostasAct extends ActionBarActivity {

    @Click
    public void buttonA() {
        App.inst().out.println(App.inst.ip + " respondeu 'A'");
        App.inst().out.flush();
    }

    @Click
    public void buttonB() {
        App.inst().out.println(App.inst.ip + " respondeu 'B'");
        App.inst().out.flush();
    }

    @Click
    public void buttonC() {
        App.inst().out.println(App.inst.ip + " respondeu 'C'");
        App.inst().out.flush();
    }

    @Click
    public void buttonD() {
        App.inst().out.println(App.inst.ip + " respondeu 'D'");
        App.inst().out.flush();
    }

}
