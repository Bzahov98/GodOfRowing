package com.bzahov.elsys.godofrowing.Fragments.Dialogs;

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

import java.util.Calendar;

/**
 * God of Rowing
 * Created by B. Zahov on 23.04.18.
 */
public class DatePickerDialog extends DialogFragment {

    private TextView startDate;
    private TextView endDate;
    private DatePicker myDatePicker;
    private ImageButton startDateBtn;
    private ImageButton endDateBtn;
    private int currYear=1;
    private int currMonth=1;
    private int currDay=2000;
    private String currYearStr="01";
    private String currMonthStr="01";
    private String currDayStr="2000";
    private String currDateStr=  "2000:01:01";
    private Calendar c;
    private ImageButton startEraseBtn;
    private ImageButton endEraseBtn;
    private RowApplication app = RowApplication.getInstance();
    private Button doneBtn;
    private Button cancelBtn;

    public DatePickerDialog(){

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
        setDefaultDates();
        setListeners();

    }

    private void setListeners() {
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateSequences(myDatePicker);
                if (isDateBefore(c)){
                    startDate.setText(currDateStr);
                }else Toast.makeText(getContext(), app.getString(R.string.err_date_before_today),Toast.LENGTH_SHORT).show();
            }
        });
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateSequences(myDatePicker);
                endDate.setText(currDateStr);
                //}else Toast.makeText(getContext(), app.getString(R.string.err_date_before_today),Toast.LENGTH_SHORT).show();

            }
        });

        startEraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateSequences(1,1,2010);
                startDate.setText(currDateStr);
            }
        });
        endEraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateSequences(c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH),c.get(Calendar.YEAR));
                endDate.setText(currDateStr);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // .. TODO Connect with Fragment
                getDialog().dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void setViews(View view) {
        myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);
        startDate = (TextView)view.findViewById(R.id.start_date_head_text_date);
        endDate = (TextView)view.findViewById(R.id.end_date_head_text_date);

        startDateBtn = (ImageButton) view.findViewById(R.id.start_date_parameter_image_end);
        endDateBtn = (ImageButton) view.findViewById(R.id.end_date_parameter_image_end);

        startEraseBtn = (ImageButton) view.findViewById(R.id.start_date_parameter_image_factory);
        endEraseBtn = (ImageButton) view.findViewById(R.id.end_date_parameter_image_factory);

        doneBtn = (Button) view.findViewById(R.id.range_btn_done);
        cancelBtn = (Button) view.findViewById(R.id.range_btn_cancel);


        c = Calendar.getInstance();
        myDatePicker.setCalendarViewShown(false);
    }

    private void setDefaultDates() {
        setDateSequences(1,1,2010);
        startDate.setText(currDateStr);

        setDateSequences(c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH),c.get(Calendar.YEAR));
        endDate.setText(currDateStr);

        myDatePicker.setMaxDate(c.getTimeInMillis());
    }

    private boolean isDateBefore(Calendar calendar) {
        if (currYear<=calendar.get(Calendar.YEAR)){
            if (currMonth<=calendar.get(Calendar.MONTH)){
                if (currDay<= calendar.get(Calendar.DAY_OF_MONTH)){
                    return true;
                }
            }
        }return false;
    }

    private boolean isDateAfter(Calendar calendar) {
        if (currYear>=calendar.get(Calendar.YEAR)){
            if (currMonth>=calendar.get(Calendar.MONTH)){
                if (currDay > calendar.get(Calendar.DAY_OF_MONTH)){
                    return true;
                }
            }
        }return false;
    }

    private void setDateSequences(int day, int month, int year) {
        currDay = day;
        currDayStr =formatToStr(currDay);
        currMonth = month;
        currMonthStr = formatToStr(currMonth+1);
        currYear = year;
        currYearStr = String.valueOf(currYear);
        currDateStr = currYearStr + ":" + currMonthStr+ ":" + currDayStr;
    }

    private void getDateSequences(DatePicker datePicker){
        currDay = datePicker.getDayOfMonth();
        currDayStr =formatToStr(currDay);
        currMonth = datePicker.getMonth();
        currMonthStr = formatToStr(currMonth+1);
        currYear = datePicker.getYear();
        currYearStr = String.valueOf(currYear);
        currDateStr = currYearStr + ":" + currMonthStr+ ":" + currDayStr;
    }

    private String formatToStr(int num){
        if (num<10){
             return "0"+ String.valueOf(num);
        }else
            return String.valueOf(num);
    }
    /*@Override
    public View onCreateView(Bundle savedInstanceState) {
        final String userEmail = getArguments().getString("userEmail");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_date_range, null, false);
        final DatePicker myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);
        myDatePicker.setCalendarViewShown(false);

        new AlertDialog.Builder(getActivity()).setView(view)
                .setTitle("Set Date: ")
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        int month = myDatePicker.getMonth() + 1;
                        int day = myDatePicker.getDayOfMonth();
                        int year = myDatePicker.getYear();

                        Toast.makeText(getActivity().getApplicationContext(),"" + month + " "+ day+ " "+ year,Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }

                }).show();
       /* return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon_logo)
                .setTitle("Hello ")
                // Set Dialog Message
                .setMessage("")

                // Positive button
                .setPositiveButton("OK, log out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ...
                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {

                    }
                }).create();
    }*/
}
