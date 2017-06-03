package com.bzahov.elsys.godofrowing.Fragments;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.TestData.AccelerometerTestData;
import com.bzahov.elsys.godofrowing.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by bobo-pc on 1/14/2017.
 */

public abstract class BaseChartFragment extends Fragment {

	protected LineChart lineGraphChart;
	protected LineData chartGraphData;

	protected float x_accelerometer;
	protected ArrayList<Entry> xVals;
	protected LineDataSet dataSetX;

	protected float y_accelerometer;
	protected  ArrayList<Entry> yVals;
	protected LineDataSet dataSetY;

	protected float z_accelerometer;
	protected  ArrayList<Entry> zVals ;
	protected LineDataSet dataSetZ;

	protected SensorManager mSensorManager;
	protected Sensor mAccelerometer;
	private int[] firstAtListIndex;
	protected float[] xFloatTestVector;
	protected float[] yFloatTestVector;
	protected float[] zFloatTestVector;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AccelerometerTestData accelerometerTestData = new AccelerometerTestData();

		//Log.e("Float","\n\n\n\n\n-------------X------------------------");
		xFloatTestVector = accelerometerTestData.getXtestVector();
		//Log.e("Float","\n\n\n\n\n-------------Y------------------------");
		yFloatTestVector = accelerometerTestData.getYtestVector();
		//Log.e("Float","\n\n\n\n\n-------------Z-------------------------");
		zFloatTestVector = accelerometerTestData.getZtestVector();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.fragment_graph, parent, false);

		setGraph(v);
		setSensor();
		firstAtListIndex = new int [10];
		for (int i = 0; i < 3 ; i++){
			firstAtListIndex[i] = 0;
		}
		return v;
	}

	protected abstract void setSensor();

	protected void setGraph(View v) {
		lineGraphChart = (LineChart) v.findViewById(R.id.LineChartFrag);
		lineGraphChart.getDescription().setEnabled(false);
		lineGraphChart.setNoDataText("No Data at the moment");
		lineGraphChart.setTouchEnabled(false);
		lineGraphChart.setDragEnabled(false);
		lineGraphChart.setHardwareAccelerationEnabled(true);
		lineGraphChart.setPinchZoom(false);

		chartGraphData = new LineData();
		lineGraphChart.setData(chartGraphData);
		lineGraphChart.getXAxis().setDrawLabels(false);
		//lineGraphChart.getXAxis().setDrawGridLines(false);
		lineGraphChart.invalidate();

		chartGraphData = lineGraphChart.getData();

		ILineDataSet setX = chartGraphData.getDataSetByIndex(0);
		ILineDataSet setY = chartGraphData.getDataSetByIndex(1);
		ILineDataSet setZ = chartGraphData.getDataSetByIndex(2);
		ILineDataSet setj = chartGraphData.getDataSetByIndex(3);

		setX = createSet("X", Color.BLACK);
		setY = createSet("Y",Color.YELLOW);
		setZ = createSet("Z",Color.GREEN);
		chartGraphData.addDataSet(setX);
		chartGraphData.addDataSet(setY);
		chartGraphData.addDataSet(setZ);

		lineGraphChart.getAxisLeft().setAxisMinimum(-8);
		lineGraphChart.getAxisLeft().setAxisMaximum(9);
		lineGraphChart.invalidate();

		//chartGraphData.addDataSet(setj);
	}

	protected void addEntry(LineChart lineChart, float AccelerometerValue, int dataSetIndex) {

		LineData data = lineChart.getData();

		ILineDataSet set = data.getDataSetByIndex(dataSetIndex);

		if (set == null) {
			set = createSet();
			data.addDataSet(set);
			firstAtListIndex[dataSetIndex] = 0;
		}

		DecimalFormat df = new DecimalFormat("0.00");

		int entryCount = (data.getDataSetByIndex(dataSetIndex).getEntryCount());
		//data.removeEntry(0,dataSetIndex);
		set.addEntry(new Entry(entryCount + 1, AccelerometerValue));

		data.notifyDataChanged();
		lineChart.invalidate();
		lineChart.notifyDataSetChanged();
		// let the chart know it's data has changed


		lineChart.setVisibleXRangeMaximum(60);
		lineChart.setVisibleXRangeMinimum(40);

		lineChart.moveViewTo(data.getEntryCount() - 2, 10f, YAxis.AxisDependency.LEFT);

		firstAtListIndex[dataSetIndex]++;

	}

	protected ILineDataSet createSet() {
		//Defaut
		ArrayList<Entry> data = new ArrayList<>();
		data.add(new Entry(0, 0.0f));

		LineDataSet set = new LineDataSet(data, "");
		set.setLineWidth(1.5f);
		set.setCircleRadius(1.5f);
		set.setColor(Color.rgb(240, 99, 99));
		set.setCircleColor(Color.rgb(240, 99, 99));
		set.setHighLightColor(Color.rgb(190, 190, 190));
		set.setAxisDependency(YAxis.AxisDependency.LEFT);
		set.setValueTextSize(10f);
		return set;
	}

	protected ILineDataSet createSet(String dataSetName, int mainColor) {
		//Custom
		ArrayList<Entry> data = new ArrayList<>();
		data.add(new Entry(0, 0.0f));

		LineDataSet newSet = new LineDataSet(data, dataSetName);
		newSet.setColor(mainColor);
		newSet.setHighLightColor(Color.MAGENTA);
		newSet.setAxisDependency(YAxis.AxisDependency.LEFT);
		newSet.setValueTextSize(0f);
		newSet.setLineWidth(3.5f);
		newSet.setCircleRadius(1.5f);
		return newSet;
	}

	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

	protected void addDataSet( LineData data, ArrayList vals) {

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

	protected void removeDataSet(LineData data, int index) {

		if (data != null) {

			if ( (data.getDataSetCount() - 1) > index) {
				data.removeDataSet(data.getDataSetByIndex(index));
			}else
				data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

			lineGraphChart.notifyDataSetChanged();
			lineGraphChart.invalidate();
		}
	}

	protected  void clearDataSet(LineData data, int index){
		if (data != null) {


			firstAtListIndex[0] = 0;
			firstAtListIndex[1] = 0;
			firstAtListIndex[2] = 0;
			firstAtListIndex[3] = 0;

			data.clearValues();

			ILineDataSet setX = chartGraphData.getDataSetByIndex(0);
			ILineDataSet setY = chartGraphData.getDataSetByIndex(1);
			ILineDataSet setZ = chartGraphData.getDataSetByIndex(2);
			ILineDataSet setj = chartGraphData.getDataSetByIndex(3);

			setX = createSet("X", Color.BLACK);
			setY = createSet("Y",Color.YELLOW);
			setZ = createSet("Z",Color.GREEN);
			chartGraphData.addDataSet(setX);
			chartGraphData.addDataSet(setY);
			chartGraphData.addDataSet(setZ);

			lineGraphChart.notifyDataSetChanged();
			lineGraphChart.invalidate();
		}
	}
}
