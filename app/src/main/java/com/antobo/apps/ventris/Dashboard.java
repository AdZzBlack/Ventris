package com.antobo.apps.ventris;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.R.attr.format;

public class Dashboard extends Fragment implements View.OnClickListener {

    private TextView tv_namauser, tv_omzet, tv_target;
    private ImageButton ib_contact, ib_salesorder, ib_groupmessage, ib_privatemessage, ib_nfctools, ib_stockreport, ib_scheduletask, ib_omzet, ib_target;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard, container, false);
        getActivity().setTitle("Dashboard");

        Index.settingsharedpreferences = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        String actionUrl = "Login/getAllSetting/";
        new getAllSetting().execute( actionUrl );

//         String actionUrlOmzet = "Omzettarget/getOmzetSalesTotal/";
//         new getOmzetSalesTotal().execute( actionUrlOmzet );

        tv_namauser = (TextView) v.findViewById(R.id.tv_namauser);
        tv_omzet = (TextView) v.findViewById(R.id.tv_omzet);
        tv_target = (TextView) v.findViewById(R.id.tv_target);
        ib_contact = (ImageButton) v.findViewById(R.id.ib_contact);
        ib_salesorder = (ImageButton) v.findViewById(R.id.ib_salesorder);
        ib_groupmessage = (ImageButton) v.findViewById(R.id.ib_groupmessage);
        ib_privatemessage = (ImageButton) v.findViewById(R.id.ib_privatemessage);
        ib_nfctools = (ImageButton) v.findViewById(R.id.ib_nfctools);
        ib_stockreport = (ImageButton) v.findViewById(R.id.ib_stockreport);
        ib_scheduletask = (ImageButton) v.findViewById(R.id.ib_scheduletask);
        ib_omzet = (ImageButton) v.findViewById(R.id.ib_omzet);
        ib_target = (ImageButton) v.findViewById(R.id.ib_target);

        tv_namauser.setText(Index.user_nama.toUpperCase());
        ib_contact.setOnClickListener(this);
        ib_salesorder.setOnClickListener(this);
        ib_groupmessage.setOnClickListener(this);
        ib_privatemessage.setOnClickListener(this);
        ib_nfctools.setOnClickListener(this);
        ib_stockreport.setOnClickListener(this);
        ib_scheduletask.setOnClickListener(this);
        ib_omzet.setOnClickListener(this);
        ib_target.setOnClickListener(this);

        return v;
    }
	
    @Override
    public void onResume(){
        super.onResume();

        if(Index.user_sales.equals("0") || Index.user_sales.equals("")){
            if (Index.user_jabatan.equals("IT"))
            {
                tv_omzet.setText("-");
                tv_target.setText("-");
            }
            else
            {
                String actionUrlOmzet = "Omzettarget/getOmzetSalesTotal/";
                new getOmzetSalesTotal().execute( actionUrlOmzet );
            }
        }else{
            String actionUrlOmzet = "Omzettarget/getOmzetSalesTotal/";
            new getOmzetSalesTotal().execute( actionUrlOmzet );
        }
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_contact){
            Fragment fragment = new ContactUser();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_salesorder){
            Fragment fragment = new SalesOrderList();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_groupmessage){
            Fragment fragment = new GroupList();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_privatemessage){
            Fragment fragment = new PrivateMessageUser();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_nfctools){
            Fragment fragment = new NFCTools();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_stockreport){
            Fragment fragment = new StockReportFilter();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.ib_scheduletask){
            if (Index.user_jabatan.equals("IT")) {
                Toast.makeText(getContext(), "You do not have permissions", Toast.LENGTH_LONG).show();
            }
            else
            {
                Fragment fragment = new ScheduleTaskList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(v.getId() == R.id.ib_omzet){
            if (Index.user_jabatan.equals("IT")) {
                Toast.makeText(getContext(), "You do not have permissions", Toast.LENGTH_LONG).show();
            }
            else
            {
                if (Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")) {
                    Fragment fragment = new OmzetReportSalesFilter();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    Fragment fragment = new OmzetReportFilter_new();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }
        else if(v.getId() == R.id.ib_target){
            if (Index.user_jabatan.equals("IT")) {
                Toast.makeText(getContext(), "You do not have permissions", Toast.LENGTH_LONG).show();
            }
            else
            {

            }
//            if (user_jabatan == "SALES") {
//                Fragment fragment = new TargetSalesFilter();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//            else{
//                Fragment fragment = new TargetFilter();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getAllSetting extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("data", "");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("settinggg", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            SharedPreferences.Editor editor = Index.settingsharedpreferences.edit();
                            editor.putString( "interval",             obj.getString("interval") );
                            editor.putString( "radius",                obj.getString("radius") );
                            editor.putString( "tracking",              obj.getString("tracking") );
                            editor.putString( "latitude",           obj.getString("latitude") );
                            editor.putString( "longitude",           obj.getString("longitude") );
                            editor.putString( "jam_awal",           obj.getString("jam_awal") );
                            editor.putString( "jam_akhir",           obj.getString("jam_akhir") );
                            editor.commit();
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private class getOmzetSalesTotal extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                if(!Index.user_sales.equals("0") && !Index.user_sales.equals("")){
                    Index.jsonObject.put("bex_nomor", Index.user_nomor);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePostReport(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            DecimalFormat format=new DecimalFormat("#,###.##");

                            Double Raw = Double.parseDouble(obj.getString("total_omzet"));
                            String omzet = String.valueOf(format.format(Raw));

                            tv_omzet.setText(omzet);
                        }
                    }
                }
                String actionUrlTarget = "Target/getDataTargetThisMonth/";
                new getTargetSalesTotal().execute( actionUrlTarget );
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Total Omzet Failed", Toast.LENGTH_LONG).show();
            }
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Index.pDialog = new ProgressDialog(getActivity());
//            Index.pDialog.setMessage("Loading...");
//            Index.pDialog.setCancelable(true);
//            Index.pDialog.show();
//        }
    }

    private class getTargetSalesTotal extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                if(!Index.user_sales.equals("0") && !Index.user_sales.equals("")){
                    Index.jsonObject.put("bex_nomor", Index.user_sales);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePostReport(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            DecimalFormat format=new DecimalFormat("#,###.##");

                            Double Raw = Double.parseDouble(obj.getString("decTarget"));
                            String target = String.valueOf(format.format(Raw));
                            tv_target.setText(target);
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Total Target Failed", Toast.LENGTH_LONG).show();
            }
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Index.pDialog = new ProgressDialog(getActivity());
//            Index.pDialog.setMessage("Loading...");
//            Index.pDialog.setCancelable(true);
//            Index.pDialog.show();
//        }
    }
}