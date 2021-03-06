package com.bzahov.elsys.godofrowing.Fragments.AnalysisFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v4.app.Fragment;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Fragments.Dialogs.DatePickerDialog;
import com.bzahov.elsys.godofrowing.Models.MyLocation;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.Models.TrainingOverview;
import com.bzahov.elsys.godofrowing.R;
import com.bzahov.elsys.godofrowing.RowApplication;
import com.bzahov.elsys.godofrowing.Support.MathFunct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by bobo-pc on 5/28/2017.
 */

public class ResultContentHistoryFragment extends Fragment {
    private final String TAG = "HistoryActivity";

    private static final int DIALOG_REQUEST = 101;
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
    private RowApplication app = RowApplication.getInstance();


    private Query mQuery;
    private ArrayList<ResourcesFromActivity> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    private FirebaseRecyclerAdapter<ResourcesFromActivity, FbResVwHolder> mMyAdapter;
    private FloatingActionButton fab;
    private long range_startDate = 0;
    private long range_endDate = 0;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_result_history, container, false);
        setUpViews(v);
        checkUserAuth();
        setUpFabButton(v);
        setDataReference();
        handleInstanceState(savedInstanceState);
        setupRecyclerview();
        return v;
    }

    private void setUpViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        fab = (FloatingActionButton) v.findViewById(R.id.res_analysis_fab);
    }

    private void setDataReference() {
        Log.d("aa","saas");
        if (mAuth.getCurrentUser() == null) {
            mAuth.getCurrentUser().reload();
        } else {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            mListItemRef = database.getReference(app.getString(R.string.ref_database_users)).child(mUser.getUid()).child(app.getString(R.string.ref_database_activities));
            Log.e(TAG,mListItemRef.toString());
            mQuery = mListItemRef.limitToLast(25); //TODO Fix Mem Usage!!! eat too much memory
        }
    }

    private void checkUserAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {// User is signed in
                    mUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {// User is signed out
                    startActivity(new Intent(getActivity(), LogInActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }};
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerDialog pickerDialog = new DatePickerDialog();//.newInstance("Some Title");
        pickerDialog.setTargetFragment(this,DIALOG_REQUEST);
        pickerDialog.show(fm, "fragment_picker");

    }

    private void updateQuery() {
        String childName = app.getString(R.string.ref_database_sortby_currentTime);
        mQuery = mListItemRef.orderByChild(childName)
                .startAt(range_startDate)
                .endAt(range_endDate);
        Log.d("Date Query","" + mQuery.toString());
        setupRecyclerview();
    }

    private void setUpFabButton(View v) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {
        mAdapterItems = new ArrayList<ResourcesFromActivity>();
        mAdapterKeys = new ArrayList<String>();
    }

    private void setupFirebase() {
        //Firebase.setAndroidContext(this);
        //String firebaseLocation = getResources().getString(R.string.firebase_location);
        //mQuery = mListItemRef.limitToFirst(5);//database.getReference(); //TODO
    }

    private void setupRecyclerview() {
        if (mMyAdapter != null){
            mMyAdapter.cleanup();
        }
        mMyAdapter = new FirebaseRecyclerAdapter<ResourcesFromActivity, FbResVwHolder>(ResourcesFromActivity.class, R.layout.category_history_list_item, FbResVwHolder.class, mQuery) {

            @Override
            public void populateViewHolder(FbResVwHolder resourcesViewHolder, ResourcesFromActivity model, int position) {
                resourcesViewHolder.setKey(getRef(position).getKey());
                // delete unnecessary data
                model.setAllTrainLocations(new ArrayList<MyLocation>());

                TrainingOverview overView = model.getTrainingOverview();
                resourcesViewHolder.bindSportActivity(overView);
            }

            @Override
            public void onBindViewHolder(FbResVwHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
            }

            @Override
            public boolean onFailedToRecycleView(FbResVwHolder holder) {
                return super.onFailedToRecycleView(holder);
            }
        };
        recyclerView.setAdapter(mMyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMyAdapter != null) mMyAdapter.cleanup();
    }

    public static class FbResVwHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final int MAX_WIDTH = 200;
        private static final int MAX_HEIGHT = 200;
        private boolean childVisible;
        private RowApplication app = RowApplication.getInstance();
        RelativeLayout childLayout;
        View mView;
        String key = "";
        Context mContext;

        public FbResVwHolder(View itemView) {
            super(itemView);
            childVisible = false;
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            childLayout = (RelativeLayout) mView.findViewById(R.id.list_item_layout_container);
            childLayout.setVisibility(View.GONE);
        }

        public void bindSportActivity(TrainingOverview model) {
            TextView headerTextView = (TextView) mView.findViewById(R.id.start_date_head_text);
            headerTextView.setText(key + " | Workout:"+ model.getTotalMeters()+ " m");
            setAllParameters(model);
        }

        private void setAllParameters(TrainingOverview model) {
            Log.e("setAll",model.toString());
            setParameters(R.id.res_analysis_meters_total, 0, app.getString(R.string.text_result_distance), Long.toString(model.getTotalMeters()));
            setParameters(R.id.res_analysis_speed_average,0, app.getString(R.string.text_result_speed_ave), Float.toString(MathFunct.roundFloat(model.getAverageSpeed(),2)));
            setParameters(R.id.res_analysis_speed_max, 0, app.getString(R.string.text_result_speedPer500m_max), Float.toString(MathFunct.roundFloat(model.getMaxSpeed(),2)));
            setParameters(R.id.res_analysis_empty,R.drawable.icon_analysis,app.getString(R.string.text_result_StrokeRate_ave),Float.toString(model.getAverageStrokeRate()));
            setParameters(R.id.res_analysis_elapsed_time, 0, app.getString(R.string.text_result_duration), model.getElapsedTimeStr());
        }

        @Override
        public void onClick(View view) {
            if (childLayout.getVisibility() == View.VISIBLE){
                childVisible = false;
                childLayout.setVisibility(View.GONE);

            }else {
                childVisible = true;
                childLayout.setVisibility(View.VISIBLE);
            }
        }

        private void setParameters(int viewID, int imageID, @Nullable String name, String value){
            //RelativeLayout viewById = ((RelativeLayout) mView.findViewById(viewID));
            RelativeLayout viewById = (RelativeLayout)childLayout.findViewById(viewID);

            if (imageID != 0) {
                ImageView imageView = ((ImageView) viewById.findViewById(R.id.list_item_head_header));
                imageView.setImageResource(imageID);
            }
            if (name != null) {
                TextView nameView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_name));
                nameView.setText(name);
            }if (value != null){
                TextView  valueView = ((TextView) viewById.findViewById(R.id.start_date_head_text_date));
                valueView.setText(value);
            }
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    //unused
    public static class ResourcesHolder extends RecyclerView.ViewHolder {
        private final TextView mNameField;
        //private final TextView mTextField;

        public ResourcesHolder(View itemView) {
            super(itemView);
            mNameField = (TextView) itemView.findViewById(R.id.list_item_child_meters_total);
            //mTextField = (TextView) itemView.findViewById(android.R.id.);
        }

        public void setName(String name) {
            mNameField.setText(name);
        }

        public void setText(String text) {
            // mTextField.setText(text);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIALOG_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle.containsKey("isSuccessfully")) {
                    Boolean isSuccessfully = bundle.getBoolean("isSuccessfully");
                    Log.d("Date range","clicked done btn: "+isSuccessfully.toString());
                    if (isSuccessfully){
                        if (bundle.containsKey("startDate")){
                            range_startDate = bundle.getLong("startDate");
                            Log.d("Date range","start date:" + range_startDate);
                        }
                        if (bundle.containsKey("endDate")){
                            range_endDate = bundle.getLong("endDate");
                            Log.d("Date range","end date:" + range_endDate);
                        }
                        if (bundle.containsKey("showingNum")){
                            /*showingNum = bundle.getLong("showingNum");
                            Log.d("Date range","showingNum:" + showingNum);*/
                        }
                        updateQuery();
                    }
                }
            }
        }
    }
}
