package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Setting extends Fragment implements View.OnClickListener {

    private EditText et_interval, et_radius;
    private Spinner sp_tracking;
    private TextView tv_starttracking, tv_endtracking;
    private Button btn_update;

    private TimePickerDialog tp_start, tp_end;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setting, container, false);
        getActivity().setTitle("Setting");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_interval = (EditText) v.findViewById(R.id.et_interval);
        et_radius = (EditText) v.findViewById(R.id.et_radius);
        sp_tracking = (Spinner) v.findViewById(R.id.sp_tracking);
        tv_starttracking = (TextView) v.findViewById(R.id.tv_starttracking);
        tv_starttracking.setOnClickListener(this);
        tv_endtracking = (TextView) v.findViewById(R.id.tv_endtracking);
        tv_endtracking.setOnClickListener(this);
        btn_update = (Button) v.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        Calendar startTime = Calendar.getInstance();
        tp_start = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                try {
                    String time = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date newtime = sdf.parse(time);
                    time = sdf.format(newtime);
                    tv_starttracking.setText(time);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), true);
        Calendar endTime = Calendar.getInstance();
        tp_end = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                try {
                    String time = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date newtime = sdf.parse(time);
                    time = sdf.format(newtime);
                    tv_endtracking.setText(time);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE), true);
        //-----END DECLARE---------------------------------------------------------------------------------------

        String actionUrl = "Login/getAllSetting/";
        new getAllSetting().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.tv_starttracking){
            tp_start.show();
        }
        else if(v.getId() == R.id.tv_endtracking){
            tp_end.show();
        }
        else if(v.getId() == R.id.btn_update){
            String actionUrl = "Login/updateSetting/";
            new updateSetting().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getAllSetting extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("data", "");
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
                            et_interval.setText(obj.getString("interval"));
                            et_radius.setText(obj.getString("radius"));
                            if(obj.getString("tracking").equals("GPS Only")){
                                sp_tracking.setSelection(0);
                            }
                            else{
                                sp_tracking.setSelection(1);
                            }
                            tv_starttracking.setText(obj.getString("jam_awal"));
                            tv_endtracking.setText(obj.getString("jam_akhir"));
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Setting Failed", Toast.LENGTH_LONG).show();
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

    private class updateSetting extends AsyncTask<String, Void, String> {
        String interval = et_interval.getText().toString();
        String radius = et_radius.getText().toString();
        String tracking = sp_tracking.getSelectedItem().toString();
        String jam_awal = tv_starttracking.getText().toString();
        String jam_akhir = tv_endtracking.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("interval", interval);
                Index.jsonObject.put("radius", radius);
                Index.jsonObject.put("tracking", tracking);
                Index.jsonObject.put("jam_awal", jam_awal);
                Index.jsonObject.put("jam_akhir", jam_akhir);
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
                        Toast.makeText(getContext(), "Setting Updated", Toast.LENGTH_LONG).show();

                        Fragment fragment = new Dashboard();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Update Setting Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Update Setting Failed", Toast.LENGTH_LONG).show();
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