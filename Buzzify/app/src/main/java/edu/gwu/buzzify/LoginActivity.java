package edu.gwu.buzzify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;

public class LoginActivity extends AppCompatActivity {
    private CardView mBtnSignIn;
    private CardView mBtnRegister;
    private String mUserName;
    private String mPassword;
    private static final String TAG = "LoginActivity";

    private boolean credsAreValid() {

        String validUser = getResources().getString(R.string.username);
        String validPass = getResources().getString(R.string.password);

        if ((mUserName.matches(validUser) && mPassword.matches(validPass))) {
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnSignIn = (CardView) findViewById(R.id.btnSignIn);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                mPassword = ((EditText) findViewById(R.id.etPassword)).getText().toString();

                if (credsAreValid()) {
                    //Change to location view.
                    Intent intentLocation = new Intent(LoginActivity.this, LocationActivity.class);
                    LoginActivity.this.startActivity(intentLocation);

                } else {
                    Toast.makeText(LoginActivity.this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnRegister = (CardView) findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, R.string.register_error_msg, Toast.LENGTH_SHORT).show();
            }
        });
        
    }


    public void onClick(View view){
        Log.d(TAG, "onClick called");
    }
}
