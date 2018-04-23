package com.bzahov.elsys.godofrowing.Fragments.Dialogs;

/**
 * Created by bobo-pc on 2/12/2017.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.R;

public class AlertUserLoggedFragment extends DialogFragment {

    public static AlertUserLoggedFragment newInstance(String userEmail) {
        AlertUserLoggedFragment frag = new AlertUserLoggedFragment();
        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String userEmail = getArguments().getString("userEmail");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon_logo)
                .setTitle("Hello "+ userEmail)
                // Set Dialog Message
                .setMessage("You are already logged in . \n" +
                        "Does you want to log out\n" +
                        "and to log in at another account?")

                // Positive button
                .setPositiveButton("OK, log out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), LogInActivity.class);
                        startActivity(intent);
                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        Toast.makeText(getContext(),"You stay at "+ userEmail,Toast.LENGTH_SHORT).show();;
                    }
                }).create();
    }
}
