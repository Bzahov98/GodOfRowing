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
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private Sensor mAccelerometer;

    private float x_accelerometer;
    private  ArrayList<Entry> xVals;
    private LineDataSet dataSetX;

    private float y_accelerometer;
    private  ArrayList<Entry> yVals;
    private LineDataSet dataSetY;

    private float z_accelerometer;
    private  ArrayList<Entry> zVals ;
    private LineDataSet dataSetZ;

    private int choosedDetailOption;

    private LineChart lineGraphChart;
    private LineData chartGraphData;
    private ArrayList<ILineDataSet> lineDataSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //============================
        SetGraph();
    }

    private void SetGraph() {

        lineGraphChart = (LineChart) findViewById(R.id.LineChart);
        lineGraphChart.getDescription().setEnabled(false);
        lineGraphChart.getDescription().setEnabled(false);
        lineGraphChart.setNoDataText("No Data at the moment");
        lineGraphChart.setTouchEnabled(true);
        lineGraphChart.setDragEnabled(true);
        lineGraphChart.setHardwareAccelerationEnabled(true);
        lineGraphChart.setPinchZoom(true);
        // add an empty data object
        lineGraphChart.setData(new LineData());
        lineGraphChart.getXAxis().setDrawLabels(false);
        //lineGraphChart.getXAxis().setDrawGridLines(false);
        lineGraphChart.invalidate();

        chartGraphData = lineGraphChart.getData();

        ILineDataSet setX = chartGraphData.getDataSetByIndex(0);
        ILineDataSet setY = chartGraphData.getDataSetByIndex(1);
        ILineDataSet setZ = chartGraphData.getDataSetByIndex(2);

        setX = createSet("X",Color.BLACK);
        setY = createSet("Y",Color.YELLOW);
        setZ = createSet("Z",Color.GREEN);

        chartGraphData.addDataSet(setX);
        chartGraphData.addDataSet(setY);
        chartGraphData.addDataSet(setZ);
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
            yText.setText("");
            zText.setText("");
        }
    }

    private void addEntry(LineChart lineChart, float AccelerometerValue, int dataSetIndex) {

        LineData data = lineChart.getData();

        ILineDataSet set = data.getDataSetByIndex(dataSetIndex);

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        int entryCount = (data.getDataSetByIndex(dataSetIndex).getEntryCount());
        data.addEntry(new Entry(entryCount, AccelerometerValue), dataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        lineChart.notifyDataSetChanged();

        lineChart.setVisibleXRangeMaximum(6);
        //lineGraphChart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
        // this automa1tically refreshes the chart (calls invalidate())
        lineChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
    }

    private ILineDataSet createSet() {
        //Defaut
        ArrayList<Entry> data = new ArrayList<>();
        data.add(new Entry(0, 0.0f));

        LineDataSet set = new LineDataSet(data, "");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);
        return set;
    }

    private ILineDataSet createSet(String dataSetName, int mainColor) {
            //Custom
            ArrayList<Entry> data = new ArrayList<>();
                data.add(new Entry(0, 0.0f));

            LineDataSet newSet = new LineDataSet(data, dataSetName);
            newSet.setColor(mainColor);
            newSet.setHighLightColor(Color.MAGENTA);
            newSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            newSet.setValueTextSize(10f);
            newSet.setLineWidth(2.5f);
            newSet.setCircleRadius(4.5f);
            return newSet;
    }

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
    private void addDataSet( ArrayList vals) {

        LineData data = lineGraphChart.getData();

        if (data != null) {

            int count = (data.getDataSetCount() + 1);

            LineDataSet set = new LineDataSet(vals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
        }
    }

    private void removeDataSet(int index) {

        LineData data = lineGraphChart.getData();

        if (data != null) {

            if ( (data.getDataSetCount() - 1) > index) {
                data.removeDataSet(data.getDataSetByIndex(index));
            }else
                data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor accSensor = sensorEvent.sensor;

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x_accelerometer = sensorEvent.values[0];
            addEntry(lineGraphChart ,x_accelerometer,0);
            y_accelerometer = sensorEvent.values[1];
           //     yVals.add(new Entry(i, y_accelerometer));
            addEntry(lineGraphChart, y_accelerometer,1);
            z_accelerometer = sensorEvent.values[2];
             //   zVals.add(new Entry(i, z_accelerometer));
            addEntry(lineGraphChart ,z_accelerometer,2);

            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
           // i++;
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
