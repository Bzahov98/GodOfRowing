package com.bzahov.elsys.godofrowing.AuthenticationActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by bobo-pc on 2/10/2017.
 */
public class LogInActivity extends Activity {

        private static final String TAG = "LoginActivity";
        private Button btnLogin, btnLinkToSignUp;
        private ProgressBar progressBar;
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private EditText loginInputEmail, loginInputPassword;
        private TextInputLayout loginInputLayoutEmail, loginInputLayoutPassword;
        private Switch anonymousSwitch;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            mAuth = FirebaseAuth.getInstance();

        setViews();

        setUpAuthListener();
    }

    private void setViews() {
        loginInputLayoutEmail = (TextInputLayout) findViewById(R.id.login_input_layout_email);
        loginInputLayoutPassword = (TextInputLayout) findViewById(R.id.login_input_layout_password);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        anonymousSwitch = (Switch) findViewById(R.id.login_switch_anonymous);

        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLinkToSignUp = (Button) findViewById(R.id.btn_link_signin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnLinkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    /**
         * Validating form
         */

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
        private void submitForm() {
            String email = loginInputEmail.getText().toString().trim();
            String password = loginInputPassword.getText().toString().trim();

            if (anonymousSwitch.isChecked()) {
                email = "anonymous@gmail.com";
                password = "anonymous";
            } else {
                if (!checkEmail()) {
                    return;
                }
                if (!checkPassword()) {
                    return;
                }
                loginInputLayoutEmail.setErrorEnabled(false);
                loginInputLayoutPassword.setErrorEnabled(false);

                progressBar.setVisibility(View.VISIBLE);
            }

            signIn(email, password);

        }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LogInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            Log.e(TAG, "signInWithEmail", task.getException());
                        } else {
                            Intent returnIntent = new Intent(); //+
                            //startActivity(intent);
                            returnIntent.putExtra("returnFormLogIn",false);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    }
                });
    }

    private boolean checkEmail() {
            String email = loginInputEmail.getText().toString().trim();
            if (email.isEmpty() || !isEmailValid(email)) {

                loginInputLayoutEmail.setErrorEnabled(true);
                loginInputLayoutEmail.setError(getString(R.string.err_msg_email));
                loginInputEmail.setError(getString(R.string.err_msg_required));
                requestFocus(loginInputEmail);
                return false;
            }
            loginInputLayoutEmail.setErrorEnabled(false);
            return true;
        }

        private boolean checkPassword() {

            String password = loginInputPassword.getText().toString().trim();
            if (password.isEmpty() || !isPasswordValid(password)) {

                loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
                loginInputPassword.setError(getString(R.string.err_msg_required));
                requestFocus(loginInputPassword);
                return false;
            }
            loginInputLayoutPassword.setErrorEnabled(false);
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

        @Override
        public void onBackPressed() {
            super.onBackPressed();
        }

    public void onAnonymouSwitchClick(View view) {
        if (anonymousSwitch.isChecked()) {
            loginInputLayoutEmail.setVisibility(View.INVISIBLE);
            loginInputLayoutPassword.setVisibility(View.INVISIBLE);
        } else {
            loginInputLayoutEmail.setVisibility(View.VISIBLE);
            loginInputLayoutPassword.setVisibility(View.VISIBLE);
        }
    }
}
