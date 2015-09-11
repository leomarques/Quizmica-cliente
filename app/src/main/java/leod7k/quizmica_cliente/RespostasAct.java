package leod7k.quizmica_cliente;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.respostas)
public class RespostasAct extends AppCompatActivity {

    @Click
    public void buttonA() {
        App.inst().enviarResposta("A");
        Toast.makeText(this, "Você respondeu 'A'", Toast.LENGTH_SHORT).show();
    }

    @Click
    public void buttonB() {
        App.inst().enviarResposta("B");
        Toast.makeText(this, "Você respondeu 'B'", Toast.LENGTH_SHORT).show();
    }

    @Click
    public void buttonC() {
        App.inst().enviarResposta("C");
        Toast.makeText(this, "Você respondeu 'C'", Toast.LENGTH_SHORT).show();
    }

    @Click
    public void buttonD() {
        App.inst().enviarResposta("D");
        Toast.makeText(this, "Você respondeu 'D'", Toast.LENGTH_SHORT).show();
    }

}
