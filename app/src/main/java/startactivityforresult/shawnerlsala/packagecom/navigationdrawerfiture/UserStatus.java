package startactivityforresult.shawnerlsala.packagecom.navigationdrawerfiture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by ShawnErl on 01/09/2017.
 */

public class UserStatus extends AppCompatActivity {
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status);
        Bundle inBundle = getIntent().getExtras();
        Intent intent = getIntent();


        imageUrl = inBundle.getString("imageUrl");

        ImageView circleImage = (ImageView) findViewById(R.id.circularImage);

        circleImage.setImageResource(R.drawable.ic_launcher_round);

//        Glide.with(this).load(imageUrl).centerCrop().into(circleImage);
//        Glide.with(UserStatus.this)
//                .load(imageUrl) // add your image url
//                .transform(new CircleTransform(UserStatus.this)) // applying the image transformer
//                .into(circleImage);
    }
}