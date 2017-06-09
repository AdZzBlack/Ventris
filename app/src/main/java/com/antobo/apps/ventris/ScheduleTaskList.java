package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleTaskList extends Fragment implements View.OnClickListener {

    private FloatingActionButton btn_addscheduletask;
    private ListView lv_scheduletask;
    private ScheduleTaskListAdapter scheduletaskadapter;

    private String nomor_delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_list, container, false);
        getActivity().setTitle("Schedule Task");

        //---------------------------------------------------------

        btn_addscheduletask = (FloatingActionButton) v.findViewById(R.id.btn_addscheduletask);
        btn_addscheduletask.setOnClickListener(this);

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
                ScheduleTaskReview.scheduletask_nomor = tv_nomor.getText().toString();
                ScheduleTaskView.scheduletask_nomor = tv_nomor.getText().toString();

                TextView tv_managernomor = (TextView) v.findViewById(R.id.tv_managernomor);
                String managernomor = tv_managernomor.getText().toString();

                TextView tv_jenis = (TextView) v.findViewById(R.id.tv_jenisjadwal);
                String jenis = tv_jenis.getText().toString();

                if(jenis.equals("Group Meeting") || jenis.equals("Meeting"))
                {
                    if(Integer.parseInt(Index.user_nomor) == Integer.parseInt(managernomor))
                    {
                        Fragment fragment = new ScheduleTaskReview();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Fragment fragment = new ScheduleTaskView();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                else
                {
                    if(Integer.parseInt(Index.user_nomor) == Integer.parseInt(managernomor))
                    {
                        Fragment fragment = new ScheduleTaskReview();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Fragment fragment = new ScheduleTaskView();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            }
        });
        lv_scheduletask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                nomor_delete = tv_nomor.getText().toString();

                TextView tv_managernomor = (TextView) v.findViewById(R.id.tv_managernomor);
                String managernomor = tv_managernomor.getText().toString();

                if(Integer.parseInt(Index.user_nomor) == Integer.parseInt(managernomor)){
                    alertbox("Delete Schedule Task", "Are you sure?", getActivity(), position);
//                Index.yesnoalert.showDialog(getActivity(), "Delete Schedule Task", "Anda yakin?", R.drawable.scheduletask_dark, "Delete Schedule Task");
                }

                return true;
            }
        });

        //---------------------------------------------------------

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_addscheduletask){
            Fragment fragment = new ScheduleTaskAdd();
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

    private class getScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                if (Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")) {
                    Index.jsonObject.put("bex_nomor", Index.user_nomor);
                }else{
                    Index.jsonObject.put("manager_nomor", Index.user_nomor);
                }
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
                Log.d("result", result);
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = jsonarray.length() - 1; i >= 0; i--) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String nomor = obj.getString("jadwal_nomor");
                            String managernomor = obj.getString("manager_nomor");
                            String tipe = obj.getString("tipe");
                            String tanggal = obj.getString("tanggal");
                            String jam = obj.getString("jam");
                            String namacustomer = obj.getString("customer_nama");
                            String nama;
                            if (Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")) {
                                nama = obj.getString("manager_nama");
                            }else{
                                nama = obj.getString("bex_nama");
                            }
                            String jenisjadwal = obj.getString("jenisjadwal");
                            String keterangan = obj.getString("keterangan");

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            Date newdate = sdf.parse(jam);
                            jam = sdf.format(newdate);

                            scheduletaskadapter.add(new ScheduleTaskAdapter(nomor, managernomor, tipe, tanggal, jam, namacustomer, nama, jenisjadwal, keterangan));
                            scheduletaskadapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                Log.d("result", result);
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

    public class deleteScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("jadwal_nomor", nomor_delete);
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
                        Toast.makeText(getContext(), "Schedule Task Deleted", Toast.LENGTH_LONG).show();

                        Fragment fragment = new ScheduleTaskList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getActivity(), "Delete Schedule Task Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Delete Schedule Task Failed", Toast.LENGTH_LONG).show();
                Index.pDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                super.onPreExecute();
                Index.pDialog = new ProgressDialog(getActivity());
                Index.pDialog.setMessage("Loading...");
                Index.pDialog.setCancelable(true);
                Index.pDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void alertbox(String title, String message, final Activity activity, final int pos) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String actionUrl = "Scheduletask/deleteScheduleTask/";
                new deleteScheduleTask().execute( actionUrl );
            } });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });
        alertDialog.show();
    }
}