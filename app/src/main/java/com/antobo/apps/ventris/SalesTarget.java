package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class SalesTarget extends Fragment implements View.OnClickListener {

    private EditText et_target, et_bulan, et_tahun;
    private TextView tv_sales;
    private Button btn_set, btn_add;

    private DatePickerDialog dp_periode;

    private ListView lv_gridsales;
    private GridSalesListAdapter gridsalesadapter;
    private Set<String> setData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salestarget, container, false);
        getActivity().setTitle("Sales Target");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_target = (EditText) v.findViewById(R.id.et_target);
        tv_sales = (TextView) v.findViewById(R.id.tv_sales);
        tv_sales.setOnClickListener(this);
        et_bulan = (EditText) v.findViewById(R.id.et_bulan);
        et_bulan.setOnClickListener(this);
        et_tahun = (EditText) v.findViewById(R.id.et_tahun);
        et_tahun.setOnClickListener(this);
        btn_set = (Button) v.findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);
        btn_add = (Button) v.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        tv_sales.setText(Index.salestargetsharedpreferences.getString("namabex", ""));
        et_target.setText(Index.salestargetsharedpreferences.getString("target", ""));
        et_bulan.setText(Index.salestargetsharedpreferences.getString("bulan", ""));
        et_tahun.setText(Index.salestargetsharedpreferences.getString("tahun", ""));

        gridsalesadapter = new GridSalesListAdapter(getActivity(), R.layout.list_grid_sales, new ArrayList<GridSalesAdapter>());
        lv_gridsales = (ListView) v.findViewById(R.id.lv_gridsales);
        lv_gridsales.setAdapter(gridsalesadapter);

        lv_gridsales.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                alertbox("Sales Detail", "Delete Sales Target ?", getActivity(), pos);
                return true;
            }
        });
        //-----END DECLARE---------------------------------------------------------------------------------------

        refreshGrid();
        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Sales Target");
        poseditor.commit();

        SharedPreferences.Editor editor = Index.salestargetsharedpreferences.edit();
        editor.putString( "target", et_target.getText().toString());
        editor.putString( "bulan", et_bulan.getText().toString());
        editor.putString( "tahun", et_tahun.getText().toString());
        editor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.tv_sales){
            Fragment fragment = new ChooseBEX();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_add) {

            String nomor = Index.salestargetsharedpreferences.getString("nomorbex", "");
            String nama = Index.salestargetsharedpreferences.getString("namabex", "");
            String target = Index.salestargetsharedpreferences.getString("target", "");
            String bulan = Index.salestargetsharedpreferences.getString("bulan", "");
            String tahun = Index.salestargetsharedpreferences.getString("tahun", "");

            String dataOldListSales = Index.salestargetsharedpreferences.getString("datalistsales", "");

            Boolean same = false;
            if (!dataOldListSales.equals("")) {
                String[] pieces = dataOldListSales.trim().split("\\|");
                for (int i = 0; i < pieces.length; i++) {
                    String string = pieces[i];
                    String[] parts = string.trim().split("\\~");

                    if (nomor.equals(parts[0]) && bulan.equals(parts[3]) && tahun.equals(parts[4])) {
                        same = true;
                    }
                }
            }

            if (same)
            {
                Toast.makeText(getContext(), "Sales already targeted", Toast.LENGTH_LONG).show();
            }
            else
            {
                SharedPreferences.Editor editor1 = Index.salestargetsharedpreferences.edit();
                editor1.putString("datalistsales", dataOldListSales + nomor + "~" + nama + "~" + target + "~" + bulan + "~" + tahun + "|");
                editor1.commit();
                gridsalesadapter.add(new GridSalesAdapter(nomor, nama, target, bulan, tahun));
                gridsalesadapter.notifyDataSetChanged();
            }
        }
        else if(v.getId() == R.id.btn_set){
            String actionUrl = "Target/createTarget/";
            new createTarget().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class createTarget extends AsyncTask<String, Void, String> {
        String bulan = et_bulan.getText().toString();
        String tahun = et_tahun.getText().toString();
        String datalisttarget = Index.salestargetsharedpreferences.getString("datalistsales", "");


        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor", Index.user_nomor);
                Index.jsonObject.put("bulan", bulan);
                Index.jsonObject.put("tahun", tahun);
                Index.jsonObject.put("datalisttarget", datalisttarget);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resultttt", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    String success = obj.getString("success");
                    if(success.equals("true")){
                        Toast.makeText(getContext(), "Create Target Success", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = Index.salestargetsharedpreferences.edit();
                        editor.clear();
                        editor.commit();

                        Fragment fragment = new Dashboard();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Create Target Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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

    public void refreshGrid(){
        gridsalesadapter.clear();
        String datalistbarang = Index.salestargetsharedpreferences.getString("datalistsales", "");
        if(!datalistbarang.equals("")){
            String[] pieces = datalistbarang.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");

                gridsalesadapter.add(new GridSalesAdapter(parts[0], parts[1], parts[2], parts[3], parts[4]));
                gridsalesadapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteGrid(final int pos){
        String realData = "";
        String datalistbarang = Index.salestargetsharedpreferences.getString("datalistsales", "");
        if(!datalistbarang.equals("")){
            String[] pieces = datalistbarang.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                if(i != pos){
                    realData += pieces[i] + "|";
                }
            }
            SharedPreferences.Editor editor = Index.salestargetsharedpreferences.edit();
            editor.putString( "datalistsales", realData);
            editor.commit();

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    refreshGrid();
                }
            }, 300);
        }
    }

    public void alertbox(String title, String message, final Activity activity, final int pos) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteGrid(pos);
            } });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });
        alertDialog.show();
    }
}