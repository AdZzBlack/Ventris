package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleTaskAdd extends Fragment implements View.OnClickListener {

    private Spinner sp_tipepekerjaan, sp_jenisjadwal;
    private TextView tv_date, tv_time;
    private EditText et_keterangan, et_reminder;
    private Button btn_next;

    private DatePickerDialog dp_date;
    private TimePickerDialog tp_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_add, container, false);
        getActivity().setTitle("Create Schedule Task");

        sp_tipepekerjaan = (Spinner) v.findViewById(R.id.sp_tipepekerjaan);
        sp_jenisjadwal = (Spinner) v.findViewById(R.id.sp_jenisjadwal);

        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);

        et_keterangan = (EditText) v.findViewById(R.id.et_keterangan);
        et_reminder = (EditText) v.findViewById(R.id.et_reminder);

        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        // Define DatePicker
        Calendar newCalendar = Calendar.getInstance();
        dp_date = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);

                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                    Date daysBeforeDate = cal.getTime();

                    if(daysBeforeDate.getTime() > newdate.getTime())
                    {
                        tv_date.setText("[Date]");
                    }
                    else
                    {
                        tv_date.setText(date);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dp_date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Define TimePicker
        Calendar newTime = Calendar.getInstance();
        tp_time = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                try {
                    String time = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date newtime = sdf.parse(time);
                    time = sdf.format(newtime);
                    tv_time.setText(time);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.tv_date){
            dp_date.show();
        }
        else if(v.getId() == R.id.tv_time){
            tp_time.show();
        }
        else if(v.getId() == R.id.btn_next){
            SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
            poseditor.putString( "position", "Schedule Task");
            poseditor.commit();

            SharedPreferences.Editor editor = Index.scheduletasksharedpreferences.edit();
            editor.putString( "date", tv_date.getText().toString());
            editor.putString( "time", tv_time.getText().toString());
            editor.putString( "reminder", et_reminder.getText().toString());
            editor.putString( "keterangan", et_keterangan.getText().toString());
            editor.putString( "tipe", sp_tipepekerjaan.getSelectedItem().toString());
            editor.putString( "jenisjadwal", sp_jenisjadwal.getSelectedItem().toString());
            editor.commit();

            if(!tv_date.getText().toString().equals("[Date]") || !tv_time.getText().toString().equals("[Time]"))
            {
                if(sp_jenisjadwal.getSelectedItem().toString().equals("Group Meeting"))
                {
                    SharedPreferences.Editor editorTask = Index.scheduletasksharedpreferences.edit();
                    editorTask.putString( "nomorbex", Index.user_nomor);
                    editorTask.putString( "namabex", Index.user_nama);
                    editorTask.commit();

                    Fragment fragment = new GroupList();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else
                {
                    if (Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")) {
                        SharedPreferences.Editor editorTask = Index.scheduletasksharedpreferences.edit();
                        editorTask.putString( "nomorbex", Index.user_nomor);
                        editorTask.putString( "namabex", Index.user_nama);
                        editorTask.commit();

                        if(sp_jenisjadwal.getSelectedItem().toString().equals("Custom Note") || sp_jenisjadwal.getSelectedItem().toString().equals("Meeting"))
                        {
                            Fragment fragment = new ScheduleTaskAddReview();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else if(sp_jenisjadwal.getSelectedItem().toString().equals("Prospecting Customer"))
                        {
                            Fragment fragment = new ChooseProspectingCustomer();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else
                        {
                            Fragment fragment = new ChooseCustomer();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }else{
                        Fragment fragment = new ChooseBEX();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            }
            else
            {
                Toast.makeText(getContext(), "Some field required", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}