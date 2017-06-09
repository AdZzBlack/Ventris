package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
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

public class OmzetReport_new extends Fragment implements View.OnClickListener{

    private OmzetReportListAdapter_new omzetreportadapter;

    private ListView lv_report;
    private Button btn_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.omzetreport_new, container, false);
        getActivity().setTitle("Omzet Report");

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_close = (Button) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        omzetreportadapter = new OmzetReportListAdapter_new(getActivity(), R.layout.list_omzetreport_new, new ArrayList<OmzetReportAdapter_new>());
        lv_report = (ListView) v.findViewById(R.id.lv_report);
        lv_report.setAdapter(omzetreportadapter);
        //-----END DECLARE---------------------------------------------------------------------------------------

        Log.d("1", Index.omzetsharedpreferences.getString("nomorbex", ""));
        Log.d("1", Index.omzetsharedpreferences.getString("type", ""));
        Log.d("1", Index.omzetsharedpreferences.getString("enddate", ""));
        String actionUrlTotal = "Omzettarget/getOmzetSalesTotalReport/";
        new getOmzetSalesTotal().execute( actionUrlTotal );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_close){
            getActivity().getSharedPreferences("omzet", Context.MODE_PRIVATE).edit().clear().commit();

            Fragment fragment = new OmzetReportFilter_new();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
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
                Index.jsonObject.put("bex_nomor", Index.omzetsharedpreferences.getString("nomorbex", "%"));
                Index.jsonObject.put("type", Index.omzetsharedpreferences.getString("type", ""));
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
            Log.d("res", result + "a");
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String sales = (obj.getString("sales"));
                            String omzet = (obj.getString("total_omzet"));
                            String target = (obj.getString("total_target"));

                            omzetreportadapter.add(new OmzetReportAdapter_new(sales, omzet, target));
                            omzetreportadapter.notifyDataSetChanged();
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Log.d("res", result);
                Toast.makeText(getContext(), "Load Total Omzet Failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}