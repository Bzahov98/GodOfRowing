package com.bzahov.elsys.godofrowing.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * Created by bobo-pc on 12/30/2016.
 */
public class MainGraphFragment extends Fragment implements SensorEventListener {

    private LineChart lineGraphChart;
    private LineData chartGraphData;

    private float x_accelerometer;
    private  ArrayList<Entry> xVals;
    private LineDataSet dataSetX;

    private float y_accelerometer;
    private  ArrayList<Entry> yVals;
    private LineDataSet dataSetY;

    private float z_accelerometer;
    private  ArrayList<Entry> zVals ;
    private LineDataSet dataSetZ;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v =  inflater.inflate(R.layout.fragment_graph, parent, false);
            lineGraphChart = (LineChart) v.findViewById(R.id.LineChartFrag);
            lineGraphChart.getDescription().setEnabled(false);
            lineGraphChart.getDescription().setEnabled(false);
            lineGraphChart.setNoDataText("No Data at the moment");
            lineGraphChart.setTouchEnabled(true);
            lineGraphChart.setDragEnabled(true);
            lineGraphChart.setHardwareAccelerationEnabled(true);
            lineGraphChart.setPinchZoom(true);

            chartGraphData = new LineData();
            lineGraphChart.setData(chartGraphData);
            lineGraphChart.getXAxis().setDrawLabels(false);
            //lineGraphChart.getXAxis().setDrawGridLines(false);
            lineGraphChart.invalidate();

            chartGraphData = lineGraphChart.getData();

            ILineDataSet setX = chartGraphData.getDataSetByIndex(0);
            ILineDataSet setY = chartGraphData.getDataSetByIndex(1);
            ILineDataSet setZ = chartGraphData.getDataSetByIndex(2);

            setX = createSet("X", Color.BLACK);
            setY = createSet("Y",Color.YELLOW);
            setZ = createSet("Z",Color.GREEN);

            chartGraphData.addDataSet(setX);
            chartGraphData.addDataSet(setY);
            chartGraphData.addDataSet(setZ);

        //=========================
            mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

            return v;
        }

    @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

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
            //DetailsGraph();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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

    private void addDataSet( LineData data, ArrayList vals) {

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

    private void removeDataSet(LineData data, int index) {

        if (data != null) {

            if ( (data.getDataSetCount() - 1) > index) {
                data.removeDataSet(data.getDataSetByIndex(index));
            }else
                data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
        }
    }
}