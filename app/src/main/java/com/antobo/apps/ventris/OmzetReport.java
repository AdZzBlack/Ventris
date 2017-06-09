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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OmzetReport extends Fragment implements View.OnClickListener{

    private OmzetReportListAdapter omzetreportadapter;

    private ListView lv_report;
    private TextView tv_totalomzet;
    private Button btn_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.omzetreport, container, false);
        getActivity().setTitle("Omzet Report");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_totalomzet = (TextView) v.findViewById(R.id.tv_totalomzet);
        btn_close = (Button) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        omzetreportadapter = new OmzetReportListAdapter(getActivity(), R.layout.list_omzetreport, new ArrayList<OmzetReportAdapter>());
        lv_report = (ListView) v.findViewById(R.id.lv_report);
        lv_report.setAdapter(omzetreportadapter);
        //-----END DECLARE---------------------------------------------------------------------------------------

        String actionUrlTotal = "Omzettarget/getOmzetSalesTotal/";
        new getOmzetSalesTotal().execute( actionUrlTotal );

        Log.d("nomor", Index.omzetsharedpreferences.getString("nomorbex", ""));
        String actionUrl = "Omzettarget/getOmzetSales/";
        new getOmzetSales().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_close){
            getActivity().getSharedPreferences("omzet", Context.MODE_PRIVATE).edit().clear().commit();

            Fragment fragment = new Dashboard();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getOmzetSalesTotal extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject.put("bex_nomor", Index.omzetsharedpreferences.getString("nomorbex", ""));
                Index.jsonObject.put("brand_nomor", Index.omzetsharedpreferences.getString("nomorbrand", ""));
                Index.jsonObject.put("area_nomor", Index.omzetsharedpreferences.getString("nomorarea", ""));
                Index.jsonObject.put("tgl_awal", Index.omzetsharedpreferences.getString("startdate", ""));
                Index.jsonObject.put("tgl_akhir", Index.omzetsharedpreferences.getString("enddate", ""));
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
                            tv_totalomzet.setText(Index.globalfunction.delimeter(obj.getString("total_omzet")));
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Total Omzet Failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private class getOmzetSales extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("bex_nomor", Index.omzetsharedpreferences.getString("nomorbex", ""));
                Index.jsonObject.put("brand_nomor", Index.omzetsharedpreferences.getString("nomorbrand", ""));
                Index.jsonObject.put("area_nomor", Index.omzetsharedpreferences.getString("nomorarea", ""));
                Index.jsonObject.put("tgl_awal", Index.omzetsharedpreferences.getString("startdate", ""));
                Index.jsonObject.put("tgl_akhir", Index.omzetsharedpreferences.getString("enddate", ""));
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
                            String brand = (obj.getString("vcNamaBrand"));
                            String area = (obj.getString("vcNamaArea"));
                            String customer = (obj.getString("vcNamaCustomer"));
                            String dpp = (obj.getString("decDPP"));

                            omzetreportadapter.add(new OmzetReportAdapter(brand, dpp, customer, area));
                            omzetreportadapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Report Failed", Toast.LENGTH_LONG).show();
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