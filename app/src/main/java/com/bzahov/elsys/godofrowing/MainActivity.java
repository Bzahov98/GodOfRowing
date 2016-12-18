package com.bzahov.elsys.godofrowing;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private Sensor mAccelerometer;
    private float x_accelerometer;
    private  ArrayList<Entry> xVals;
    private float y_accelerometer;
    private  ArrayList<Entry> yVals;
    private float z_accelerometer;
    private  ArrayList<Entry> zVals ;
    private int choosedDetailOption;
    private LineData chartGraphData;
    private LineChart lineGraphChart;
    private LineDataSet dataSetX;
    private LineDataSet dataSetZ;
    private LineDataSet dataSetY;
    ArrayList<ILineDataSet> lineDataSets;
    private int i = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TableLayout ParamTable = (TableLayout)findViewById(R.id.main_param_table);
        TableRow RowOne = (TableRow) findViewById(R.id.main_table_row_1);

        TableLayout ParamMete1r =  (TableLayout) findViewById(R.id.main_param_table);
        TextView ParamMeter = (TextView) findViewById(R.id.param_Meters);
        ParamMeter.setText("0000\nmeters");
        ParamMeter.setTextSize(17);


        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.main_HScrow_View) ;
        LayoutInflater li = LayoutInflater.from(getBaseContext());

        View v = li.inflate(R.layout.context_main_paramether,null,false);
        scrollView.addView(v);

        //--------------Accelerometer preparation----------------------
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

       SetGraph();
    }

    private void SetGraph() {

        xVals = new ArrayList<Entry>();
        yVals = new ArrayList<Entry>();
        zVals = new ArrayList<Entry>();

        lineGraphChart = (LineChart) findViewById(R.id.LineChart);
        lineGraphChart.setVisibility(View.INVISIBLE); /// <---------------------------

        lineGraphChart.setNoDataText("No Data at the moment");
        lineGraphChart.setTouchEnabled(true);
        lineGraphChart.setDragEnabled(true);
        lineGraphChart.setHardwareAccelerationEnabled(true);
        lineGraphChart.setPinchZoom(true);

     //   yVals.add(new Entry(11, 10));

        // create a dataset and give it a type
        dataSetX = new LineDataSet(xVals, "X");
        dataSetY = new LineDataSet(yVals, "Y");
        dataSetZ = new LineDataSet(zVals, "Z");

        lineDataSets = new ArrayList<>();

        dataSetX.setFillColor(Color.WHITE);

        // set the line to be drawn like this "- - - - - -"
        // dataSetX.enableDashedLine(10f, 5f, 0f);
        // dataSetX.enableDashedHighlightLine(10f, 5f, 0f);
        //==========
        dataSetX.addEntry(new Entry(0, 0));
        dataSetX.setColor(Color.BLACK);
        dataSetX.setCircleColor(Color.BLACK);
        dataSetX.setValueTextSize(9f);

        dataSetY.addEntry(new Entry(0, 0));
        dataSetY.setColor(Color.YELLOW);
        dataSetY.setCircleColor(Color.YELLOW);
        dataSetY.setValueTextSize(9f);

        dataSetZ.addEntry(new Entry(0, 0));
        dataSetZ.setColor(Color.BLUE);
        dataSetZ.setCircleColor(Color.BLUE);
        dataSetZ.setValueTextSize(9f);
        //==========

        lineDataSets.add(dataSetX);
        lineDataSets.add(dataSetY);
        lineDataSets.add(dataSetZ);

        lineGraphChart.setVisibleXRangeMaximum(70f);

        lineGraphChart.setData(new LineData(lineDataSets));
        // ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //dataSets.add(dataSetX); // add the datasets

        // create a data object with the datasets
        // LineDataSet chartGraphDataSet = new LineData(xVals, "# of Calls");*/
    }

    // ScrollView child on Click
    public void ScrollItem(View view) {

        //RelativeLayout detailLayout = (RelativeLayout) findViewById(R.id.details);
        TextView detailText = ((TextView) (findViewById(R.id.details)).findViewById(R.id.first_text));

        switch (view.getId()) {

            case R.id.param_1:
                choosedDetailOption = 1;
                DetailsGraph();
                break;
            case R.id.param_2:
                TextView p2 = (TextView) findViewById(R.id.param_2);
                String p2Text = (String) p2.getText();
                detailText.setText(p2Text);
                choosedDetailOption =2 ;
                //Toast.makeText(this, p2Text + " Clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.param_3:
                TextView p3 = (TextView) findViewById(R.id.param_3);
                String p3Text = (String) p3.getText();
                detailText.setText(p3Text);
                choosedDetailOption = 3;
               // Toast.makeText(this, p3Text + " Clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.param_4:
                TextView p4 = (TextView) findViewById(R.id.param_4);
                String p4Text = (String) p4.getText();
                detailText.setText(p4Text);
                choosedDetailOption = 4;
                //Toast.makeText(this, p4Text + " Clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.param_5:
                TextView p5 = (TextView) findViewById(R.id.param_5);
                String p5Text = (String) p5.getText();
                detailText.setText(p5Text);
                choosedDetailOption = 5;
                //Toast.makeText(this, p5Text + " Clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.param_6:
                TextView p6 = (TextView) findViewById(R.id.param_6);
                String p6Text = (String) p6.getText();
                detailText.setText(p6Text);
                choosedDetailOption = 6;
               // Toast.makeText(this, p6Text + " Clicked ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //
    private void DetailsGraph() {
        RelativeLayout detailLayout = (RelativeLayout) findViewById(R.id.details);
        TextView xText = (TextView) detailLayout.findViewById(R.id.first_text);
        TextView yText = (TextView) detailLayout.findViewById(R.id.second_text);
        TextView zText = (TextView) detailLayout.findViewById(R.id.third_text);

        if (choosedDetailOption == 1) {
            lineGraphChart.setVisibility(View.VISIBLE);

            xText.setText("X: = " + Float.toString(x_accelerometer));

            yText.setText("Y: = " + Float.toString(y_accelerometer));

            zText.setText("Z: = " + Float.toString(z_accelerometer));

        }else {
            lineGraphChart.setVisibility(View.INVISIBLE);
            yText.setText("@");
            zText.setText("#");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor accSensor = sensorEvent.sensor;

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x_accelerometer = sensorEvent.values[0];
                xVals.add(new Entry(i, x_accelerometer));
            y_accelerometer = sensorEvent.values[1];
                yVals.add(new Entry(i, y_accelerometer));
            z_accelerometer = sensorEvent.values[2];
                zVals.add(new Entry(i, z_accelerometer));


            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
            i++;
            DetailsGraph();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){}

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
