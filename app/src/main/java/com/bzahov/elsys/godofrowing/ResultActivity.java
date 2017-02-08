package com.bzahov.elsys.godofrowing;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

 /**
 * Created by bobo-pc on 2/6/2017.
 */
public class ResultActivity extends TabActivity {

         TabHost host;
         @Override
         protected void onCreate(@Nullable Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);

             // setContentView(R.layout.activity_result);

             host = getTabHost();

             host.addTab(host
                     .newTabSpec("Tab for analysis")
                     .setIndicator("",getResources().getDrawable(R.drawable.icon_analysis,null))
                     .setContent(new Intent(this  ,ResultContentAnalysisActivity.class)));

             host.addTab(host
                     .newTabSpec("Tab for time Split")
                     .setIndicator("",getResources().getDrawable(R.drawable.icon_timer,null))
                     .setContent(new Intent(this  ,ResultContentSplitterActivity.class )));

             host.addTab(host
                     .newTabSpec("Tab for settings")
                     .setIndicator("",getResources().getDrawable(R.drawable.icon_settings,null))
                     .setContent(new Intent(this  ,ResultContentSettingsActivity.class )));
             host.setCurrentTab(0);

             //host = (TabHost) findViewById(R.id.res_tab_host);

             // SetHostTab();


         }
         /*
              private void SetHostTab() {

                  host.setup();

                  //Tab for analysis
                  hostTabSpec = host.newTabSpec("Tab for analysis");
                  hostTabSpec.setContent(R.id.res_tab_analysis);
                  hostTabSpec.setIndicator("", getResources().getDrawable(R.drawable.icon_analysis,null));
                  host.addTab(hostTabSpec);
                  RelativeLayout a = (RelativeLayout) findViewById(R.id.aa);
                  View b = a.getRootView().findViewById(R.id.res_elapsed_time);
                  ((TextView)a.findViewById(R.id.parameterName)).setText("assdads");
                  //Tab for time Split
                  hostTabSpec = host.newTabSpec("Tab for time Split");
                  hostTabSpec.setContent(R.id.res_tab_content_timer);
                  hostTabSpec.setIndicator("",getResources().getDrawable(R.drawable.icon_timer,null));
                  host.addTab(hostTabSpec);

                  //Tab for settings
                  hostTabSpec = host.newTabSpec("Tab for settings");
                  hostTabSpec.setContent(R.id.res_tab_content_settings);
                  hostTabSpec.setIndicator("", getResources().getDrawable(R.drawable.icon_settings,null));
                  host.addTab(hostTabSpec);
              }
         */
         @Override
         public boolean onCreateOptionsMenu(Menu menu) {
             //Inflate the  menu; this adds items to the action bar if it is present.
             getMenuInflater().inflate(R.menu.menu_res, menu);
             return true;
         }

         @Override
         public boolean onOptionsItemSelected(MenuItem item) {
             // Handle action bar item clicks here. The action bar will
             // automatically handle clicks on the Home/Up button, so long
             // as you specify a parent activity in AndroidManifest.xml.
             int id = item.getItemId();

             //noinspection SimplifiableIfStatement
             if (id == R.id.action_settings) {
                 return true;
             }

             return super.onOptionsItemSelected(item);
         }
     }




    /*private TabHost host;
     private TabHost.TabSpec hostTabSpec;

     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        host = (TabHost) findViewById(R.id.res_tab_host);

        SetHostTab();


    }

     private void SetHostTab() {

         host.setup();

         //Tab for analysis
         hostTabSpec = host.newTabSpec("Tab for analysis");
         hostTabSpec.setContent(R.id.res_tab_analysis);
         hostTabSpec.setIndicator("", getResources().getDrawable(R.drawable.icon_analysis,null));
         host.addTab(hostTabSpec);

         //Tab for time Split
         hostTabSpec = host.newTabSpec("Tab for time Split");
         hostTabSpec.setContent(R.id.res_tab_content_timer);
         hostTabSpec.setIndicator("",getResources().getDrawable(R.drawable.icon_timer,null));
         host.addTab(hostTabSpec);

         //Tab for settings
         hostTabSpec = host.newTabSpec("Tab for settings");
         hostTabSpec.setContent(R.id.res_tab_content_settings);
         hostTabSpec.setIndicator("", getResources().getDrawable(R.drawable.icon_settings,null));
         host.addTab(hostTabSpec);
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         //Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_res, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();

         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
             return true;
         }

         return super.onOptionsItemSelected(item);
     }
 }

*/