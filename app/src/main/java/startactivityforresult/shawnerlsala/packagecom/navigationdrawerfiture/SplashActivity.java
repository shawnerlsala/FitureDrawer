package startactivityforresult.shawnerlsala.packagecom.navigationdrawerfiture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ShawnErl on 01/09/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private TextView fiture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        fiture = (TextView) findViewById(R.id.Fiture);
//        fiture.setTypeface(EasyFonts.windSong(this));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,FacebookLoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}