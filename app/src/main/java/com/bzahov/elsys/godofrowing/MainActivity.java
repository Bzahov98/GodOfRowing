package com.bzahov.elsys.godofrowing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.MainGridView.ParameterAdapter;

import org.w3c.dom.Text;

import javax.sql.RowSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TableLayout ParamTable = (TableLayout)findViewById(R.id.main_param_table);
        TableRow RowOne = (TableRow) findViewById(R.id.main_table_row_1);

        TableLayout ParamMete1r =  (TableLayout) findViewById(R.id.main_param_table);
       // ParamMeter.findViewById(R.id.);
        TextView ParamMeter = (TextView) findViewById(R.id.param_Meters);
        ParamMeter.setText("0000\nmeters");
        ParamMeter.setTextSize(17);
     }
}
