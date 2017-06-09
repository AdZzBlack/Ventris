package com.antobo.apps.ventris;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
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

import java.nio.charset.Charset;
import java.util.Arrays;

public class ScheduleTaskSalesNFC extends Fragment implements View.OnClickListener {

    private TextView tv_verify, tv_namamanager, tv_kodecustomer, tv_namacustomer, tv_keterangan;
    private Button btn_readtag, btn_checkin;

    private NfcManager manager;
    private NfcAdapter adapter;

    private static final int PENDING_INTENT_TECH_DISCOVERED = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scheduletask_sales_nfc, container, false);
        getActivity().setTitle("Schedule Task NFC");

        tv_verify = (TextView) v.findViewById(R.id.tv_verify);
        tv_namamanager = (TextView) v.findViewById(R.id.tv_namamanager);
        tv_kodecustomer = (TextView) v.findViewById(R.id.tv_kodecustomer);
        tv_namacustomer = (TextView) v.findViewById(R.id.tv_namacustomer);
        tv_keterangan = (TextView) v.findViewById(R.id.tv_keterangan);

        btn_readtag = (Button) v.findViewById(R.id.btn_readtag);
        btn_checkin = (Button) v.findViewById(R.id.btn_checkin);
        btn_readtag.setOnClickListener(this);
        btn_checkin.setOnClickListener(this);

        String actionUrl = "Scheduletask/getDetailDataScheduleTask/";
        new getDetailDataScheduleTask().execute( actionUrl );

        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
            Toast.makeText(getActivity(), "Your device does not support NFC", Toast.LENGTH_SHORT).show();

            Fragment fragment = new Dashboard();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else{
            manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
            adapter = manager.getDefaultAdapter();
            if (adapter != null && adapter.isEnabled()) {}
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Information");
                builder.setMessage("Aktifkan NFC?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Fragment fragment = new Dashboard();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                builder.create().show();
            }
        }

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_readtag){
            String actionUrl = "Customer/getDataCustomer/";
            new getDataCustomer().execute( actionUrl );
        }
        else if(v.getId() == R.id.btn_checkin){
            if(tv_verify.getText().toString().equals("Verified")){
                String actionUrl = "Scheduletask/finishScheduleTask/";
                new finishScheduleTask().execute( actionUrl );
            }
            else{
                Toast.makeText(getActivity(), "Verify Customer Data First", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        PendingIntent pi = getActivity().createPendingResult(PENDING_INTENT_TECH_DISCOVERED, new Intent(), 0);

        adapter.enableForegroundDispatch(
                getActivity(), pi, new IntentFilter[]{ new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) },
                new String[][]{ new String[]{ "android.nfc.tech.NdefFormatable" },
                        new String[]{ "android.nfc.tech.Ndef" }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        adapter.disableForegroundDispatch(getActivity());
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PENDING_INTENT_TECH_DISCOVERED :
                resolveIntent(data, true);
                break;
        }
    }

    private void resolveIntent(Intent data, boolean foregroundDispatch) {
        String action = data.getAction();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Tag tag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (foregroundDispatch) {
                Parcelable[] ndefRaw = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage[] ndefMsgs = null;

                if (ndefRaw != null) {
                    ndefMsgs = new NdefMessage[ndefRaw.length];
                }

                for (int i = 0; i < ndefMsgs.length; ++i) {
                    ndefMsgs[i] = (NdefMessage) ndefRaw[i];
                }

                if (ndefMsgs != null) {
                    for (int i = 0; i < ndefMsgs.length; ++i) {
                        NdefRecord[] records = ndefMsgs[i].getRecords();
                        if (records != null) {
                            for (int j = 0; j < records.length; ++j) {
                                if ((records[j].getTnf() == NdefRecord.TNF_WELL_KNOWN) && Arrays.equals(records[j].getType(), NdefRecord.RTD_TEXT)) {
                                    byte[] payload = records[j].getPayload();
                                    String kodecustomer = new String(Arrays.copyOfRange(payload, 1, payload.length), Charset.forName("UTF-8"));
                                    tv_kodecustomer.setText(kodecustomer);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class getDataCustomer extends AsyncTask<String, Void, String> {
        String customer_kode = tv_kodecustomer.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("customer_kode", customer_kode);
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
                            String customer_nama = (obj.getString("customer_nama"));

                            tv_verify.setText("Verified");
                            tv_verify.setTextColor(getResources().getColor(R.color.colorSuccess));
                            tv_namacustomer.setText(customer_nama);
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Index.pDialog.dismiss();
                Toast.makeText(getContext(), "Customer Data Load Failed", Toast.LENGTH_LONG).show();
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

    private class getDetailDataScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("jadwal_nomor", ScheduleTaskSalesList.scheduletask_nomor);
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
                            String manager_nama = (obj.getString("manager_nama"));
                            String customer_nama = (obj.getString("customer_nama"));
                            String keterangan = (obj.getString("keterangan"));

                            tv_namamanager.setText(manager_nama);
                            tv_namacustomer.setText(customer_nama);
                            tv_keterangan.setText(keterangan);
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Schedule Task Load Failed", Toast.LENGTH_LONG).show();
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

    private class finishScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("jadwal_nomor", ScheduleTaskSalesList.scheduletask_nomor);
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
                        Toast.makeText(getContext(), "Schedule Task Completed", Toast.LENGTH_LONG).show();

                        Fragment fragment = new ScheduleTaskSalesList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Finish Schedule Task Failed", Toast.LENGTH_LONG).show();
                    }

                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Schedule Task Load Failed", Toast.LENGTH_LONG).show();
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