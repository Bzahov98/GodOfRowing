package com.bzahov.elsys.godofrowing.ResultTabContentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;
import com.bzahov.elsys.godofrowing.RecyclerView.HistoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentHistoryActivity extends AppCompatActivity {

    private final String TAG = "HistoryActivity";


    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private RecyclerView mListItemsRecyclerView;
    //private ListItemsAdapter mAdapter;
    private ArrayList<ResourcesFromActivity> myListItems;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference myUserRef;


    private Query mQuery;
    private HistoryAdapter mMyAdapter;
    private ArrayList<ResourcesFromActivity> mAdapterItems;
    private ArrayList<String> mAdapterKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_history);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    startActivity(new Intent(ResultContentHistoryActivity.this, LogInActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        if (mAuth.getCurrentUser() == null) {
            mAuth.getCurrentUser().reload();
            Toast.makeText(getBaseContext(), "WTF", Toast.LENGTH_SHORT).show();
        } else {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            mListItemRef = database.getReference("users").child(mUser.getUid()).child("activities").getRef() ;
            //myUserRef = database.getReference("message");

            Toast.makeText(getBaseContext(), mUser.getEmail(), Toast.LENGTH_SHORT).show();
        }
        handleInstanceState(savedInstanceState);
        setupFirebase();
        setupRecyclerview();
    }

    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {

            mAdapterItems = new ArrayList<ResourcesFromActivity>();
            mAdapterKeys = new ArrayList<String>();
    }

    private void setupFirebase() {
        //Firebase.setAndroidContext(this);
        //String firebaseLocation = getResources().getString(R.string.firebase_location);
        mQuery = database.getReference();
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mMyAdapter = new HistoryAdapter(mQuery, ResourcesFromActivity.class, mAdapterItems, mAdapterKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


 /*       mMyAdapter = new FirebaseRecyclerAdapter<ResourcesFromActivity, ResourcesHolder>(ResourcesFromActivity.class, android.R.layout.cat, ResourcesHolder.class, mListItemRef) {
            @Override
            public ResourcesFromActivity onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(ResourcesFromActivity holder, int position) {

            }

            @Override
            public void populateViewHolder(ResourcesHolder chatMessageViewHolder, ResourcesFromActivity chatMessage, int position) {
                chatMessageViewHolder.setName(chatMessage.getCurrentTime());
                chatMessageViewHolder.setText(chatMessage.getElapsedTimeString());
            }
        };
        recyclerView.setAdapter(mMyAdapter);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    // Saving the list of items and keys of the items on rotation

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyAdapter.destroy();
    }

    public static class ResourcesHolder extends RecyclerView.ViewHolder {
        private final TextView mNameField;
        private final TextView mTextField;

        public ResourcesHolder(View itemView) {
            super(itemView);
            mNameField = (TextView) itemView.findViewById(android.R.id.text1);
            mTextField = (TextView) itemView.findViewById(android.R.id.text2);
        }

        public void setName(String name) {
            mNameField.setText(name);
        }

        public void setText(String text) {
            mTextField.setText(text);
        }
    }
}