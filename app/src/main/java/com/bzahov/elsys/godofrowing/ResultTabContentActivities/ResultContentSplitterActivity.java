package com.bzahov.elsys.godofrowing.ResultTabContentActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bzahov.elsys.godofrowing.R;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentSplitterActivity extends Activity{

   /* private static final String TAG = class.getSimpleName();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText addTaskBox;
    private DatabaseReference databaseReference;
    private List<ResourcesFromActivity> allTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_splitter);
        allTask = new ArrayList<ResourcesFromActivity>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addTaskBox = (EditText)findViewById(R.id.add_task_box);
        recyclerView = (RecyclerView)findViewById(R.id.task_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Button addTaskButton = (Button)findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredTask = addTaskBox.getText().toString();
                if(TextUtils.isEmpty(enteredTask)){
                    Toast.makeText(MainActivity.this, "You must enter a task first", Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredTask.length() < 6){
                    Toast.makeText(MainActivity.this, "Task count must be more than 6", Toast.LENGTH_LONG).show();
                    return;
                }
                Task taskObject = new Task(enteredTask);
                databaseReference.push().setValue(taskObject);
                addTaskBox.setText("");
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                taskDeletion(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void getAllTask(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            String taskTitle = singleSnapshot.getValue(String.class);
            allTask.add(new Task(taskTitle));
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allTask);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
    private void taskDeletion(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for(int i = 0; i < allTask.size(); i++){
                if(allTask.get(i).getTask().equals(taskTitle)){
                    allTask.remove(i);
                }
            }
            Log.d(TAG, "Task tile " + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allTask);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }*/
/**************************

 package com.bzahov.elsys.godofrowing;

 import android.content.DialogInterface;
 import android.content.Intent;
 import android.os.Bundle;
 import android.support.annotation.NonNull;
 import android.support.design.widget.FloatingActionButton;
 import android.support.v7.app.AlertDialog;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.support.v7.widget.StaggeredGridLayoutManager;
 import android.support.v7.widget.Toolbar;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.animation.Animation;
 import android.view.animation.ScaleAnimation;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.RatingBar;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
 import com.bzahov.elsys.godofrowing.Model.ResourcesFromActivity;
 import com.firebase.ui.database.FirebaseRecyclerAdapter;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.database.ChildEventListener;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;

 import static com.google.android.gms.plus.PlusOneDummyView.TAG;

 /**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentHistoryActivity extends AppCompatActivity {

    /*private final String TAG = "HistoryActivity";

    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private RecyclerView mListItemsRecyclerView;
    private ListItemsAdapter mAdapter;
    private ArrayList<ResourcesFromActivity> myListItems;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference myUserRef;

*/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_history);
/*
        mDB = FirebaseDatabase.getInstance().getReference();
        myListItems = new ArrayList<>();
        mListItemsRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mListItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();

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
        mListItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllTask(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*        mListItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG + "Added", dataSnapshot.getValue(ResourcesFromActivity.class).toString() + "\n" + s);
               fetchData(dataSnapshot);
               getAllTask(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
           //     getAllTask(dataSnapshot);
                Log.d(TAG + "Changed", dataSnapshot.getValue(ResourcesFromActivity.class).toString());
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG + "Removed", dataSnapshot.getValue(ResourcesFromActivity.class).toString());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG + "Moved", dataSnapshot.getValue(ResourcesFromActivity.class).toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG + "Cancelle", databaseError.toString());
            }
        });*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all:
                deleteAllListItems();
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewListItem() {
        // Create new List Item  at /listItem
        Toast.makeText(getBaseContext(), "aawdaw ", Toast.LENGTH_SHORT);
        /*
        final String key = FirebaseDatabase.getInstance().getReference().child("listItem").push().getKey();
        LayoutInflater li = LayoutInflater.from(this);
        View getListItemView = li.inflate(R.layout.dialog_get_list_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(getListItemView);

        final EditText userInput = (EditText) getListItemView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        String listItemText = userInput.getText().toString();
                        ListItem listItem = new ListItem(listItemText);
                        Map<String, Object> listItemValues = listItem.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/listItem/" + key, listItemValues);
                        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

                    }
                }).create()
                .show();*/
    }
