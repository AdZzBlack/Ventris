package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactUser extends Fragment implements View.OnClickListener {

    private EditText et_choosebex;
    private ImageButton ib_search;

    private ListView lv_user;

    private UserListAdapter useradapter;

    public static String user_nama, user_hp, user_nomor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_user, container, false);
        getActivity().setTitle("Contact List");

        //---------------------------------------------------------

        et_choosebex = (EditText) v.findViewById(R.id.et_choosebex);

        lv_user = (ListView) v.findViewById(R.id.lv_user);

        useradapter = new UserListAdapter(getActivity(), R.layout.list_user, new ArrayList<UserAdapter>());
        lv_user.setAdapter(useradapter);

        String actionUrl = "Master/alldatakontak/";
        new getUser().execute( actionUrl );

        if(Index.user_jabatan.equals("OWNER") || Index.user_jabatan.equals("CONTROLLER"))
        {
            lv_user.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    user_nomor =((TextView)view.findViewById(R.id.tv_nomor)).getText().toString();
                    alertbox("Reset Password", "Reset password " + user_nama, getActivity(), position);

                    return false;
                }

            });
        }

        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                TextView tv_hp = (TextView) v.findViewById(R.id.tv_hp);
                user_nama = tv_nama.getText().toString();
                user_hp = tv_hp.getText().toString();

                Index.contactalert.showDialog(getActivity(), "Contact Option(s)", "Contact " + user_nama, R.drawable.contact_dark, user_hp);
            }
        });

        ib_search = (ImageButton) v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);

        //---------------------------------------------------------

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_search){
            useradapter.clear();

            String actionUrl = "Master/alldatakontak/";
            new getUser().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void resetPass() {
        String actionUrl = "Login/resetPassword/";
        new resetPassword().execute( actionUrl );
    }

    private class getUser extends AsyncTask<String, Void, String> {
        String search = et_choosebex.getText().toString();
        
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor", Index.user_nomor);
                Index.jsonObject.put("search", search);
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
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
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

                            useradapter.add(new UserAdapter(nomor, nama, jabatan, hp, lokasi, latitude, longitude));
                            useradapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load User Failed", Toast.LENGTH_LONG).show();
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


    private class resetPassword extends AsyncTask<String, Void, String> {
        String search = et_choosebex.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor", user_nomor);
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
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        String success = obj.getString("success");
                        if(success.equals("true")){
                            Toast.makeText(getContext(), "Reset password Success", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Reset password failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }catch(Exception e)
            {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void alertbox(String title, String message, final Activity activity, final int pos) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                resetPass();
            } });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });
        alertDialog.show();
    }
}