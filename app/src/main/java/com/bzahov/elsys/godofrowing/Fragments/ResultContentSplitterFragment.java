package com.bzahov.elsys.godofrowing.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;

/**
 * Created by bobo-pc on 5/29/2017.
 */

public class ResultContentSplitterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_result_splitter, container, false);

        return v;
    }
}
