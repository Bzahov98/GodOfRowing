package com.bzahov.elsys.godofrowing.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;

/**
 * Created by bobo-pc on 3/7/2017.
 */
/*public class FirebaseActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    View mView;
    Context mContext;
    public FirebaseActivityViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRestaurant(ResourcesFromActivity restaurant) {
        //ImageView restaurantImageView = (ImageView) mView.findViewById(R.id.list_item_child_meters_total);
        TextView nameTextView = (TextView) mView.findViewById(R.id.start_date_head_text);
        TextView totalMetersTextView = (TextView) mView.findViewById(R.id.list_item_child_meters_total);
    //    TextView ratingTextView = (TextView) mView.findViewById(R.id.ratingTextView);

        /*Picasso.with(mContext)
                .load(restaurant.getImageUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(restaurantImageView);*/
/*
        nameTextView.setText(Long.toString(rest.getTotalMeters()));
        totalMetersTextView.setText(Long.toString(rest.getTotalMeters()) + " (1)");
        //ratingTextView.setText("Rating: " + restaurant.getRating() + "/5");
    }

    @Override
    public void onClick(View view) {
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
        });
        Toast.makeText(mContext, "OnClickView", Toast.LENGTH_SHORT).show();
    }
}
*/