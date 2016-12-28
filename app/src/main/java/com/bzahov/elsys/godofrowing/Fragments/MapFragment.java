package com.bzahov.elsys.godofrowing.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;

/**
 * Created by bobo-pc on 12/28/2016.
 */
public class MapFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map,container,false);

        return view;

    }
}
