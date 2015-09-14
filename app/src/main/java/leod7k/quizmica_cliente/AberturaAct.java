package leod7k.quizmica_cliente;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.abertura)
public class AberturaAct extends AppCompatActivity {

    @AfterViews
    public void afterViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callAct();
                finish();
            }
        }, 5000);
    }

    private void callAct() {
        MainActivity_.intent(this).start();
    }

}
