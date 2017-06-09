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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleTaskSalesList extends Fragment {

    private ListView lv_scheduletask;

    private ScheduleTaskListAdapter scheduletaskadapter;

    public static String scheduletask_nomor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_sales_list, container, false);
        getActivity().setTitle("Schedule Task");

        //---------------------------------------------------------

        lv_scheduletask = (ListView) v.findViewById(R.id.lv_scheduletask);

        scheduletaskadapter = new ScheduleTaskListAdapter(getActivity(), R.layout.list_scheduletask, new ArrayList<ScheduleTaskAdapter>());
        lv_scheduletask.setAdapter(scheduletaskadapter);

        String actionUrl = "Scheduletask/getScheduleTask/";
        new getScheduleTask().execute( actionUrl );

        lv_scheduletask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                scheduletask_nomor = tv_nomor.getText().toString();

                TextView tv_tipe = (TextView) v.findViewById(R.id.tv_tipe);

                if(tv_tipe.getText().toString().equals("STANDART")){
                    Fragment fragment = new ScheduleTaskSalesStandart();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(tv_tipe.getText().toString().equals("NFC")){
                    Fragment fragment = new ScheduleTaskSalesNFC();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        //---------------------------------------------------------

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("bex_nomor", Index.user_nomor);
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

                            String nomor = (obj.getString("jadwal_nomor"));
                            String managernomor = (obj.getString("manager_nomor"));
                            String tipe = (obj.getString("tipe"));
                            String tanggal = (obj.getString("tanggal"));
                            String jam = (obj.getString("jam"));
                            String namacustomer = obj.getString("customer_nama");
                            String nama = obj.getString("manager_nama");
                            String jenisjadwal = obj.getString("jenisjadwal");
                            String keterangan = (obj.getString("keterangan"));

                            scheduletaskadapter.add(new ScheduleTaskAdapter(nomor, managernomor, tipe, tanggal, jam, namacustomer, nama, jenisjadwal, keterangan));
                            scheduletaskadapter.notifyDataSetChanged();
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