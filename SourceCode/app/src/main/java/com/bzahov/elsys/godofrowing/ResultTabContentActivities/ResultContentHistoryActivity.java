package com.bzahov.elsys.godofrowing.ResultTabContentActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by bobo-pc on 2/8/2017.
 */
@Deprecated
public class ResultContentHistoryActivity extends AppCompatActivity {
    //UNUSED //UNUSED!!! //UNUSED!!! //UNUSED!!! //UNUSED!!! //UNUSED!!!
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


    private Query mQuery;
    private ArrayList<ResourcesFromActivity> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    private FirebaseRecyclerAdapter<ResourcesFromActivity, FirebaseResViewHolder> mMyAdapter;

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
            }
        };

            mUser = FirebaseAuth.getInstance().getCurrentUser();
            mListItemRef = database.getReference("users").child(mUser.getUid()).child("activities") ;
            //myUserRef = database.getReference("message");

            Toast.makeText(getBaseContext(), mUser.getEmail(), Toast.LENGTH_SHORT).show();

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
        mQuery = mListItemRef;//database.getReference();
    }

    private void setupRecyclerview() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mMyAdapter = new HistoryAdapter(mQuery, ResourcesFromActivity.class, mAdapterItems, mAdapterKeys);

        mMyAdapter = new FirebaseRecyclerAdapter<ResourcesFromActivity, FirebaseResViewHolder>(ResourcesFromActivity.class, R.layout.category_history_list_item, FirebaseResViewHolder.class, mListItemRef) {

            @Override
            public void populateViewHolder(FirebaseResViewHolder resourcesViewHolder, ResourcesFromActivity resourcesFromActivity, int position) {
               // resourcesViewHolder.setName(resourcesFromActivity.getCurrentTime());
                Log.e("holder",position+ " " + resourcesFromActivity.getTrainingOverview().getCurrentTime() + "\n" + resourcesFromActivity.toString());
                resourcesViewHolder.setKey(getRef(position).getKey());
                resourcesViewHolder.bindSportActivity(resourcesFromActivity);
  //              ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new
//                        CallBack(0, ItemTouchHelper.RIGHT, mMyAdapter); // Making the SimpleCallback

//                ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);

                //touchHelper.attachToRecyclerView(recyclerView);

            }

            @Override
            public void onBindViewHolder(FirebaseResViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                //viewHolder.setText("aaa");
            }

            @Override
            public boolean onFailedToRecycleView(FirebaseResViewHolder holder) {
                Log.e("ohh","aaa");
                return super.onFailedToRecycleView(holder);
            }
        };
        recyclerView.setAdapter(mMyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        mMyAdapter.cleanup();
    }

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

    public static class FirebaseResViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final int MAX_WIDTH = 200;
        private static final int MAX_HEIGHT = 200;
        private boolean childVisible;
        RelativeLayout childLayout;
        View mView;
        String key = "";
        Context mContext;

        public FirebaseResViewHolder(View itemView) {
            super(itemView);
            childVisible = false;
            mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            childLayout = (RelativeLayout) mView.findViewById(R.id.list_item_child_layout);
        }

        public void bindSportActivity(ResourcesFromActivity model) {
            //ImageView restaurantImageView = (ImageView) mView.findViewById(R.id.restaurantImageView);
            TextView totalMeters = (TextView) childLayout.findViewById(R.id.list_item_child_meters_total);
            MapView map = (MapView) childLayout.findViewById(R.id.res_analysis_map);
            TextView aaa = (TextView) childLayout.findViewById(R.id.res_analysis_empty);


            TextView headerTextView = (TextView) mView.findViewById(R.id.start_date_head_text);
            //TextView ratingTextView = (TextView) mView.findViewById(R.id.ratingTextView);
            TextView childMetersView = (TextView) mView.findViewById(R.id.start_date_head_text);
            RelativeLayout first = (RelativeLayout) mView.findViewById(R.id.list_item_layout_container);

            headerTextView.setText(key);
            totalMeters.setText(Long.toString(model.getTrainingOverview().getTotalMeters()));
            map.setVisibility(View.GONE);

            setParameters(R.id.list_item_layout_container,0,"Distance(m):",Long.toString(model.getTrainingOverview().getTotalMeters()));
            setParameters(R.id.list_item_layout_container,0,"Distance(m):",Long.toString(model.getTrainingOverview().getTotalMeters()));


  //          categoryTextView.setText(restaurant.getCategories().get(0));
    //        ratingTextView.setText("Rating: " + restaurant.getRating() + "/5");
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
            /*final ArrayList<Restaurant> restaurants = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESTAURANTS);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        restaurants.add(snapshot.getValue(Restaurant.class));
                    }

                    int itemPosition = getLayoutPosition();

                    Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                    intent.putExtra("position", itemPosition + "");
                    intent.putExtra("restaurants", Parcels.wrap(restaurants));

                    mContext.startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/
        }

        private void setParameters( int viewID, int imageID, @Nullable String name, String value){
            RelativeLayout viewById = ((RelativeLayout) mView.findViewById(viewID));
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

    public class CallBack extends ItemTouchHelper.SimpleCallback {


        private FirebaseRecyclerAdapter adapter; // this will be your recycler adapter

        private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        public CallBack(int dragDirs, int swipeDirs, FirebaseRecyclerAdapter adapter) {
            super(dragDirs, swipeDirs);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition(); // this is how you can get the position
           // ResourcesFromActivity object = adapter.getItem(position); // You will have your own class ofcourse.

            Toast.makeText(getBaseContext(),"jj",Toast.LENGTH_SHORT).show();
            Log.e("gh",""+position);
            // then you can delete the object
           // root.child("Object").child(object.getId()).setValue(null);// setting the value to null will just delete it from the database.

        }

}
}