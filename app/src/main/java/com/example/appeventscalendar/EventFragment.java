package com.example.appeventscalendar;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EventFragment extends Fragment {

    EditText txtNameEvent, txtDescripcion, txtDateStart, txtDateEnd, tmpEditText;
    Button btnCancel,btnConfirm;
    Spinner cmbTypeEvent;
    Switch switchAllDay;

    private DatePickerDialog.OnDateSetListener setListenerDate=
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month=month+1;
                    String date=dayOfMonth+"/"+month+"/"+year;
                    tmpEditText.setText(date);
                }
            };

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        txtNameEvent=rootView.findViewById(R.id.txtNameEvent);
        txtDescripcion=rootView.findViewById(R.id.txtDescripcion);
        txtDateStart=rootView.findViewById(R.id.txtDateStart);
        txtDateEnd=rootView.findViewById(R.id.txtDateEnd);
        cmbTypeEvent=rootView.findViewById(R.id.cmbTypeEvent);
        switchAllDay=rootView.findViewById(R.id.switchAllDay);
        btnCancel=rootView.findViewById(R.id.btnCancel);
        btnConfirm=rootView.findViewById(R.id.btnConfirm);

        final Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        txtDateStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tmpEditText=txtDateStart;
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),setListenerDate,year,month,day);
                datePickerDialog.show();
            }
        });
        txtDateEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tmpEditText=txtDateEnd;
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),setListenerDate,year,month,day);
                datePickerDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment= new HomeFragment();
                FragmentTransaction transaction= getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, homeFragment);
                transaction.commit();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateTitleEvent() & validateDateStart()& validateDateEnd(switchAllDay.isChecked()))
                {
                    Bundle bundle = new Bundle();
                    String result="";
                    if(switchAllDay.isChecked()) {
                        result = String.format("Tipo de evento: %s\nEvento: %s\nFecha: %s\nTodo el día\nDescripción: %s",
                                cmbTypeEvent.getSelectedItem().toString(),
                                txtNameEvent.getText().toString(),
                                txtDateStart.getText().toString(),
                                txtDescripcion.getText().toString());
                    }
                    else{
                        result = String.format("Tipo de evento: %s\nEvento: %s\nDel: %s\nAl: %s\nDescripción: %s",
                                cmbTypeEvent.getSelectedItem().toString(),
                                txtNameEvent.getText().toString(),
                                txtDateStart.getText().toString(),
                                txtDateEnd.getText().toString(),
                                txtDescripcion.getText().toString());
                    }

                    bundle.putString("DATA",result);
                    ResultNewEventFragment resultFragment= new ResultNewEventFragment();
                    FragmentTransaction transaction= getFragmentManager().beginTransaction();
                    resultFragment.setArguments(bundle);
                    transaction.replace(R.id.frame_layout, resultFragment);
                    transaction.commit();
                }
            }
        });

        return rootView;

    }

    public Boolean validateTitleEvent() {
        String val = txtNameEvent.getText().toString();
        if (val.isEmpty()) {
            txtNameEvent.setError("Error. Título del evento vacío.");
            return false;
        } else {
            txtNameEvent.setError(null);
            return true;
        }
    }

    public Boolean validateDateStart() {
        String val = txtDateStart.getText().toString();
        if (val.isEmpty()) {
            txtDateStart.setError("Error. Fecha de Inicio vacía.");
            return false;
        } else {
            if(validateAndParseDate(val,"dd/MM/yyyy")==null)
            {
                txtDateStart.setError("Error. Fecha de Inicio Incorrecta");
                return false;
            }
            else {
                txtDateStart.setError(null);
                return true;
            }
        }
    }

    public Boolean validateDateEnd(Boolean isChecked) {
        String val = txtDateEnd.getText().toString();
        if (val.isEmpty()) {
            if(isChecked)
            {
                txtDateEnd.setError(null);
                return true;
            }
            else
            {
                txtDateEnd.setError("Error. Fecha de Fin vacía.");
                return false;
            }
        } else {
            if(validateAndParseDate(val,"dd/MM/yyyy")==null)
            {
                txtDateEnd.setError("Error. Fecha de Fin Incorrecta");
                return false;
            }
            else {
                txtDateEnd.setError(null);
                return true;
            }
        }
    }

    public static Date validateAndParseDate(String dateString, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        format.setLenient(false); // Strict parsing, rejects invalid dates
        try {
            Date date = format.parse(dateString);
            return date; // Valid date, return the parsed Date object
        } catch (ParseException e) {
            // Invalid date format, handle the error
            return null; // Or throw an exception, depending on your requirements
        }
    }
}