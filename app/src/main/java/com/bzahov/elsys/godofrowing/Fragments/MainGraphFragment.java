package com.bzahov.elsys.godofrowing.Fragments;

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
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * Created by bobo-pc on 12/30/2016.
 */
public class MainGraphFragment extends Fragment{

    private LineChart mChart;
    private LineData chartGraphData;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View v =  inflater.inflate(R.layout.fragment_graph, parent, false);
            mChart = (LineChart) v.findViewById(R.id.LineChartFrag);
        mChart.getDescription().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.setNoDataText("No Data at the moment");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setHardwareAccelerationEnabled(true);
        mChart.setPinchZoom(true);
        // add an empty data object
        mChart.setData(new LineData());
        mChart.getXAxis().setDrawLabels(false);
        //mChart.getXAxis().setDrawGridLines(false);
        mChart.invalidate();

        chartGraphData = mChart.getData();

        ILineDataSet setX = chartGraphData.getDataSetByIndex(0);
        ILineDataSet setY = chartGraphData.getDataSetByIndex(1);
        ILineDataSet setZ = chartGraphData.getDataSetByIndex(2);

        setX = createSet("X", Color.BLACK);
        setY = createSet("Y",Color.YELLOW);
        setZ = createSet("Z",Color.GREEN);

        chartGraphData.addDataSet(setX);
        chartGraphData.addDataSet(setY);
        chartGraphData.addDataSet(setZ);
            return v;
        }

    public ILineDataSet createSet(String dataSetName, int mainColor) {
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

    @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

        }
    }