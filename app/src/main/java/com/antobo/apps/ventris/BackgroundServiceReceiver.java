package com.antobo.apps.ventris;

import android.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by antonnw on 23/06/2016.
 */
public class BackgroundServiceReceiver extends WakefulBroadcastReceiver {

    public static Context contextNew;
    public static PendingIntent pendingIntent;
    public static AlarmManager alarmmanager;
    private SharedPreferences sharedpreferences;
    private SharedPreferences settingsharedpreferences;
    private SharedPreferences serversharedpreferences;
    public static String interval, user_jabatan, jam_awal, jam_akhir, user_sales;
    private String hostUrl = "http://36.85.88.55/Dropbox/webdiv/webserviceBEX/ventris/index.php/api/";
    public static JSONObject jsonObject;

    @Override
    public void onReceive(Context context, Intent intent) {
        contextNew = context;

        sharedpreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        user_jabatan    = sharedpreferences.getString("user_jabatan", "SALES");

        serversharedpreferences = context.getSharedPreferences("server", Context.MODE_PRIVATE);
        hostUrl                 = serversharedpreferences.getString("servernow", "");
        hostUrl = "http://" + hostUrl + "/webserviceBEX/ventris/index.php/api/";

        settingsharedpreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        interval        = settingsharedpreferences.getString("interval", "0");
        jam_awal        = settingsharedpreferences.getString("jam_awal", "0");
        jam_akhir       = settingsharedpreferences.getString("jam_akhir", "0");
        user_sales      = sharedpreferences.getString("user_sales", "");

        // Start Alarm BackgroundServiceReceiver
        if(!user_jabatan.equals("OWNER")){
            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    new Intent("com.antobo.apps.ventris.LocationService"),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (!alarmUp) {
                if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
                    String actionUrl = "Scheduletask/createReminderScheduleTask/";
                    new createReminderScheduleTask().execute( actionUrl );

                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent pushIntent = new Intent(context, LocationService.class);
                        startWakefulService(context, pushIntent);
                    }

                    // Check Working Hours
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                        Date dateawal = sdf.parse(jam_awal);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateawal);
                        cal.add(Calendar.MINUTE, 5);
                        String newTime = sdf.format(cal.getTime());

                        Date dateakhir = sdf.parse(newTime);

                        String date = sdf.format(new Date());
                        Date datesekarang = sdf.parse(date);

                        if(datesekarang.after(dateawal) && datesekarang.before(dateakhir)){
                            String actionUrlOmzet = "Omzettarget/getOmzetSalesTotal/";
                            new createReminderOmzet().execute( actionUrlOmzet );
                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }
                else{
                    String actionUrl = "Scheduletask/createReminderScheduleTask/";
                    new createReminderScheduleTask().execute( actionUrl );

                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent pushIntent = new Intent(context, LocationService.class);
                        startWakefulService(context, pushIntent);
                    }

                    // Check Working Hours
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                        Date dateawal = sdf.parse(jam_awal);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateawal);
                        cal.add(Calendar.MINUTE, 5);
                        String newTime = sdf.format(cal.getTime());

                        Date dateakhir = sdf.parse(newTime);

                        String date = sdf.format(new Date());
                        Date datesekarang = sdf.parse(date);

                        if(datesekarang.after(dateawal) && datesekarang.before(dateakhir)){
                            String actionUrlOmzet = "Omzettarget/getOmzetSalesTotal/";
                            new createReminderOmzet().execute( actionUrlOmzet );
                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private class createReminderScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("bex_nomor", user_sales);
                jsonObject.put("reminder", "true");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return executePost(urls[0], jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resss", "a" + result + hostUrl);
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    if(!obj.has("query")){
                        Vibrator v = (Vibrator) contextNew.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
//                Toast.makeText(contextNew, "failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class createReminderOmzet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("bex_nomor", user_sales);
                jsonObject.put("reminder", "true");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return executePost(urls[0], jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    if(!obj.has("query")){
                        Vibrator v = (Vibrator) contextNew.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                    else
                    {
                        Toast.makeText(contextNew, "Failed receive omzet", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
//                Toast.makeText(contextNew, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Execute POST JSON and Retrieve Data JSON
    public String executePost(String targetURL, JSONObject jsonObject){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost( hostUrl + targetURL );

            // 3. convert JSONObject to JSON to String
            String json = jsonObject.toString();

            // 4. ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity stringEntity = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(stringEntity);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
