package com.bzahov.elsys.godofrowing.Fragments.AnalysisFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Models.MyLocation;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;
import com.bzahov.elsys.godofrowing.RowApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by bobo-pc on 5/29/2017.
 */

public class ResultContentSplitterFragment extends Fragment implements ValueEventListener {
    private FirebaseAuth mAuth;
    private String TAG = "SplitterFrg";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference usersActivitiesRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private List<MyLocation> allLocations;
    private ResourcesFromActivity receivedData;
    private RowApplication app = RowApplication.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_result_analysis, container, false);

        ScrollView a = (ScrollView) v.findViewById(R.id.splitter_scrollview);

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
                    startActivity(new Intent(getActivity(), LogInActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        if (mAuth.getCurrentUser() == null) {
            mAuth.getCurrentUser().reload();
        }
        else{
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            usersActivitiesRef = database.getReference(app.getString(R.string.ref_database_users)).child(mUser.getUid()).child(app.getString(R.string.ref_database_activities));
            Query query = usersActivitiesRef.orderByChild(app.getString(R.string.ref_database_sortby_currentTime)).limitToLast(1);
            Log.e("Query getRef()",query.getRef().toString());
            query.addValueEventListener(this);
        }
        return v;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        receivedData = null;// = dataSnapshot.getValue(ResourcesFromActivity.class);
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            receivedData = activitySnapShot.getValue(ResourcesFromActivity.class);
        }
        if (receivedData == null) {
            return;
        }
/*
        Log.e("Query Ref",dataSnapshot.getRef().toString());
        Log.e("Query DataS","\n "+dataSnapshot.toString() );
        Log.e("Query getVal()","\n "+ receivedData.toString() );
        Log.e("Query","\n "+ Long.toString(receivedData.getTotalMeters()));
*/
//        Toast.makeText(getContext(),"onData" ,Toast.LENGTH_SHORT).show();
        allLocations = receivedData.getAllTrainLocations();

        //setAllValuesOfViews(receivedData);
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
