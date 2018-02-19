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
import android.util.Log;
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
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
	private float x_gForce;
	private float y_gForce;
	private float z_gForce;

	private boolean show_gForce = false;
	float appliedAcceleration = 0;
	float currentAcceleration = 0;
	float velocity = 0;
	Date lastUpdate;
	private double calibration;
	private GraphFrgCommunicationChannel mCommChListner;
	private float[] gravity;
	private float[] linear_acceleration;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.fragment_graph, parent, false);
		lineGraphChart = (LineChart) v.findViewById(R.id.LineChartFrag);

		setGraph(v);
		//super.onCreateView(inflater,parent,savedInstanceState);
		//=========================

		mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

		calibration = Double.NaN;
		lastUpdate = new Date(System.currentTimeMillis());

		return v;
	}

	private void setGraph(View v) {
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

		setX = createSet("X", Color.YELLOW);
		setY = createSet("Y",Color.YELLOW);
		setZ = createSet("Z",Color.GREEN);

		chartGraphData.addDataSet(setX);
		//chartGraphData.addDataSet(setY);
		//chartGraphData.addDataSet(setZ);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view,savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this,mAccelerometer);
	}


	private void updateVelocity() {
		// Calculate how long this acceleration has been applied.

		Date timeNow = new Date(System.currentTimeMillis());
		long timeDelta = timeNow.getTime()-lastUpdate.getTime();
		//Log.e("Acceler", timeNow.toString() + " D: "+ Long.toString(timeDelta));

		lastUpdate.setTime(timeNow.getTime());

		// Calculate the change in velocity at the
		// current acceleration since the last update.
		float deltaVelocity = appliedAcceleration * (timeDelta/1000);
		appliedAcceleration = currentAcceleration;

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {

		//Log.e("Acceler", "Sensor");

		Sensor accSensor = sensorEvent.sensor;

		if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			x_accelerometer = sensorEvent.values[0];
			x_gForce = x_accelerometer / 9.81f;
			//
			y_accelerometer = sensorEvent.values[1];
			y_gForce = y_accelerometer / 9.81f;
			//     yVals.add(new Entry(i, y_accelerometer));
			z_accelerometer = sensorEvent.values[2];
			z_gForce = z_accelerometer / 9.81f;
			//   zVals.add(new Entry(i, z_accelerometer));

			float newAcceler = (float) Math.sqrt(Math.pow(x_accelerometer, 2) + Math.pow(y_accelerometer, 2) + Math.pow(z_accelerometer, 2));

			if (calibration == Double.NaN){
				calibration = newAcceler;
			}
			updateVelocity();
			currentAcceleration = (float) newAcceler;
			/*if (show_gForce) {
				addEntry(lineGraphChart, x_gForce, 0);
				addEntry(lineGraphChart, y_gForce, 1);
				addEntry(lineGraphChart, z_gForce, 2);
			} else {
				addEntry(lineGraphChart, newAcceler, 0);
				//addEntry(lineGraphChart, y_accelerometer, 1);
				//addEntry(lineGraphChart, z_accelerometer, 2);
			}*/


			final float alpha = 0.8f;

			gravity[0] = alpha * gravity[0] + (1 - alpha) * x_accelerometer;
			gravity[1] = alpha * gravity[1] + (1 - alpha) * y_accelerometer;
			gravity[2] = alpha * gravity[2] + (1 - alpha) * z_accelerometer;

			linear_acceleration[0] = x_accelerometer - gravity[0];
			linear_acceleration[1] = y_accelerometer - gravity[1];
			linear_acceleration[2] = z_accelerometer - gravity[2];

			currentAcceleration = (float) (Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2)+ Math.pow(linear_acceleration[2], 2));

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

	public interface GraphFrgCommunicationChannel
	{
		void setGraphCommunication(String msg);
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if(context instanceof GraphFrgCommunicationChannel)
		{
			mCommChListner = (GraphFrgCommunicationChannel)context;
		}
		else
		{
			throw new ClassCastException();
		}
	}
	public void sendMessage(String msg)
	{
		mCommChListner.setGraphCommunication(msg);
	}

	@Override
	public void onResume() {
		super.onResume();

		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	public void onPause() {

		super.onPause();

		mSensorManager.unregisterListener(this);

	}
}
