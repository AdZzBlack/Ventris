package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SalesTracking extends Fragment implements OnMapReadyCallback, android.location.LocationListener, View.OnClickListener {

    private PolylineOptions rectOptions = new PolylineOptions();
    private ArrayList<String> arr_marker = new ArrayList<String>();

    private static boolean isGPSEnabled;
    private static boolean isNetworkEnabled;

    private static LocationManager manager;
    private static LocationManager managerGPS;
    private static Location myLocation;
    private static Location myLocationGPS;

    private double getLatitude;
    private double getLongitude;

    private FloatingActionButton btn_viewlegend;
    private ListView lv_legend;
    private MoreItemListAdapter adapter;

    private Boolean toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salestracking, container, false);
        getActivity().setTitle("Sales Tracking");

        btn_viewlegend = (FloatingActionButton) v.findViewById(R.id.btn_viewlegend);
        btn_viewlegend.setOnClickListener(this);
        lv_legend = (ListView) v.findViewById(R.id.lv_legend);
        adapter = new MoreItemListAdapter(getActivity(), R.layout.list_more_item, new ArrayList<MoreItemAdapter>());
        lv_legend.setAdapter(adapter);
        toggle = false;

        if(SalesTrackingUser.tanggal.equals("[Date]")){

        } else{
            try{
                String actionUrl = "Salestracking/getLocationBEX/";
                new getLocationBEX().execute( actionUrl ).get();
            }
            catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_viewlegend){
            if(!toggle){
                lv_legend.setVisibility(View.VISIBLE);
                toggle = true;

            }
            else{
                lv_legend.setVisibility(View.INVISIBLE);
                toggle = false;
            }
        }
    }

    public void onMapReady(final GoogleMap map) {
        if(SalesTrackingUser.tanggal.equals("[Date]")){
            Criteria criteriagps = new Criteria();
            Criteria criterianetwork = new Criteria();
            criteriagps.setAccuracy(Criteria.ACCURACY_FINE);
            criterianetwork.setAccuracy(Criteria.ACCURACY_COARSE);

            managerGPS = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            managerGPS.getBestProvider(criteriagps, true);

            manager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            manager.getBestProvider(criterianetwork, true);

            isGPSEnabled = managerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isGPSEnabled || isNetworkEnabled) {
                managerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                if(managerGPS != null || manager != null) {
                    myLocationGPS = managerGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    myLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(myLocationGPS != null) {
                        onLocationChanged(myLocationGPS);
                    }
                    else if(myLocation != null) {
                        onLocationChanged(myLocation);
                    }
                }
            }

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(getLatitude, getLongitude))
                    .title("Your Location"));
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(SalesTrackingUser.latitude), Double.parseDouble(SalesTrackingUser.longitude)))
                    .title("Target"));
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }
                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(getContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());
                    TextView snippet = new TextView(getContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });

            GoogleDirection.withServerKey(getResources().getString(R.string.google_server_key))
                    .from(new LatLng(getLatitude, getLongitude))
                    .to(new LatLng(Double.parseDouble(SalesTrackingUser.latitude), Double.parseDouble(SalesTrackingUser.longitude)))
                    .transportMode(TransportMode.DRIVING)
                    .alternativeRoute(true)
                    .avoid(AvoidType.INDOOR)
                    .unit(Unit.METRIC)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if(direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);

                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), directionPositionList, 2, Color.RED);
                                map.addPolyline(polylineOptions);
                            } else {
                                // Do something
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something
                        }
                    });

            LatLng latlng = new LatLng(Double.parseDouble(SalesTrackingUser.latitude), Double.parseDouble(SalesTrackingUser.longitude));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 14);
            map.animateCamera(cameraUpdate);
        } else{
            for (int i = arr_marker.size() - 1; i >= 0; i--) {
                String temp[] = arr_marker.get(i).split("%");
                Double lat = Double.parseDouble(temp[0]);
                Double lng = Double.parseDouble(temp[1]);
                String tanggal = temp[2];
                LatLng latlng = new LatLng(lat, lng);

                // Use Polyline
                rectOptions.add(latlng);
                rectOptions.color(Color.RED);
                rectOptions.width(2);
                map.addPolyline(rectOptions);

                map.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(temp[2]));
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }
                    @Override
                    public View getInfoContents(Marker marker) {
                        LinearLayout info = new LinearLayout(getContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());
                        TextView snippet = new TextView(getContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 13);
                map.animateCamera(cameraUpdate);

                String lokasi = "";
                if(!lat.equals("0") || !lng.equals("0")){
                    lokasi = GeocoderAddress.getKnownLocation(getContext(), String.valueOf(lat), String.valueOf(lng));
                }
                else{
                    lokasi = "-";
                }

                adapter.add(new MoreItemAdapter("", tanggal, lokasi));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocationGPS = location;
        myLocation = location;

        getLatitude = location.getLatitude();
        getLongitude = location.getLongitude();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
            fragment.getMapAsync(this);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getLocationBEX extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor", SalesTrackingUser.user_nomor);
                Index.jsonObject.put("start_date", SalesTrackingUser.tanggal);
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
                            String latitude = (obj.getString("declatitude"));
                            String longitude = (obj.getString("declongitude"));
                            String time = (obj.getString("dtInsertDate"));

                            arr_marker.add(latitude + "%" + longitude + "%" + time);
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Location Load Failed", Toast.LENGTH_LONG).show();
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