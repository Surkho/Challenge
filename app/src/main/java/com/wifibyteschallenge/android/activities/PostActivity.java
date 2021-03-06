package com.wifibyteschallenge.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wifibyteschallenge.android.R;
import com.wifibyteschallenge.android.adapters.RecyclerPostAdapter;
import com.wifibyteschallenge.android.model.Posts;
import com.wifibyteschallenge.android.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    //FIREBASE OBJECTS AND VARIABLES
    private FirebaseUser fireUser;
    private FirebaseAuth fireAuth;
    private DatabaseReference fireReference;
    private final String USER_POST_PATH = "user-posts";
    private String USER_UID;

    //VIEW OBJECTS (USE OF BUTTERKNIFE)
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.postProgressBar)
    ProgressBar progressPost;
    @BindView(R.id.empty_list_layout)
    LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        //GET FIREBASE AUTH INSTANCE AND GET USERUID
        fireAuth = FirebaseAuth.getInstance();
        fireAuth.addAuthStateListener(fireAuthListener);
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        USER_UID = fireUser.getUid();
        //GET DATABAE REFERENCE AND CREATION OF LIST OF POST
        fireReference = FirebaseDatabase.getInstance().getReference();
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
    }

    //FUNCTION TO READ DATABASE AND CREATE LIST OF POSTS
    private  void readAndCreateListOfPosts(){
        progressPost.setVisibility(View.VISIBLE);
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
                //IF WE HAVE ERROR, HIDE THE PROGRESS AND CREATE A MESSAGE WITH THE ERROR CODE
                progressPost.setVisibility(View.GONE);
                createDatabaseErrorMessage(databaseError.getCode());
            }
        });
    }
    //METHOD TO BUILD THE RECYCLERVIEW WITH THE ARRAYLIST OF POSTS
    private void buildListOfPosts(List<Posts> posts) {
        progressPost.setVisibility(View.GONE);
        if(!posts.isEmpty()){
            RecyclerPostAdapter postAdapter = new RecyclerPostAdapter(posts);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(postAdapter);
        }else
            emptyView.setVisibility(View.VISIBLE);
    }

    //FUNCTION TO CREATE A MESSAGE WITH DATABASE ERROR.
    private void createDatabaseErrorMessage(int code) {
        switch (code) {
            case Utils.ERROR_DISCONECT:
                Toast.makeText(PostActivity.this, getString(R.string.error_firebase_database_disconnected), Toast.LENGTH_SHORT).show();
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

    //MENU ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                fireAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fireAuthListener != null) {
            fireAuth.removeAuthStateListener(fireAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        if(fireAuth.getCurrentUser() != null)
            fireAuth.signOut();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
