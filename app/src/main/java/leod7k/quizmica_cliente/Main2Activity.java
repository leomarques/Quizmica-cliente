package leod7k.quizmica_cliente;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main2)
public class Main2Activity extends ActionBarActivity {

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
