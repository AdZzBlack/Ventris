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

public class ScheduleTaskSalesStandart extends Fragment implements View.OnClickListener {

    private TextView tv_namamanager, tv_namacustomer, tv_keterangan;
    private Button btn_done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_sales_standart, container, false);
        getActivity().setTitle("Schedule Task Standart");

        tv_namamanager = (TextView) v.findViewById(R.id.tv_namamanager);
        tv_namacustomer = (TextView) v.findViewById(R.id.tv_namacustomer);
        tv_keterangan = (TextView) v.findViewById(R.id.tv_keterangan);

        btn_done = (Button) v.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);

        String actionUrl = "Scheduletask/getDetailDataScheduleTask/";
        new getDetailDataScheduleTask().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_done){
            String actionUrl = "Scheduletask/finishScheduleTask/";
            new finishScheduleTask().execute( actionUrl );
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
                    Index.jsonObject.put("jadwal_nomor", ScheduleTaskSalesList.scheduletask_nomor);
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
                                String manager_nama = (obj.getString("manager_nama"));
                                String customer_nama = (obj.getString("customer_nama"));
                                String keterangan = (obj.getString("keterangan"));

                                tv_namamanager.setText(manager_nama);
                                tv_namacustomer.setText(customer_nama);
                                tv_keterangan.setText(keterangan);
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

    private class finishScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("jadwal_nomor", ScheduleTaskSalesList.scheduletask_nomor);
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
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    String success = obj.getString("success");
                    if(success.equals("true")){
                        Toast.makeText(getContext(), "Schedule Task Completed", Toast.LENGTH_LONG).show();

                        Fragment fragment = new ScheduleTaskSalesList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Finish Schedule Task Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Finish Schedule Task Failed", Toast.LENGTH_LONG).show();
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