/*
    public void deleteAllListItems() {
       myListItems.clear();
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this,"Items Deleted Successfully",Toast.LENGTH_SHORT).show();*/
    }

    /*private void fetchData(DataSnapshot dataSnapshot) {
        Log.e(TAG,"GetParAllCount: "+Long.toString(dataSnapshot.getChildrenCount()) + "\n");
        ResourcesFromActivity listItem=dataSnapshot.getValue(ResourcesFromActivity.class);
        myListItems.add(listItem);
        updateUI();
    }

    private void getAllTask(DataSnapshot dataSnapshot){

        Log.d(TAG, "GetParAllCount "  + Long.toString(dataSnapshot.getChildrenCount()) + "\n");
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Log.d(TAG, "GetAllCount "  + Long.toString(singleSnapshot.getChildrenCount()) + "\n");
            Log.d(TAG, "\nGetAllValue: "  + singleSnapshot.toString() + "\n");
            ResourcesFromActivity taskTitle = singleSnapshot.getValue(ResourcesFromActivity.class);
            myListItems.add(taskTitle);
            updateUI();
        }
    }

    private void updateUI(){
        mAdapter = new ListItemsAdapter(myListItems);
        mListItemsRecyclerView.setAdapter(mAdapter);
    }

    private class ListItemsHolder extends RecyclerView.ViewHolder{

        public TextView mNameTextView;
        public ListItemsHolder(final View itemView){
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.textview_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //   Toast.makeText(getBaseContext(),"onClick!!!",Toast.LENGTH_SHORT).show();
                    RelativeLayout child = (RelativeLayout) itemView.findViewById(R.id.list_item_child);;
                    if (child.getVisibility() != View.VISIBLE) {
                        child.setVisibility(View.VISIBLE);
                    }else {
                        child.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }

        public void bindData(ResourcesFromActivity s){
            mNameTextView.setText(Long.toString((s.getElapsedTimeLong())));

        }
    }

    private class ListItemsAdapter extends RecyclerView.Adapter<ListItemsHolder>{
        private ArrayList<ResourcesFromActivity> mListItems;
        public ListItemsAdapter(ArrayList<ResourcesFromActivity> ListItems){
            mListItems = ListItems;

        }

        @Override
        public ListItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ResultContentHistoryActivity.this);
            View view = layoutInflater.inflate(R.layout.category_history_list_item,parent,false);
            return new ListItemsHolder(view);

        }

        @Override
        public void onBindViewHolder(ListItemsHolder holder, int position) {

            ResourcesFromActivity s = mListItems.get(position);
            holder.bindData(s);

        }

        @Override
        public int getItemCount() {
            return mListItems.size();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null){
            mUser = mAuth.getCurrentUser();
        }else Toast.makeText(getBaseContext(),"WTF >>>>>>",Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null){
            mUser = mAuth.getCurrentUser();
        }else Toast.makeText(getBaseContext(),"WTF >>>>>>",Toast.LENGTH_SHORT);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

    //-----------
/*
    private FloatingActionButton fab;
    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private static final String userId = "53";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_history);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
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

        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) findViewById(R.id.tv_no_movies);

        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter

        if (mAuth.getCurrentUser() == null) {
            mAuth.getCurrentUser().reload();
            Toast.makeText(getBaseContext(),"WTF",Toast.LENGTH_SHORT).show();
        }
        else {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            //myUserRef = database.getReference("users").child(mUser.getUid()).child("activities") ;
            //myUserRef = database.getReference("message");

            Toast.makeText(getBaseContext(), mUser.getEmail(), Toast.LENGTH_SHORT).show();

            //myUserRef.addValueEventListener(this);
            FirebaseRecyclerAdapter<ResourcesFromActivity, ResoursesViewHolder> adapter = new FirebaseRecyclerAdapter<ResourcesFromActivity, ResoursesViewHolder>(
                    ResourcesFromActivity.class,
                    R.layout.layout_result_activity,
                    ResoursesViewHolder.class,
                    //referencing the node where we want the database to store the data from our Object
                    mDatabaseReference.child("users").child(mUser.getUid()).child("activities").getRef()
            ) {
                @Override
                protected void populateViewHolder(ResoursesViewHolder viewHolder, ResourcesFromActivity model, int position) {
                    if (tvNoMovies.getVisibility() == View.VISIBLE) {
                        tvNoMovies.setVisibility(View.GONE);
                    }
                    viewHolder.tvMovieName.setText(model.getElapsedTimeString());
                    //viewHolder.ratingBar.setRating(model.getMovieRating());
                    //Picasso.with(this).load(model.getCountry()).into(viewHolder.ivMoviePoster);
                }
            };
            mRecyclerView.setAdapter(adapter);

        }



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new AddResourcesFragment())
                        .addToBackStack(null)
                        .commit();
                //animation being used to make floating actionbar disappear
                shrinkAnim.setDuration(400);
                fab.setAnimation(shrinkAnim);
                shrinkAnim.start();
                shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //changing floating actionbar visibility to gone on animation end
                        fab.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {


                    }
                });

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fab.getVisibility() == View.GONE)
            fab.setVisibility(View.VISIBLE);
    }

    //ViewHolder for our Firebase UI
    public static class ResoursesViewHolder extends RecyclerView.ViewHolder{

        TextView tvMovieName;
        //RatingBar ratingBar;
        //ImageView ivMoviePoster;

        public ResoursesViewHolder(View v) {
            super(v);
            tvMovieName = (TextView) v.findViewById(R.id.tv_name);
            //ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
            //ivMoviePoster = (ImageView) v.findViewById(R.id.iv_movie_poster);
        }
    }


}

 **************************/

