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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SalesOrderAddHeaderKeterangan extends Fragment implements View.OnClickListener{

    private TextView tv_subtotal, tv_biaya, tv_totalum, tv_dpp, tv_ppnnominal, tv_sisa, tv_ppn;
    private Spinner sp_ppnexp;
    private EditText et_keterangansj, et_keteranganfj, et_discount, et_um1, et_um2, et_um3;
    private Button btn_back, btn_save;

    private Double subtotal, biaya, biayainternal, biayaestimasi, total_um, dpp, ppn, ppn_nominal, sisa, diskonpersen, diskon_nominal = 0.0;
    private Integer um1, um2, um3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_add_header_keterangan, container, false);
        getActivity().setTitle("Estimation Cost Sales Order");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_subtotal = (TextView) v.findViewById(R.id.tv_subtotal);
        tv_biaya = (TextView) v.findViewById(R.id.tv_biaya);
        tv_totalum = (TextView) v.findViewById(R.id.tv_totalum);
        tv_dpp = (TextView) v.findViewById(R.id.tv_dpp);
        tv_ppnnominal = (TextView) v.findViewById(R.id.tv_ppnnominal);
        tv_sisa = (TextView) v.findViewById(R.id.tv_sisa);
        tv_ppn = (TextView) v.findViewById(R.id.tv_ppn);
        sp_ppnexp = (Spinner) v.findViewById(R.id.sp_ppnexp);
        et_keterangansj = (EditText) v.findViewById(R.id.et_keterangansj);
        et_keteranganfj = (EditText) v.findViewById(R.id.et_keteranganfj);
        et_discount = (EditText) v.findViewById(R.id.et_discount);
        et_um1 = (EditText) v.findViewById(R.id.et_um1);
        et_um2 = (EditText) v.findViewById(R.id.et_um2);
        et_um3 = (EditText) v.findViewById(R.id.et_um3);
        btn_back = (Button) v.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        tv_subtotal.setText(Index.salesordersharedpreferences.getString("subtotal", ""));
        tv_biaya.setText(Index.salesordersharedpreferences.getString("biaya", ""));
        tv_totalum.setText(Index.salesordersharedpreferences.getString("totalum", ""));
        tv_dpp.setText(Index.salesordersharedpreferences.getString("dpp", ""));
        tv_ppnnominal.setText(Index.salesordersharedpreferences.getString("ppnnominal", ""));
        tv_sisa.setText(Index.salesordersharedpreferences.getString("sisa", ""));

        et_keterangansj.setText(Index.salesordersharedpreferences.getString("keterangansj", ""));
        et_keteranganfj.setText(Index.salesordersharedpreferences.getString("keteranganfj", ""));
        et_discount.setText(Index.salesordersharedpreferences.getString("diskon_header", ""));
        et_um1.setText(Index.salesordersharedpreferences.getString("um1_header", ""));
        et_um2.setText(Index.salesordersharedpreferences.getString("um2_header", ""));
        et_um3.setText(Index.salesordersharedpreferences.getString("um3_header", ""));

        et_discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "diskon_header", et_discount.getText().toString());
                    poseditor.commit();

                    calculateSubtotal();
                    calculateBiaya();
                    calculateAll();
                }
            }
        });

        et_um1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "um1_header", et_um1.getText().toString());
                    poseditor.commit();

                    calculateSubtotal();
                    calculateBiaya();
                    calculateAll();
                }
            }
        });

        et_um2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "um2_header", et_um2.getText().toString());
                    poseditor.commit();

                    calculateSubtotal();
                    calculateBiaya();
                    calculateAll();
                }
            }
        });

        et_um3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "um3_header", et_um3.getText().toString());
                    poseditor.commit();

                    calculateSubtotal();
                    calculateBiaya();
                    calculateAll();
                }
            }
        });

        sp_ppnexp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String selecteditem =  sp_ppnexp.getSelectedItem().toString();
                if(selecteditem.equals("PPN")){
                    tv_ppn.setText("10%");
                }else{
                    tv_ppn.setText("0");
                }

                calculateSubtotal();
                calculateBiaya();
                calculateAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        calculateSubtotal();
        calculateBiaya();
        calculateAll();

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_back){
            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString( "keterangansj", et_keterangansj.getText().toString());
            editor.putString( "keteranganfj", et_keteranganfj.getText().toString());
            editor.putString( "diskon_header", et_discount.getText().toString());
            editor.putString( "um1_header", et_um1.getText().toString());
            editor.putString( "um2_header", et_um2.getText().toString());
            editor.putString( "um3_header", et_um3.getText().toString());
            editor.commit();

            Fragment fragment = new SalesOrderAddGridBiayaInternal();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_save){
            calculateSubtotal();
            calculateBiaya();
            calculateAll();

            Index.jsonObject = new JSONObject();
            try {
                Index.jsonObject.put("top", Index.salesordersharedpreferences.getString("pembayaran", ""));
                Index.jsonObject.put("jatuhtempo", Index.salesordersharedpreferences.getString("jatuhtempo", ""));
                Index.jsonObject.put("tanggal", Index.salesordersharedpreferences.getString("tanggal", ""));
                Index.jsonObject.put("tanggalkirim", Index.salesordersharedpreferences.getString("tanggalkirim", ""));
                Index.jsonObject.put("keterangan", Index.salesordersharedpreferences.getString("keterangan", ""));
                Index.jsonObject.put("keteranganfj", Index.salesordersharedpreferences.getString("keteranganfj", ""));
                Index.jsonObject.put("keterangankw", Index.salesordersharedpreferences.getString("keterangansj", ""));
                Index.jsonObject.put("gabungan", "0");
                Index.jsonObject.put("cabang", Index.cabang_nomor);
                Index.jsonObject.put("gudang", Index.salesordersharedpreferences.getString("nomorgudang", ""));
                Index.jsonObject.put("customer", Index.salesordersharedpreferences.getString("nomorcustomer", ""));
                Index.jsonObject.put("sales", Index.user_nomor);
                Index.jsonObject.put("jenispenjualan", "0");
                Index.jsonObject.put("valuta", Index.salesordersharedpreferences.getString("nomorvaluta", ""));
                Index.jsonObject.put("biaya", biaya);
                Index.jsonObject.put("subtotal", subtotal);
                Index.jsonObject.put("kurs",  Index.salesordersharedpreferences.getString("kursvaluta", ""));
                Index.jsonObject.put("disc", diskonpersen);
                Index.jsonObject.put("discnominal", diskon_nominal);
                Index.jsonObject.put("ppn", ppn);
                Index.jsonObject.put("ppnnominal", ppn_nominal);
                Index.jsonObject.put("total", dpp);
                Index.jsonObject.put("totallama", dpp);
                Index.jsonObject.put("dpp", dpp);
                Index.jsonObject.put("totalumc", total_um);
                Index.jsonObject.put("sisa", sisa);
                Index.jsonObject.put("totalbiaya", biaya);
                Index.jsonObject.put("totalbiayainternal", biayainternal);
                Index.jsonObject.put("totalbiayaestimasi", biayaestimasi);
                Index.jsonObject.put("intuser", Index.user_nomor);
                Index.jsonObject.put("datalistbarang", Index.salesordersharedpreferences.getString("datalistbarang", ""));
                Index.jsonObject.put("datalistbiayaestimasi", Index.salesordersharedpreferences.getString("datalistbiayaestimasi", ""));
                Index.jsonObject.put("datalistbiayainternal", Index.salesordersharedpreferences.getString("datalistbiayainternal", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("coba", Index.jsonObject.toString());
            String actionUrl = "Orderjual/insertorderjual/";
            new createSalesOrder().execute( actionUrl );
            // do save
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void calculateSubtotal(){
        subtotal = 0.0;
        String datalistbarang = Index.salesordersharedpreferences.getString("datalistbarang", "");
        if(!datalistbarang.equals("")){
            String[] pieces = datalistbarang.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");
                subtotal = subtotal + Double.parseDouble(parts[13]);
            }
            tv_subtotal.setText(GlobalFunction.delimeter(String.valueOf(subtotal)));
        }
    }

    public void calculateBiaya(){
        biaya = 0.0;
        biayainternal = 0.0;
        biayaestimasi = 0.0;
        String datalistbiaya1 = Index.salesordersharedpreferences.getString("datalistbiayaestimasi", "");
        if(!datalistbiaya1.equals("")){
            String[] pieces = datalistbiaya1.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");
                biaya = biaya + Double.parseDouble(parts[9]);
                biayainternal = biayainternal + Double.parseDouble(parts[9]);
            }
        }

        String datalistbiaya2 = Index.salesordersharedpreferences.getString("datalistbiayainternal", "");
        if(!datalistbiaya2.equals("")){
            String[] pieces = datalistbiaya2.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");
                biaya = biaya + Double.parseDouble(parts[6]);
                biayaestimasi = biayaestimasi + Double.parseDouble(parts[6]);
            }
        }

        tv_biaya.setText(GlobalFunction.delimeter(String.valueOf(biaya)));
    }

    public void calculateAll(){

        try {
            um1 = Integer.parseInt(et_um1.getText().toString());
        }catch (Exception ex){
            um1 = 0;
        }
        try {
            um2 = Integer.parseInt(et_um2.getText().toString());
        }catch (Exception ex){
            um2 = 0;
        }
        try {
            um3 = Integer.parseInt(et_um3.getText().toString());
        }catch (Exception ex){
            um3 = 0;
        }
        try {
            diskon_nominal = Double.parseDouble(et_discount.getText().toString());
        }catch (Exception ex){
            diskon_nominal = 0.0;
        }

        try{
            total_um = Double.parseDouble(String.valueOf(um1)) + Double.parseDouble(String.valueOf(um2)) + Double.parseDouble(String.valueOf(um3));
            tv_totalum.setText(GlobalFunction.delimeter(String.valueOf(total_um)));
        }catch (Exception e){

        }
        try{
            dpp = subtotal - diskon_nominal - total_um;
            tv_dpp.setText(GlobalFunction.delimeter(String.valueOf(dpp)));
        }catch (Exception e){

        }

        try{
            ppn = 10.0;
            ppn_nominal = (dpp * 10) / 100;
            tv_ppnnominal.setText(GlobalFunction.delimeter(String.valueOf(ppn_nominal)));
            if(sp_ppnexp.getSelectedItem().toString().equals("Exp")){
                ppn = 0.0;
                tv_ppnnominal.setText("0");
            }
        }catch (Exception e){

        }

        try{
            sisa = dpp + ppn_nominal;
            tv_sisa.setText(GlobalFunction.delimeter(String.valueOf(sisa)));
        }catch (Exception e){

        }
    }



    private class createSalesOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("top", Index.salesordersharedpreferences.getString("pembayaran", ""));
                Index.jsonObject.put("jatuhtempo", Index.salesordersharedpreferences.getString("jatuhtempo", ""));
                Index.jsonObject.put("tanggal", Index.salesordersharedpreferences.getString("tanggal", ""));
                Index.jsonObject.put("tanggalkirim", Index.salesordersharedpreferences.getString("tanggalkirim", ""));
                Index.jsonObject.put("keterangan", Index.salesordersharedpreferences.getString("keterangan", ""));
                Index.jsonObject.put("keteranganfj", Index.salesordersharedpreferences.getString("keteranganfj", ""));
                Index.jsonObject.put("keterangankw", Index.salesordersharedpreferences.getString("keterangansj", ""));
                Index.jsonObject.put("gabungan", "0");
                Index.jsonObject.put("cabang", Index.cabang_nomor);
                Index.jsonObject.put("gudang", Index.salesordersharedpreferences.getString("nomorgudang", ""));
                Index.jsonObject.put("customer", Index.salesordersharedpreferences.getString("nomorcustomer", ""));
                Index.jsonObject.put("sales", Index.user_nomor);
                Index.jsonObject.put("jenispenjualan", "0");
                Index.jsonObject.put("valuta", Index.salesordersharedpreferences.getString("nomorvaluta", ""));
                Index.jsonObject.put("biaya", biaya);
                Index.jsonObject.put("subtotal", subtotal);
                Index.jsonObject.put("kurs",  Index.salesordersharedpreferences.getString("kursvaluta", ""));
                Index.jsonObject.put("disc", diskonpersen);
                Index.jsonObject.put("discnominal", diskon_nominal);
                Index.jsonObject.put("ppn", ppn);
                Index.jsonObject.put("ppnnominal", ppn_nominal);
                Index.jsonObject.put("total", dpp);
                Index.jsonObject.put("totallama", dpp);
                Index.jsonObject.put("dpp", dpp);
                Index.jsonObject.put("totalumc", total_um);
                Index.jsonObject.put("sisa", sisa);
                Index.jsonObject.put("totalbiaya", biaya);
                Index.jsonObject.put("totalbiayainternal", biayainternal);
                Index.jsonObject.put("totalbiayaestimasi", biayaestimasi);
                Index.jsonObject.put("intuser", Index.user_nomor);
                Index.jsonObject.put("datalistbarang", Index.salesordersharedpreferences.getString("datalistbarang", ""));
                Index.jsonObject.put("datalistbiayaestimasi", Index.salesordersharedpreferences.getString("datalistbiayaestimasi", ""));
                Index.jsonObject.put("datalistbiayainternal", Index.salesordersharedpreferences.getString("datalistbiayainternal", ""));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePostSales(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    String success = obj.getString("success");
                    if(success.equals("true")){
//                        getActivity().getSharedPreferences("scheduletask", Context.MODE_PRIVATE).edit().clear().commit();
//                        getActivity().getSharedPreferences("salesorder", Context.MODE_PRIVATE).edit().clear().commit();
//                        getActivity().getSharedPreferences("position", Context.MODE_PRIVATE).edit().clear().commit();

                        Toast.makeText(getContext(), "Sales Order Assigned", Toast.LENGTH_LONG).show();

                        Fragment fragment = new SalesOrderList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Assign Sales Order Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Assign Sales Order Failed", Toast.LENGTH_LONG).show();
                Index.pDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Index.pDialog = new ProgressDialog(getActivity());
            Index.pDialog.setMessage("Loading...");
            Index.pDialog.setCancelable(true);
            Index.pDialog.show();
        }
    }
}