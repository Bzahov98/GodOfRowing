package com.bzahov.elsys.godofrowing.RecyclerView.MainGridView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.lang.reflect.Array;

/**
 * Created by bobo-pc on 11/21/2016.
 */

public class ParameterAdapter extends BaseAdapter {
    private final Context mContext;
    private final Array[] parameters;

    // 1
    public ParameterAdapter(Context context, Array[] parameters) {
        this.mContext = context;
        this.parameters = parameters;
    }

    // 2
    @Override
    public int getCount() {
        return parameters.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setText(String.valueOf(position));
        return dummyTextView;
    }

}



