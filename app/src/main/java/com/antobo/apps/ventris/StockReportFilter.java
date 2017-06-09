package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StockReportFilter extends Fragment implements View.OnClickListener{

    private TextView tv_gudang, tv_namabeli, tv_brand, tv_tipe, tv_group, tv_enddate;
    private Button btn_check;
    private ImageButton btn_gudang, btn_nama, btn_brand, btn_tipe, btn_group;
    private DatePickerDialog dp_end;

    private String nomorgudang, namagudang, nomorbarangnamabeli, namabarangnamabeli, nomorbrand, namabrand, nomortipe, namatipe, nomorgroup, namagroup, enddate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stockreport_filter, container, false);
        getActivity().setTitle("Stock Report Filter");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_gudang = (TextView) v.findViewById(R.id.tv_gudang);
        tv_gudang.setOnClickListener(this);
        tv_namabeli = (TextView) v.findViewById(R.id.tv_namabeli);
//        tv_namabeli.setOnClickListener(this);
        tv_brand = (TextView) v.findViewById(R.id.tv_brand);
        tv_brand.setOnClickListener(this);
        tv_tipe = (TextView) v.findViewById(R.id.tv_tipe);
        tv_tipe.setOnClickListener(this);
        tv_group = (TextView) v.findViewById(R.id.tv_group);
        tv_group.setOnClickListener(this);
        tv_gudang = (TextView) v.findViewById(R.id.tv_gudang);
        tv_gudang.setOnClickListener(this);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);

        btn_gudang = (ImageButton) v.findViewById(R.id.btn_clear_gudang);
        btn_gudang.setOnClickListener(this);
        btn_nama = (ImageButton) v.findViewById(R.id.btn_clear_nama);
//        btn_nama.setOnClickListener(this);
        btn_brand = (ImageButton) v.findViewById(R.id.btn_clear_brand);
        btn_brand.setOnClickListener(this);
        btn_tipe = (ImageButton) v.findViewById(R.id.btn_clear_tipe);
        btn_tipe.setOnClickListener(this);
        btn_group = (ImageButton) v.findViewById(R.id.btn_clear_group);
        btn_group.setOnClickListener(this);

        Calendar endDate = Calendar.getInstance();
        dp_end = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_enddate.setText(date);

                    SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
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

        nomorgudang = Index.stocksharedpreferences.getString("nomorgudang", "");
        namagudang = Index.stocksharedpreferences.getString("namagudang", "");
        nomorbarangnamabeli = Index.stocksharedpreferences.getString("nomorbarangnamabeli", "");
        namabarangnamabeli = Index.stocksharedpreferences.getString("namabarangnamabeli", "");
        nomorbrand = Index.stocksharedpreferences.getString("nomorbrand", "");
        namabrand = Index.stocksharedpreferences.getString("namabrand", "");
        nomortipe = Index.stocksharedpreferences.getString("nomortipe", "");
        namatipe = Index.stocksharedpreferences.getString("namatipe", "");
        nomorgroup = Index.stocksharedpreferences.getString("nomorgroupbarang", "");
        namagroup = Index.stocksharedpreferences.getString("namagroupbarang", "");
        enddate = Index.stocksharedpreferences.getString("enddate", "");

        tv_gudang.setText(namagudang);
        tv_namabeli.setText(namabarangnamabeli);
        tv_brand.setText(namabrand);
        tv_tipe.setText(namatipe);
        tv_group.setText(namagroup);
        tv_gudang.setText(namagudang);
        tv_enddate.setText(enddate);

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Stock Report");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_check){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "namabarangnamabeli", tv_namabeli.getText().toString());
            editor.commit();

//            if(nomorbarangnamabeli.equals(""))
//            {
//                Toast.makeText(getContext(), "Required Item Name Field", Toast.LENGTH_LONG).show();
//                return;
//            }

            if(tv_namabeli.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Required Item Name Field", Toast.LENGTH_LONG).show();
                return;
            }

            Fragment fragment = new StockReport();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_clear_gudang){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorgudang", "");
            editor.putString( "namagudang", "");
            editor.commit();
            tv_gudang.setText("");
        }
        else if(v.getId() == R.id.btn_clear_nama){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorbarangnamabeli", "");
            editor.putString( "namabarangnamabeli", "");
            editor.commit();
            tv_namabeli.setText("");
        }
        else if(v.getId() == R.id.btn_clear_brand){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorbrand", "");
            editor.putString( "namabrand", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.btn_clear_tipe){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomortipe", "");
            editor.putString( "namatipe", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.btn_clear_group){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorgroupbarang", "");
            editor.putString( "namagroupbarang", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.tv_gudang){
            Fragment fragment = new ChooseGudang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_namabeli){
            Fragment fragment = new ChooseNamaBeli();
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
        else if(v.getId() == R.id.tv_tipe){
            Fragment fragment = new ChooseTipe();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_group){
            Fragment fragment = new ChooseGroupBarang();
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