package edu.gwu.buzzify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private CardView mBtnSignIn;
    private Button mBtnRegister;
    private String mUserName;
    private String mPassword;
    private static final String TAG = "LoginActivity";

    private boolean credsAreValid() {
        if (!(mUserName.matches(getResources().getString(R.string.username)) && mPassword.matches(getResources().getString(R.string.password)))){
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
        /*

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == mBtnSignIn) {
                    TextView userText = (TextView) findViewById(R.id.etUsername);
                    mUserName = userText.getText().toString();

                    TextView passwordText = (TextView) findViewById(R.id.etPassword);
                    mPassword = passwordText.getText().toString();

                    //Check for correct login credentials. Display error if invalid.
                    if (!(mUserName.matches(getResources().getString(R.string.username)) && mPassword.matches(getResources().getString(R.string.password)))){
                        Toast.makeText(LoginActivity.this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();
                    }

                    //Change to location view.
                    Intent intentLocation = new Intent(LoginActivity.this, LoginActivity.class);
                    LoginActivity.this.startActivity(intentLocation);
                }

            }
        });

        */
    }


    public void onClick(View view){
        Log.d(TAG, "onClick called");
    }
}
