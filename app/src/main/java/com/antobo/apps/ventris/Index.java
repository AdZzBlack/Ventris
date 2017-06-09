package com.antobo.apps.ventris;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, android.location.LocationListener {

    public static PendingIntent pendingIntent;
    public static AlarmManager alarmmanager;

    public static TextView approve;

    public static IntentDialogManager intentalert = new IntentDialogManager();
    public static YesNoDialogManager yesnoalert = new YesNoDialogManager();
    public static NotificationDialogManager notifalert = new NotificationDialogManager();
    public static ContactDialogManager contactalert = new ContactDialogManager();

    public static AlphaAnimation buttoneffect = new AlphaAnimation(1F, 0.8F);
    public static AlphaAnimation listeffect = new AlphaAnimation(1F, 0.5F);
    public static ProgressDialog pDialog;

    public static SharedPreferences sharedpreferences, settingsharedpreferences, scheduletasksharedpreferences, salesordersharedpreferences, salestargetsharedpreferences, stocksharedpreferences, omzetsharedpreferences, targetsharedpreferences, positionsharedpreferences, customersharedpreferences, serversharedpreferences;
    public static GlobalFunction globalfunction;
    public static JSONObject jsonObject;

    public static String user_nomor, user_nama, user_jabatan, user_sales, cabang_nomor, cabang_nama, interval, radius, tracking, latitude, longitude, jam_awal, jam_akhir;

    private LocationManager managerGPS;
    private boolean isGPSEnabled = false;

    public static Integer width, height;

    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        globalfunction = new GlobalFunction(this);

        /*Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/lato-regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/lato-bold.ttf"))
                .addItalic(Typekit.createFromAsset(this, "fonts/lato-italic.ttf"))
                .addBoldItalic(Typekit.createFromAsset(this, "fonts/lato-bolditalic.ttf"));*/

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        approve = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_approvecustomer));

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initializeCountDrawer();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        // Start Registering GCM
        Intent intent = new Intent(this, GCMInstanceIDRegistrationService.class);
        startService(intent);

        //-----------------------------------------------------------------------------------------------

            // Set SharedPreferences
            scheduletasksharedpreferences = getSharedPreferences("scheduletask", Context.MODE_PRIVATE);
            salesordersharedpreferences = getSharedPreferences("salesorder", Context.MODE_PRIVATE);
            salestargetsharedpreferences = getSharedPreferences("salestarget", Context.MODE_PRIVATE);
            stocksharedpreferences = getSharedPreferences("stock", Context.MODE_PRIVATE);
            customersharedpreferences = getSharedPreferences("customer", Context.MODE_PRIVATE);
            omzetsharedpreferences = getSharedPreferences("omzet", Context.MODE_PRIVATE);
            targetsharedpreferences = getSharedPreferences("target", Context.MODE_PRIVATE);
            positionsharedpreferences = getSharedPreferences("position", Context.MODE_PRIVATE);

            // Set & Get Login SharedPreferences
            sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            user_nomor      = sharedpreferences.getString("user_nomor", "");
            user_nama       = sharedpreferences.getString("user_nama", "");
            user_sales       = sharedpreferences.getString("user_sales", "");
            user_jabatan    = sharedpreferences.getString("user_jabatan", "");
            cabang_nomor    = sharedpreferences.getString("cabang_nomor", "");
            cabang_nama    = sharedpreferences.getString("cabang_nama", "");

            if (user_jabatan.equals("")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
            } else {
//                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                Menu navmenu = navigationView.getMenu();

                if (user_jabatan.equals("SALES") || user_jabatan.equals("SALES ADMIN")) {
                    navmenu.findItem(R.id.nav_scheduletask).setVisible(false);
                    navmenu.findItem(R.id.nav_salestracking).setVisible(false);
//                    navmenu.findItem(R.id.nav_report).setVisible(false);
                }

                if (user_jabatan.equals("IT")) {
                    navmenu.findItem(R.id.nav_scheduletask).setVisible(false);
                    navmenu.findItem(R.id.nav_salestracking).setVisible(false);
                    navmenu.findItem(R.id.nav_report).setVisible(false);
                    navmenu.findItem(R.id.nav_omzet).setVisible(false);
                    navmenu.findItem(R.id.nav_addcustomer).setVisible(false);
                    navmenu.findItem(R.id.nav_target).setVisible(false);
                }

                if (!user_jabatan.equals("SALES ADMIN") && !user_jabatan.equals("OWNER") && !user_jabatan.equals("CONTROLLER")) {
                    navmenu.findItem(R.id.nav_approvecustomer).setVisible(false);
                }

                Fragment fragment = new Dashboard();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        //-----------------------------------------------------------------------------------------------

        // Open Specific Notification
        String menuFragment = this.getIntent().getStringExtra("fragment");
        String nomor = this.getIntent().getStringExtra("nomor");
        String nama = this.getIntent().getStringExtra("nama");
        if(menuFragment != null){
            if(menuFragment.equals("PrivateMessage")){
                this.globalfunction.private_user_nomor_tujuan = nomor;
                this.globalfunction.private_user_nama_tujuan = nama;

                Fragment fragment = new PrivateMessage();
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(menuFragment.equals("GroupMessage")){
                this.globalfunction.group_nomor = nomor;
                this.globalfunction.group_nama = nama;

                Fragment fragment = new GroupMessage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(menuFragment.equals("ScheduleTaskSalesList")){
                ScheduleTaskReview.scheduletask_nomor = nomor;

                Fragment fragment = new ScheduleTaskReview();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(menuFragment.equals("NewCustomerList")){
                Fragment fragment = new NewCustomerList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(menuFragment.equals("SalesOrderList")){
                Fragment fragment = new SalesOrderList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(menuFragment.equals("Dashboard")){
                Fragment fragment = new Dashboard();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

        // Set & Get Setting SharedPreferences
        settingsharedpreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        interval    = settingsharedpreferences.getString("interval", "0");
        radius    = settingsharedpreferences.getString("radius", "0");
        tracking    = settingsharedpreferences.getString("tracking", "GPS Only");
        latitude    = settingsharedpreferences.getString("latitude", "0");
        longitude    = settingsharedpreferences.getString("longitude", "0");
        jam_awal    = settingsharedpreferences.getString("jam_awal", "0");
        jam_akhir    = settingsharedpreferences.getString("jam_akhir", "0");

        // Start Alarm BackgroundServiceReceiver
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            }
        }

        if(!user_jabatan.equals("OWNER")) {
            boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                    new Intent("com.antobo.apps.ventris.LocationService"),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (!alarmUp) {
                Intent alarmIntent = new Intent(this, BackgroundServiceReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
                alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Integer.parseInt(interval), pendingIntent);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        this.globalfunction.private_run_check = 100;
        this.globalfunction.private_user_nama_tujuan = null;
        this.globalfunction.private_user_nomor_tujuan = null;

        this.globalfunction.group_run_check = 100;
        this.globalfunction.group_nama = null;
        this.globalfunction.group_nomor = null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fm.getBackStackEntryCount() > 1) {
                fm.popBackStack();
            }
            else {
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_settings, menu);
        if(!user_jabatan.equals("OWNER") && !user_jabatan.equals("CONTROLLER")){
            MenuItem navsetting = menu.findItem(R.id.action_setting);
            navsetting.setVisible(false);
        }
        if(!user_jabatan.equals("OWNER") && !user_jabatan.equals("MANAGER") && !user_jabatan.equals("CONTROLLER")){
            MenuItem navsetting = menu.findItem(R.id.action_target);
            navsetting.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Index.positionsharedpreferences.edit().clear();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            Fragment fragment = new Setting();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.action_target) {
            Fragment fragment = new ChoosePeriode();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.action_password) {
            Fragment fragment = new Password();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if (id == R.id.action_server) {
            SharedPreferences.Editor editor = Index.serversharedpreferences.edit();
            editor.putString( "servernow", "");
            editor.commit();

            Intent i = new Intent(Index.this, SelectServer.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }
        else if (id == R.id.action_logout) {
            String actionUrl = "Gcm/updateGCM/";
            new logoutUser().execute( actionUrl );

            getSharedPreferences("login", Context.MODE_PRIVATE).edit().clear().commit();
            getSharedPreferences("setting", Context.MODE_PRIVATE).edit().clear().commit();

            boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                    new Intent("com.antobo.apps.ventris.LocationService"),
                    PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmUp) {
                if(alarmmanager != null && pendingIntent != null){
                    alarmmanager.cancel(pendingIntent);
                }
                if(BackgroundServiceReceiver.alarmmanager != null && BackgroundServiceReceiver.pendingIntent != null){
                    BackgroundServiceReceiver.alarmmanager.cancel(BackgroundServiceReceiver.pendingIntent);
                }
                Intent pushIntent = new Intent(this, LocationService.class);
                stopService(pushIntent);
            }

            Intent i = new Intent(Index.this, Login.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Index.positionsharedpreferences.edit().clear();
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Fragment fragment = new Dashboard();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_contact) {
            Fragment fragment = new ContactUser();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_groupmessage) {
            Fragment fragment = new GroupList();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_privatemessage) {
            Fragment fragment = new PrivateMessageUser();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_salesorder) {
            Fragment fragment = new SalesOrderList();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_stockreport) {
            Fragment fragment = new StockReportFilter();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_scheduletask) {
            if (user_jabatan == "SALES") {
                Fragment fragment = new ScheduleTaskSalesList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else{
                Fragment fragment = new ScheduleTaskList();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (id == R.id.nav_pricelist) {
            Fragment fragment = new PriceListFilter();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_addcustomer) {
            Fragment fragment = new AddCustomer();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_approvecustomer) {
            Fragment fragment = new NewCustomerList();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_nfctools) {
            Fragment fragment = new NFCTools();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_omzet) {
            if (user_jabatan == "SALES") {
                Fragment fragment = new OmzetReportSalesFilter();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else{
                Fragment fragment = new OmzetReportFilter();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (id == R.id.nav_target) {
//            if (user_jabatan == "SALES") {
//                Fragment fragment = new TargetSalesFilter();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//            else{
//                Fragment fragment = new TargetFilter();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
        } else if (id == R.id.nav_salestracking) {
            Fragment fragment = new SalesTrackingUser();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_report) {
            if (user_jabatan.equals("SALES")) {
                Fragment fragment = new ReportSales();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else
            {
                Fragment fragment = new Report();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume(){
        initializeCountDrawer();

        managerGPS = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = managerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isGPSEnabled) {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Index.intentalert.showDialog(this, "Location Service", "Enable GPS?", R.drawable.salestracking_dark, i);
        }
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
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
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private class logoutUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("user_nomor", user_nomor);
                jsonObject.put("gcmid", "");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
    }

    public void initializeCountDrawer(){
        //Gravity property aligns the text

        String actionUrl = "Customer/getCounterDataNewCustomer/";
        new getCounterNewCustomer().execute( actionUrl );

        approve.setGravity(Gravity.CENTER_VERTICAL);
        approve.setTypeface(null, Typeface.BOLD);
        approve.setTextColor(getResources().getColor(R.color.colorDanger));
        approve.setText("0");
    }

    private class getCounterNewCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)
        {
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
                            String counter = obj.getString("customer_baru");

                            approve.setGravity(Gravity.CENTER_VERTICAL);
                            approve.setTypeface(null, Typeface.BOLD);
                            approve.setTextColor(getResources().getColor(R.color.colorDanger));
                            approve.setText(counter);
                        }
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
