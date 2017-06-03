package com.bzahov.elsys.godofrowing.AuthenticationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.MainActivity;
import com.bzahov.elsys.godofrowing.Models.User;
import com.bzahov.elsys.godofrowing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bobo-pc on 2/11/2017.
 */
public class SignInActivity extends AppCompatActivity {

        private static final String TAG = "SignInActivity" ;
        private Button btnSignUp,btnLinkToLogIn;
        private ProgressBar progressBar;
        private FirebaseAuth auth;
        private EditText signupInputEmail, signupInputPassword;
        private TextInputLayout signupInputLayoutEmail, signupInputLayoutPassword;
        private EditText signupInputUsername;
        private EditText signupInputAge;
        private TextInputLayout signupInputLayoutUsername;
        private DatabaseReference mDatabase;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
        signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signup_input_layout_password);
        signupInputLayoutUsername = (TextInputLayout) findViewById(R.id.signup_input_layout_username);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputUsername = (EditText) findViewById(R.id.signup_input_username);
        signupInputAge = (EditText) findViewById(R.id.signup_input_age);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLinkToLogIn = (Button) findViewById(R.id.btn_link_login);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });

        btnLinkToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

        /**
         * Validating form
         */
    private void submitForm() {

        String email = signupInputEmail.getText().toString().trim();
        String password = signupInputPassword.getText().toString().trim();
        if(!checkEmail()) {
            return;
        }
        if(!checkPassword()) {
            return;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUserWithEmail:onComplete:" + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Log.d(TAG,"Authentication failed." + task.getException());

                        } else {
                            //startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            createNewUser(task.getResult().getUser());
                            finish();
                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
    }

    private void createNewUser(FirebaseUser userFromRegistration) {
        String uId = userFromRegistration.getUid();
        String username = signupInputUsername.getText().toString().trim();
        String password = signupInputPassword.getText().toString().trim();
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        String age = signupInputAge.getText().toString().trim();
        if (android.text.TextUtils.isDigitsOnly(age)) {

        }

        User user = new User(uId,username, email,password);

            mDatabase.child("users").child(userId).setValue(user);
    }

    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError(getString(R.string.err_msg_email));
            signupInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {

        String password = signupInputPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            signupInputLayoutPassword.setError(getString(R.string.err_msg_password));
            signupInputPassword.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}