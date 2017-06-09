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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OmzetReportFilter extends Fragment implements View.OnClickListener{

    private TextView tv_bex, tv_brand, tv_area, tv_startdate, tv_enddate;
    private Button btn_check;
    private DatePickerDialog dp_start, dp_end;

    private String nomorbex, namabex, nomorbrand, namabrand, nomorarea, namaarea, startdate, enddate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.omzetreport_filter, container, false);
        getActivity().setTitle("Omzet Report Filter");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_bex = (TextView) v.findViewById(R.id.tv_bex);
        tv_bex.setOnClickListener(this);
        tv_brand = (TextView) v.findViewById(R.id.tv_brand);
        tv_brand.setOnClickListener(this);
        tv_area = (TextView) v.findViewById(R.id.tv_area);
        tv_area.setOnClickListener(this);
        tv_startdate = (TextView) v.findViewById(R.id.tv_startdate);
        tv_startdate.setOnClickListener(this);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
        Calendar startDate = Calendar.getInstance();
        dp_start = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_startdate.setText(date);

                    SharedPreferences.Editor editor = Index.omzetsharedpreferences.edit();
                    editor.putString( "startdate", date);
                    editor.commit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
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
        nomorbrand = Index.omzetsharedpreferences.getString("nomorbrand", "");
        namabrand = Index.omzetsharedpreferences.getString("namabrand", "");
        nomorarea = Index.omzetsharedpreferences.getString("nomorarea", "");
        namaarea = Index.omzetsharedpreferences.getString("namaarea", "");
        startdate = Index.omzetsharedpreferences.getString("startdate", "");
        enddate = Index.omzetsharedpreferences.getString("enddate", "");

        tv_bex.setText(namabex);
        tv_brand.setText(namabrand);
        tv_area.setText(namaarea);
        tv_startdate.setText(startdate);
        tv_enddate.setText(enddate);

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Omzet Report");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_check){
            Fragment fragment = new OmzetReport();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_bex){
            Fragment fragment = new ChooseBEX();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_brand){
            Fragment fragment = new ChooseBrand();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_area){
            Fragment fragment = new ChooseArea();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_startdate){
            dp_start.show();
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