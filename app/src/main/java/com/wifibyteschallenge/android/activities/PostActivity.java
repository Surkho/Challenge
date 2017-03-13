package com.wifibyteschallenge.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wifibyteschallenge.android.R;
import com.wifibyteschallenge.android.model.Posts;
import com.wifibyteschallenge.android.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    private FirebaseUser fireUser;
    private DatabaseReference fireReference;
    private final String USER_POST_PATH = "user-posts";
    private String USER_UID;

    @BindView(R.id.recyclerView)
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        FirebaseAuth.getInstance().addAuthStateListener(fireAuthListener);
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        USER_UID = fireUser.getUid();

        fireReference = FirebaseDatabase.getInstance().getReference();
        if(fireReference!= null)
            readAndCreateListOfPosts();

    }
    //FIREBASE LISTENER
    private FirebaseAuth.AuthStateListener fireAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            //IF WE DONT HAVE USER, GO BACK TO SING IN
            if(firebaseAuth.getCurrentUser() == null)
                backToLoginScreen();
        }
    };
    //FUNCTION TO GO BACK TO LOGIN SCREEN IF CURRENTUSER IS NULL
    private void backToLoginScreen() {
        startActivity(new Intent(PostActivity.this,LoginActivity.class));
        finish();
        return;
    }

    //FUNCTION TO READ DATABASE AND CREATE LIST OF POSTS
    private  void readAndCreateListOfPosts(){
        fireReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Posts> posts = new ArrayList<Posts>();
                //For each to get data and create list with model Posts.
                for(DataSnapshot item : dataSnapshot.child(USER_POST_PATH).child(USER_UID).getChildren()){
                    Posts post = item.getValue(Posts.class);
                    posts.add(post);
                }
                buildListOfPosts(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                createDatabaseErrorMessage(databaseError.getCode());
            }
        });
    }
    //METHOD TO BUILD THE RECYCLERVIEW WITH THE ARRAYLIST OF POSTS
    private void buildListOfPosts(List<Posts> posts) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    //FUNCTION TO CREATE A MESSAGE WITH DATABASE ERROR.
    private void createDatabaseErrorMessage(int code) {
        switch (code) {
            default:
                Toast.makeText(PostActivity.this, getString(R.string.error_firebase_database_default), Toast.LENGTH_SHORT).show();
                break;
            case Utils.ERROR_NETWORK:
                Toast.makeText(PostActivity.this, getString(R.string.error_firebase_database_network), Toast.LENGTH_SHORT).show();
                break;
            case Utils.ERROR_UNAVALAIABLE:
                Toast.makeText(PostActivity.this, getString(R.string.error_firebase_database_unavaliable), Toast.LENGTH_SHORT).show();
                break;
            case Utils.ERROR_UNKNOW:
                Toast.makeText(PostActivity.this, getString(R.string.error_firebase_database_unknown), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
