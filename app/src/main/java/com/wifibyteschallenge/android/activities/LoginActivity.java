package com.wifibyteschallenge.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.wifibyteschallenge.android.R;
import com.wifibyteschallenge.android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    @BindView(R.id.edPassword)
    EditText edPassword;
    @BindView(R.id.edUser)
    EditText edUser;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fbAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }
    //BUTTON LOGIN CLICK EVENT
    @OnClick(R.id.btnLogin)
    public void OnClick(){
        //CHECKING USER AND PASS WITH VALID FORMAT AND CALL THE METHOD TO GET UID.
        progressLogin.setVisibility(View.VISIBLE);
        String user,pass;
        user = edUser.getText().toString();
        pass = edPassword.getText().toString();

        if(Utils.isValidEmail(user) && Utils.isValidPassword(pass))
           getUserUID(user,pass);
        else{
            progressLogin.setVisibility(View.GONE);
            if (!Utils.isValidEmail(user) && !Utils.isValidEmail(pass))
                edUser.setError(getString(R.string.error_login_email));
            if (!Utils.isValidPassword(pass))
                edPassword.setError(getString(R.string.error_login_password));
        }

    }

    //FIREBASE LISTENER AND FUNCTIONS TO FIREBAE AND OVERRIDE METHODS
    private FirebaseAuth.AuthStateListener fireAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        }
    };
    private void getUserUID(String user,String password){
        fbAuth.signInWithEmailAndPassword(user,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //IF THE VALIDATION IS OK, LAUNCH THE NEW ACTIVITY TO SHOW THE POSTS
                        if(task.isSuccessful()){
                            progressLogin.setVisibility(View.GONE);
                            launchPostActivity();
                        }else{
                            //THROW THE EXCEPTION AND SHOW MESSAGE FOR WRONG EMAIL OR WRONG PASSWORD
                            try {
                                progressLogin.setVisibility(View.GONE);
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                edPassword.setError(getString(R.string.error_firebase_pass));
                            } catch(FirebaseAuthInvalidUserException e) {
                                edUser.setError(getString(R.string.error_firebase_user));
                            } catch(Exception e) {
                                Log.e("Firebase Exception", e.getMessage());
                            }
                        }
                    }
                });
    }

    private void launchPostActivity() {
        startActivity(new Intent(LoginActivity.this,PostActivity.class));
        finish();
        return;
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(fireAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuthListener != null) {
            fbAuth.removeAuthStateListener(fireAuthListener);
        }
    }
}
