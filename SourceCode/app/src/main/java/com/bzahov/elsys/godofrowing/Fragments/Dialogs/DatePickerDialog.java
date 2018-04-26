package com.bzahov.elsys.godofrowing.Fragments.Dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.R;
import com.bzahov.elsys.godofrowing.RowApplication;
import com.bzahov.elsys.godofrowing.Support.DateFunctions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.bzahov.elsys.godofrowing.Support.DateFunctions.convertDatetoMillis;
import static com.bzahov.elsys.godofrowing.Support.DateFunctions.formatToStr;

/**
 * God of Rowing
 * Created by B. Zahov on 23.04.18.
 */
public class DatePickerDialog extends DialogFragment {

    private TextView startDateView;
    private TextView endDateView;
    private DatePicker datePickerView;
    private ImageButton startDateBtn;
    private ImageButton endDateBtn;
    private int currYear=2010;
    private int currMonth=0;
    private int currDay=1;
    private String currYearStr="2010";
    private String currMonthStr="01";
    private String currDayStr="01";
    private String currDateStr=  "2010:01:01";
    private long startDateMillis;
    private long endDateMillis;

    private Calendar todaysDay;
    private GregorianCalendar startDateC;
    private Calendar endDateC;
    
    //private Date startDateView;
    //private Date endDateView;

    private ImageButton startEraseBtn;
    private ImageButton endEraseBtn;
    private RowApplication app = RowApplication.getInstance();
    private Button doneBtn;
    private Button cancelBtn;

    public DatePickerDialog(){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
        setDefaultDates();
        setListeners();

    }

    public static DatePickerDialog newInstance(String title){
        DatePickerDialog frag = new DatePickerDialog();
        Bundle args = new Bundle();
        //.. args.put(...);
        args.putString("title",title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_date_range, container);
    }

    private void setListeners() {
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateSequences(datePickerView);
                if (DateFunctions.isDateBefore(currDay,currMonth,currYear, endDateC)){
                    startDateView.setText(currDateStr);
                    startDateMillis = convertDatetoMillis(currDateStr+ app.getString(R.string.time_start_day),app.getString(R.string.time_pattern));
                    startDateC.set(currYear,currMonth,currDay);
                }else{
                    Toast.makeText(getContext(), app.getString(R.string.err_date_before_end),Toast.LENGTH_SHORT).show();
                }
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateSequences(datePickerView);
                if (DateFunctions.isDateAfter(currDay, currMonth, currYear, startDateC)) {
                    endDateView.setText(currDateStr);
                    endDateC.set(currYear, currMonth, currDay);
                    endDateMillis = convertDatetoMillis(currDateStr + app.getString(R.string.time_end_day), app.getString(R.string.time_pattern));
                } else
                    Toast.makeText(getContext(), app.getString(R.string.err_date_after_start), Toast.LENGTH_SHORT).show();
            }
            });

        startEraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateSequences(1,0,2010);
                startDateView.setText(currDateStr);
                startDateMillis = convertDatetoMillis(currDateStr+ app.getString(R.string.time_start_day),app.getString(R.string.time_pattern));
                startDateC.set(currYear,currMonth,currDay);
            }
        });

        endEraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateSequences(todaysDay.get(Calendar.DAY_OF_MONTH), todaysDay.get(Calendar.MONTH), todaysDay.get(Calendar.YEAR));
                endDateView.setText(currDateStr);
                endDateMillis = convertDatetoMillis(currDateStr+ app.getString(R.string.time_end_day), app.getString(R.string.time_pattern));
                endDateC.set(currYear,currMonth,currDay);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSuccessfully",true);
                bundle.putLong("startDate", startDateMillis);
                bundle.putLong("endDate", endDateMillis);

                Intent intent = new Intent().putExtras(bundle);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                getDialog().dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSuccessfully",false);

                Intent intent = new Intent().putExtras(bundle);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                getDialog().dismiss();
            }
        });
    }

    private void setViews(View view) {
        datePickerView = (DatePicker) view.findViewById(R.id.myDatePicker);
        startDateView = (TextView)view.findViewById(R.id.start_date_head_text_date);
        endDateView = (TextView)view.findViewById(R.id.end_date_head_text_date);

        startDateBtn = (ImageButton) view.findViewById(R.id.start_date_parameter_image_end);
        endDateBtn = (ImageButton) view.findViewById(R.id.end_date_parameter_image_end);

        startEraseBtn = (ImageButton) view.findViewById(R.id.start_date_parameter_image_factory);
        endEraseBtn = (ImageButton) view.findViewById(R.id.end_date_parameter_image_factory);

        doneBtn = (Button) view.findViewById(R.id.range_btn_done);
        cancelBtn = (Button) view.findViewById(R.id.range_btn_cancel);

        datePickerView.setCalendarViewShown(false);
    }

    private void setDefaultDates() {
        todaysDay = Calendar.getInstance();
        endDateC = (Calendar) todaysDay.clone();

        //
        startDateC = new GregorianCalendar(2010, 0,1);//getInstance().set(1,0,2010);
        setDateSequences(1,0,2010);
        startDateView.setText(currDateStr);
        startDateMillis = convertDatetoMillis(currDateStr+app.getString(R.string.time_start_day),app.getString(R.string.time_pattern));
        Log.d("millis",""+ Long.toString(startDateMillis));
        setDateSequences(todaysDay.get(Calendar.DAY_OF_MONTH), todaysDay.get(Calendar.MONTH), todaysDay.get(Calendar.YEAR));
        endDateView.setText(currDateStr);
        endDateC.set(currYear,currMonth,currDay);
        endDateMillis = convertDatetoMillis(currDateStr+app.getString(R.string.time_end_day),app.getString(R.string.time_pattern));
        datePickerView.setMaxDate(todaysDay.getTimeInMillis());
    }

    private void setDateSequences(int day, int month, int year) {
        currDay = day;
        currDayStr = formatToStr(currDay);
        currMonth = month;
        currMonthStr = formatToStr(currMonth+1);
        currYear = year;
        currYearStr = String.valueOf(currYear);
        currDateStr = currYearStr + ":" + currMonthStr+ ":" + currDayStr;
    }

    private void getDateSequences(DatePicker datePicker){
        currDay = datePicker.getDayOfMonth();
        currDayStr = formatToStr(currDay);
        currMonth = datePicker.getMonth();
        currMonthStr = formatToStr(currMonth+1);
        currYear = datePicker.getYear();
        currYearStr = String.valueOf(currYear);
        currDateStr = currYearStr + ":" + currMonthStr+ ":" + currDayStr;
    }
}
