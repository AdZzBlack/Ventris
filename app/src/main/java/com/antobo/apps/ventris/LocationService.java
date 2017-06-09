package com.antobo.apps.ventris;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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
import java.util.Date;

/**
 * Created by antonnw on 01/07/2016.
 */
public class LocationService extends Service implements android.location.LocationListener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    IBinder mBinder;

    private SharedPreferences sharedpreferences, settingsharedpreferences, serversharedpreferences;

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    private LocationManager manager;
    private LocationManager managerGPS;
    private Location myLocation;
    private Location myLocationGPS;

    private double getLatitude;
    private double getLongitude;

    private boolean isMock;
    private boolean isNewLocation;
    private boolean canSave;

    private String trackingMode, jam_awal, jam_akhir;

    private GoogleApiClient mGoogleApiClient;
    private Location mFusedLocation;
    private LocationRequest mLocationRequest = new LocationRequest();

    private String hostUrl = "http://36.85.88.55/Dropbox/webdiv/webserviceBEX/ventris/index.php/api/";
    public static JSONObject jsonObject;

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        settingsharedpreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);

        serversharedpreferences = getSharedPreferences("server", Context.MODE_PRIVATE);
        hostUrl                 = serversharedpreferences.getString("servernow", "");
        hostUrl = "http://" + hostUrl + "/webserviceBEX/ventris/index.php/api/";

        trackingMode = settingsharedpreferences.getString("tracking", "");
        jam_awal = settingsharedpreferences.getString("jam_awal", "0");
        jam_akhir = settingsharedpreferences.getString("jam_akhir", "0");

        isGPSEnabled = false;
        isNetworkEnabled = false;
        isMock = false;
        isNewLocation = true;

        // Check Working Hours
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            Date dateawal = sdf.parse(jam_awal);
            Date dateakhir = sdf.parse(jam_akhir);

            String date = sdf.format(new Date());
            Date datesekarang = sdf.parse(date);

            Log.d("latlon", "cekcek");
            if (datesekarang.after(dateawal) && datesekarang.before(dateakhir)) {
                canSave = true;
                getLocation();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void getLocation() {
        Criteria criteriagps = new Criteria();
        Criteria criterianetwork = new Criteria();
        criteriagps.setAccuracy(Criteria.ACCURACY_FINE);
        criterianetwork.setAccuracy(Criteria.ACCURACY_COARSE);

        if (trackingMode.equals("GPS Only")) {
            managerGPS = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            managerGPS.getBestProvider(criteriagps, true);

            isGPSEnabled = managerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                if (managerGPS != null || manager != null) {
                    myLocationGPS = managerGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (myLocationGPS != null) {
                        onLocationChanged(myLocationGPS);
                        getLatitude = myLocationGPS.getLatitude();
                        getLongitude = myLocationGPS.getLongitude();
                    }
                }
            }
        } else {
            managerGPS = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            managerGPS.getBestProvider(criteriagps, true);

            manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            manager.getBestProvider(criterianetwork, true);

            isGPSEnabled = managerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled || isNetworkEnabled) {
                managerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                if (managerGPS != null || manager != null) {
                    myLocationGPS = managerGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    myLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (myLocationGPS != null) {
                        onLocationChanged(myLocationGPS);
                        getLatitude = myLocationGPS.getLatitude();
                        getLongitude = myLocationGPS.getLongitude();
                    } else if (myLocation != null) {
                        onLocationChanged(myLocation);
                        getLatitude = myLocation.getLatitude();
                        getLongitude = myLocation.getLongitude();
                    }
                }
            }
//                }
//        }

            if (isMock) {
                String actionUrl = "Fakegps/createFakegpsReport/";
                new createFakegpsReport().execute(actionUrl);
            }
            if (!isMock && isNewLocation && canSave) {
                String actionUrl = "Salestracking/sendLocationBEX/";
                new updateLocation().execute(actionUrl);
            }
        }
        Log.d("latlonpasti", getLatitude + ", " + getLongitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            isMock = Settings.Secure.getString(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        }

        if(isMock){
            Toast.makeText(this, "GPS Spoofing Detected", Toast.LENGTH_LONG).show();
        }
        else{
            myLocationGPS = location;
            myLocation = location;

            isMock = false;
            isNewLocation = true;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnected(Bundle bundle) {
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int res) {
//        mGoogleApiClient.connect();
//        Toast.makeText(this, "Google Play Services connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        mGoogleApiClient.connect();
//        Toast.makeText(this, "Google Play Services connection failed", Toast.LENGTH_SHORT).show();
    }

    private class updateLocation extends AsyncTask<String, Void, String> {
        String user_nomor = sharedpreferences.getString("user_nomor", "").toString();
        String latitude = String.valueOf(getLatitude);
        String longitude = String.valueOf(getLongitude);

        @Override
        protected String doInBackground(String... urls) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("user_nomor", user_nomor);
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return executePost(urls[0], jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {
            canSave = false;
        }
    }

    private class createFakegpsReport extends AsyncTask<String, Void, String> {
        String user_nomor = sharedpreferences.getString("user_nomor", "").toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("user_nomor", user_nomor);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return executePost(urls[0], jsonObject);
        }
    }

    // Execute POST JSON and Retrieve Data JSON
    public String executePost(String targetURL, JSONObject jsonObject){
        Log.d("executelocation", "a" + hostUrl + targetURL);
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