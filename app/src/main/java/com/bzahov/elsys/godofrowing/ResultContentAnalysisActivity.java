package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentAnalysisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_analysis );
        ScrollView a = (ScrollView) findViewById(R.id.res_analysis_scroll_view);
        a.setVisibility(View.VISIBLE);
       // a.setBackground(getDrawable(R.drawable.icon_settings));
        RelativeLayout analysis = (RelativeLayout) a.findViewById(R.id.res_elapsed_time);
        ((TextView) analysis.findViewById(R.id.parameterName)).setText("asdsadsad");
        ImageView image = ((ImageView) analysis.findViewById(R.id.parameterPic));
        image.setBackground(getDrawable(R.drawable.icon_timer));
       // image.getLayoutParams().height = 40;
       // image.getLayoutParams().width = 41;


    }
}
