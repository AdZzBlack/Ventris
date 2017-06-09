package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleTaskView extends Fragment implements View.OnClickListener {

    private TextView tv_namamanager, tv_namabex, tv_namacustomer, tv_date, tv_time, tv_keterangan, tv_tipe;
    private Button btn_done;

    private String nomormanager, namamanager, nomorbex, namabex, nomorcustomer, namacustomer, date, time, keterangan, tipe;

    public static String scheduletask_nomor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_view, container, false);
        getActivity().setTitle("Schedule Task Review");

        tv_namamanager = (TextView) v.findViewById(R.id.tv_namamanager);
        tv_namabex = (TextView) v.findViewById(R.id.tv_namabex);
        tv_namacustomer = (TextView) v.findViewById(R.id.tv_namacustomer);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_keterangan = (TextView) v.findViewById(R.id.tv_keterangan);
        tv_tipe = (TextView) v.findViewById(R.id.tv_tipe);

        btn_done = (Button) v.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);

        String actionUrl = "Scheduletask/getDetailDataScheduleTask/";
        new getDetailDataScheduleTask().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_done){
            Fragment fragment = new ScheduleTaskList();
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

    private class getDetailDataScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("jadwal_nomor", scheduletask_nomor);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = jsonarray.length() - 1; i >= 0; i--) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            namamanager = (obj.getString("manager_nama"));
                            namabex = (obj.getString("bex_nama"));
                            namacustomer = (obj.getString("customer_nama"));
                            date = (obj.getString("tanggal"));
                            time = (obj.getString("jam"));
                            keterangan = (obj.getString("keterangan"));
                            tipe = (obj.getString("tipe"));

                            tv_namamanager.setText(namamanager);
                            tv_namabex.setText(namabex);
                            tv_namacustomer.setText(namacustomer);
                            tv_date.setText(date);
                            tv_time.setText(time);
                            tv_keterangan.setText(keterangan);
                            tv_tipe.setText(tipe);
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Schedule Task Load Failed", Toast.LENGTH_LONG).show();
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