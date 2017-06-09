package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OmzetReportFilter_new extends Fragment implements View.OnClickListener{

    private TextView tv_bex, tv_enddate;
    private RadioButton rb_bulan, rb_tahun;
    private Button btn_check;
    private ImageButton btn_clear_bex;
    private DatePickerDialog dp_end;

    private String nomorbex, namabex, type, enddate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.omzetreport_filter_new, container, false);
        getActivity().setTitle("Omzet Report Filter");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_bex = (TextView) v.findViewById(R.id.tv_bex);
        tv_bex.setOnClickListener(this);
        rb_bulan = (RadioButton) v.findViewById(R.id.rb_bulan);
        rb_tahun = (RadioButton) v.findViewById(R.id.rb_tahun);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
        btn_clear_bex = (ImageButton) v.findViewById(R.id.btn_clear_bex);
        btn_clear_bex.setOnClickListener(this);

        Calendar endDate = Calendar.getInstance();
        dp_end = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_enddate.setText(date);

                    SharedPreferences.Editor editor = Index.omzetsharedpreferences.edit();
                    editor.putString( "enddate", date);
                    editor.commit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        //-----END DECLARE---------------------------------------------------------------------------------------

        nomorbex = Index.omzetsharedpreferences.getString("nomorbex", "");
        namabex = Index.omzetsharedpreferences.getString("namabex", "");
        type = Index.omzetsharedpreferences.getString("type", "");
        enddate = Index.omzetsharedpreferences.getString("enddate", "");

        tv_bex.setText(namabex);
        tv_enddate.setText(enddate);
        if(type.equals("bulan"))
        {
            rb_bulan.setChecked(true);
        }
        else if(type.equals("tahun"))
        {
            rb_tahun.setChecked(true);
        }

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Omzet Report");
        poseditor.commit();

        SharedPreferences.Editor editor = Index.omzetsharedpreferences.edit();
        if(rb_bulan.isChecked())
        {
            editor.putString( "type", "bulan");
        }
        else if(rb_tahun.isChecked())
        {
            editor.putString( "type", "tahun");
        }
        editor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_check){
            if(Index.omzetsharedpreferences.getString("type", "").equals("") && Index.omzetsharedpreferences.getString("enddate", "").equals(""))
            {
                Toast.makeText(getContext(), "End date and type required", Toast.LENGTH_LONG).show();
            }
            else {
                Fragment fragment = new OmzetReport_new();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(v.getId() == R.id.btn_clear_bex){
            editor.putString( "nomorbex", "");
            editor.putString( "namabex", "");
            tv_bex.setText("");
            editor.commit();
        }
        else if(v.getId() == R.id.tv_bex){
            Fragment fragment = new ChooseBEX();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_enddate){
            dp_end.show();
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