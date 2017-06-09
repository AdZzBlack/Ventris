package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupView extends Fragment implements View.OnClickListener {

    private AddUserListAdapter adduseradapter;

    private EditText et_namagroup;
    private ListView lv_user;
    private Button btn_control;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_control, container, false);
        getActivity().setTitle("View Group");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_namagroup = (EditText) v.findViewById(R.id.et_namagroup);
        adduseradapter = new AddUserListAdapter(getActivity(), R.layout.list_adduser, new ArrayList<AddUserAdapter>());
        lv_user = (ListView) v.findViewById(R.id.lv_user);
        lv_user.setAdapter(adduseradapter);
        btn_control = (Button) v.findViewById(R.id.btn_control);
        btn_control.setText("Back");
        btn_control.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        et_namagroup.setText(GroupList.group_nama);

        String actionUrl = "Group/getDetailGroupTeam/";
        new getDetailGroupTeam().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_control){
            Fragment fragment = new GroupList();
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

    private class getDetailGroupTeam extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("group_nomor", GroupList.group_nomor);
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
                            String exists = (obj.getString("exists"));
                            String nomor = (obj.getString("intNomor"));
                            String nama = (obj.getString("vcNama"));
                            String jabatan = (obj.getString("vcJabatan"));
                            String hp = (obj.getString("vcHp"));
                            String latitude = (obj.getString("decLatitude"));
                            String longitude = (obj.getString("decLongitude"));

                            String lokasi = "";
                            if(!latitude.equals("0") || !latitude.equals("0")){
                                lokasi = GeocoderAddress.getKnownLocation(getContext(), latitude, longitude);
                            }
                            else{
                                lokasi = "-";
                            }

                            adduseradapter.add(new AddUserAdapter(Boolean.parseBoolean(exists), nomor, nama, jabatan, hp, lokasi, latitude, longitude));
                            adduseradapter.notifyDataSetChanged();
                        }
                    }
                }

                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "User Group Load Failed", Toast.LENGTH_LONG).show();
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