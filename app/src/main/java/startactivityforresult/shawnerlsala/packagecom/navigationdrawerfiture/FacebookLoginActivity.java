package startactivityforresult.shawnerlsala.packagecom.navigationdrawerfiture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ShawnErl on 01/09/2017.
 */

public class FacebookLoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;


    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String firstName, lastName, email, birthday, gender;
    private String profilePicture;
    private String userName;
    private TextView appName;
    private String TAG = "FacebookLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);


        appName = (TextView) findViewById(R.id.nameOfApp);
        appName.setTypeface(EasyFonts.windSong(this));

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

//        loginButton.setReadPermissions("email", "user_birthday","user_posts","user_photos","public_profile");
        loginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_posts", "user_photos", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG, object.toString());
                        Log.e(TAG, response.toString());

                        try {
                            userName = object.getString("id");
//                            profilePicture = new URL("http://graph.facebook.com/" + userName + "/picture?width=500&height=500");
                            profilePicture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.d("deceree", profilePicture);
                            if (object.has("first_name"))
                                firstName = object.getString("first_name");
                            if (object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");

                            Intent main = new Intent(FacebookLoginActivity.this, MainActivity.class);
                            main.putExtra("name", firstName);
                            main.putExtra("surname", lastName);
                            main.putExtra("birthday", birthday);
                            main.putExtra("gender", gender);
                            main.putExtra("email", email);
                            main.putExtra("userID", userName);
                            main.putExtra("imageUrl", profilePicture);
                            startActivity(main);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListner);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}