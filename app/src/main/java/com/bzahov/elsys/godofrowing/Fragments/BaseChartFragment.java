package com.bzahov.elsys.godofrowing.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.fragment_graph, parent, false);

		setGraph(v);
		setSensor();
		firstAtListIndex = new int [10];
		for (int i = 0; i < 3 ; i++){
			firstAtListIndex[i] = 0;
		}
		/*mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
		*/
		return v;
	}

	protected abstract void setSensor();

	protected void setGraph(View v) {
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
		ILineDataSet setj = chartGraphData.getDataSetByIndex(3);

		setX = createSet("X", Color.BLACK);
		setY = createSet("Y",Color.YELLOW);
		setZ = createSet("Z",Color.GREEN);
		chartGraphData.addDataSet(setX);
		chartGraphData.addDataSet(setY);
		chartGraphData.addDataSet(setZ);
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

		/*if (set.getEntryCount() > GRAPH_WIDTH) {
			set.(0);
			set.removeEntry(0);

			lineData.getXVals().add(entry_date_time);
			lineData.addEntry(new Entry((float) (Math.random() * 40) + 30f, GRAPH_WIDTH), 0);

			// lineData.getXVals().add(entry_date_time);
			// Move all entries 1 to the left..
			for (int i=0; i < set.getEntryCount(); i++) {
				Entry e = set.getEntryForXIndex(i);
				if (e==null) continue;

				e.setXIndex(e.getXIndex() - 1);
			}
		}
		else{
			lineData.getXVals().add(entry_date_time);
			lineData.addEntry(new Entry((float) (Math.random() * 40) + 30f, lineData.getXValCount()-1), 0);
		}

		// let the chart know it's data has changed
		mChart.notifyDataSetChanged();
		mChart.invalidate();

*/
		DecimalFormat df = new DecimalFormat("0.00");

		int entryCount = (data.getDataSetByIndex(dataSetIndex).getEntryCount());
		//data.removeEntry(0,dataSetIndex);
		set.addEntry(new Entry(entryCount+1, Float.parseFloat(df.format(AccelerometerValue))));
		data.notifyDataChanged();
		lineChart.invalidate();

		// let the chart know it's data has changed
		lineChart.notifyDataSetChanged();

		lineChart.setVisibleXRangeMaximum(50);
		lineChart.setVisibleXRangeMinimum(40);
		//lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);
		// this automa1tically refreshes the chart (calls invalidate())
		lineChart.moveViewTo(data.getEntryCount() - 2, 20f, YAxis.AxisDependency.LEFT);

		firstAtListIndex[dataSetIndex]++;

	}

	protected ILineDataSet createSet() {
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

	protected ILineDataSet createSet(String dataSetName, int mainColor) {
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

   /* @Override
	public void onPause() {
		if(mSensorManager != null) {
			mSensorManager.unregisterListener(this);
		}
		super.onPause();
	}


	@Override
	public abstract void onAccuracyChanged(Sensor sensor, int i);

	@Override
	public abstract void onSensorChanged(SensorEvent sensorEvent);

*/
}
