package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupList extends Fragment implements View.OnClickListener {

    private FloatingActionButton btn_addgroup;

    private ListView lv_group;

    private ItemListAdapter itemadapter;

    public static String group_nomor, group_nama;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_list, container, false);
        getActivity().setTitle("Group List");

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_addgroup = (FloatingActionButton) v.findViewById(R.id.btn_addgroup);
        btn_addgroup.setOnClickListener(this);
        itemadapter = new ItemListAdapter(getActivity(), R.layout.list_item, new ArrayList<ItemAdapter>());
        lv_group = (ListView) v.findViewById(R.id.lv_group);
        lv_group.setAdapter(itemadapter);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                group_nomor = tv_nomor.getText().toString();
                group_nama = tv_nama.getText().toString();

                if(Index.positionsharedpreferences.getString("position", "").equals("Schedule Task")){
                    SharedPreferences.Editor editor = Index.scheduletasksharedpreferences.edit();
                    editor.putString( "nomorbex", tv_nomor.getText().toString());
                    editor.putString( "namabex", tv_nama.getText().toString());
                    editor.commit();

                    Fragment fragment = new ScheduleTaskAddReview();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    Index.globalfunction.group_nomor = group_nomor;
                    Index.globalfunction.group_nama = group_nama;

                    Fragment fragment = new GroupMessage();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        if(!Index.positionsharedpreferences.getString("position", "").equals("Schedule Task")){
            lv_group.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                    v.startAnimation(Index.listeffect);

                    TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                    TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                    group_nomor = tv_nomor.getText().toString();
                    group_nama = tv_nama.getText().toString();

                    if(Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")){
                        Fragment fragment = new GroupView();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else{
                        Fragment fragment = new GroupEdit();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    return true;
                }
            });
        }
        //-----END DECLARE---------------------------------------------------------------------------------------

        if(Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")){
            btn_addgroup.setVisibility(View.INVISIBLE);
        }

        String actionUrl = "Group/getGroupTeam/";
        new getGroupTeam().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_addgroup){
            Fragment fragment = new GroupAdd();
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

    private class getGroupTeam extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor", Index.user_nomor);
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
                            String nomor = obj.getString("group_nomor");
                            String nama = obj.getString("group_nama");

                            itemadapter.add(new ItemAdapter(nomor, nama));
                            itemadapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Group List Load Failed", Toast.LENGTH_LONG).show();
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