package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SalesTrackingUser extends Fragment implements View.OnClickListener {

    private EditText et_choosebex;
    private ImageButton ib_search;
    private ListView lv_user;

    private TextView tv_date;

    private UserListAdapter useradapter;

    public static String user_nomor, tanggal, latitude, longitude;

    private DatePickerDialog dp_date;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salestracking_user, container, false);
        getActivity().setTitle("User List");

        //---------------------------------------------------------

        et_choosebex = (EditText) v.findViewById(R.id.et_choosebex);

        lv_user = (ListView) v.findViewById(R.id.lv_user);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);

        useradapter = new UserListAdapter(getActivity(), R.layout.list_user, new ArrayList<UserAdapter>());
        lv_user.setAdapter(useradapter);

        // Define DatePicker
        Calendar newCalendar = Calendar.getInstance();
        dp_date = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_date.setText(dateformat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        String actionUrl = "Master/alldatakontak/";
        new getUser().execute( actionUrl );

        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_latitude = (TextView) v.findViewById(R.id.tv_latitude);
                TextView tv_longitude = (TextView) v.findViewById(R.id.tv_longitude);
                user_nomor = tv_nomor.getText().toString();
                latitude = tv_latitude.getText().toString();
                longitude = tv_longitude.getText().toString();
                tanggal = tv_date.getText().toString();

                Fragment fragment = new SalesTracking();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
        else if(v.getId() == R.id.tv_date){
            dp_date.show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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

                            if(!jabatan.equals("OWNER")){
                                useradapter.add(new UserAdapter(nomor, nama, jabatan, hp, lokasi, latitude, longitude));
                                useradapter.notifyDataSetChanged();
                            }
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
}