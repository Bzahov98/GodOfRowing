package com.bzahov.elsys.godofrowing.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;

/**
 * Created by bobo-pc on 12/30/2016.
 */
public class MainGraphFragment extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_graph, parent, false);
        }


        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

        }
    }