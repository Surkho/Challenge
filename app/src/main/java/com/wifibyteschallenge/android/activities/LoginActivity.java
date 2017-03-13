package com.wifibyteschallenge.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

    //FIREBASE OBJECT
    private FirebaseAuth fireAuth;

    //VIEW OBJECTS (USE OF BUTTERKNIFE)
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
        fireAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }
    /*  BUTTON LOGIN CLICK EVENT (BUTTERKNIFE)
    * HERE CHECKING USER AND PASS WITH VALID FORMAT AND CALL THE METHOD TO GET UID.*/
    @OnClick(R.id.btnLogin)
    public void OnClick(){
        //SHOW DIALOG
        progressLogin.setVisibility(View.VISIBLE);
        //GET USER AND PASS FROM EDITTEXT
        String user,pass;
        user = edUser.getText().toString();
        pass = edPassword.getText().toString();
        //CONDITION TO START THE LOGIN OR HIDE PROGRESSBAR AND SHOW ERROR MESSAGE ON THE EDITTEXT.
        if(Utils.isValidEmail(user) && Utils.isValidPassword(pass))
           startLoginWithFirebase(user,pass);
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
            if(firebaseAuth.getCurrentUser() != null)
                Log.e("Firebase","User logged");
            else
                Log.e("Firebase","User not logged");

        }
    };
    //METHOD TO START THE LOGIN WITH PASS AND USER
    private void startLoginWithFirebase(String user, String password){
        fireAuth.signInWithEmailAndPassword(user,password)
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
    //METHOD TO LAUNCH POST ACTIVITY WHEN THE LOGIN IS OK!
    private void launchPostActivity() {
        startActivity(new Intent(LoginActivity.this,PostActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuthListener != null) {
            fireAuth.removeAuthStateListener(fireAuthListener);
        }
    }
}
