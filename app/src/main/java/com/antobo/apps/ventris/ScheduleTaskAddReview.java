package com.antobo.apps.ventris;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleTaskAddReview extends Fragment implements View.OnClickListener {

    private TextView tv_namabex, tv_namacustomer, tv_date, tv_time, tv_reminder, tv_keterangan, tv_tipe, tv_jenisjadwal;
    private Button btn_assign;

    private String nomorbex, namabex, nomorcustomer, namacustomer, date, time, reminder, keterangan, tipe, jenisjadwal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_add_review, container, false);
        getActivity().setTitle("Create Schedule Task Review");

        tv_namabex = (TextView) v.findViewById(R.id.tv_namabex);
        tv_namacustomer = (TextView) v.findViewById(R.id.tv_namacustomer);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_reminder = (TextView) v.findViewById(R.id.tv_reminder);
        tv_keterangan = (TextView) v.findViewById(R.id.tv_keterangan);
        tv_tipe = (TextView) v.findViewById(R.id.tv_tipe);
        tv_jenisjadwal = (TextView) v.findViewById(R.id.tv_jenisjadwal);

        btn_assign = (Button) v.findViewById(R.id.btn_assign);
        btn_assign.setOnClickListener(this);
        nomorbex = Index.scheduletasksharedpreferences.getString("nomorbex", "");
        namabex = Index.scheduletasksharedpreferences.getString("namabex", "");
        nomorcustomer = Index.scheduletasksharedpreferences.getString("nomorcustomer", "");
        namacustomer = Index.scheduletasksharedpreferences.getString("namacustomer", "-");
        date = Index.scheduletasksharedpreferences.getString("date", "");
        time = Index.scheduletasksharedpreferences.getString("time", "");
        reminder = Index.scheduletasksharedpreferences.getString("reminder", "");
        keterangan = Index.scheduletasksharedpreferences.getString("keterangan", "");
        tipe = Index.scheduletasksharedpreferences.getString("tipe", "");
        jenisjadwal = Index.scheduletasksharedpreferences.getString("jenisjadwal", "");

        tv_namabex.setText(namabex);
        tv_namacustomer.setText(namacustomer);
        tv_date.setText(date);
        tv_time.setText(time);
        tv_reminder.setText(reminder + " Menit");
        tv_keterangan.setText(keterangan);
        tv_tipe.setText(tipe);
        tv_jenisjadwal.setText(jenisjadwal);

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_assign){
            Toast.makeText(getContext(), nomorbex, Toast.LENGTH_LONG).show();
            String actionUrl = "Scheduletask/createScheduleTask/";
            new createScheduleTask().execute( actionUrl );
        }
    }

    private void setAlarm(){
//        Context context = getActivity();
//        Calendar myCal = Calendar.getInstance();
//
//        String givenDateString = "2016-11-16 15:12";
//        givenDateString = date + " " + time;
//        Toast.makeText(getActivity(), givenDateString, Toast.LENGTH_SHORT).show();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        long timeInMilliseconds = 0;
//        try {
//            Date mDate = sdf.parse(givenDateString);
//            timeInMilliseconds = mDate.getTime();
//            myCal.setTimeInMillis(timeInMilliseconds);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, myCal.getTimeInMillis(), pendingIntent);
//        Toast.makeText(getActivity(),"Alarm set for " + myCal.getTime().toLocaleString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class createScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("manager_nomor", Index.user_nomor);
                Index.jsonObject.put("bex_nomor", nomorbex);
                Index.jsonObject.put("customer_nomor", nomorcustomer);
                Index.jsonObject.put("tipe", tipe);
                Index.jsonObject.put("tanggal", date);
                Index.jsonObject.put("jam", time);
                Index.jsonObject.put("reminder", reminder);
                Index.jsonObject.put("jenisjadwal", jenisjadwal);
                Index.jsonObject.put("keterangan", keterangan);
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
                        getActivity().getSharedPreferences("scheduletask", Context.MODE_PRIVATE).edit().clear().commit();

                        Toast.makeText(getContext(), "Schedule Task Assigned", Toast.LENGTH_LONG).show();

                        getActivity().getSharedPreferences("position", Context.MODE_PRIVATE).edit().clear().commit();

                        Fragment fragment = new ScheduleTaskList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        setAlarm();
                    }else{
                        Toast.makeText(getContext(), "Assign Schedule Task Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Assign Schedule Task Failed", Toast.LENGTH_LONG).show();
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