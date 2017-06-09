package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewCustomerReview extends Fragment implements View.OnClickListener {

    private TextView tv_nama, tv_kontak, tv_alamat, tv_kota;
    private EditText et_alasan;
    private Button btn_approve, btn_decline;

    private String nomor, nama, kontak, alamat, kota, alasan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newcustomer_review, container, false);
        getActivity().setTitle("New Customer Review");

        tv_nama = (TextView) v.findViewById(R.id.tv_nama);
        tv_kontak = (TextView) v.findViewById(R.id.tv_kontak);
        tv_alamat = (TextView) v.findViewById(R.id.tv_alamat);
        tv_kota = (TextView) v.findViewById(R.id.tv_kota);

        et_alasan = (EditText) v.findViewById(R.id.et_alasan);

        btn_approve = (Button) v.findViewById(R.id.btn_approve);
        btn_approve.setOnClickListener(this);
        btn_decline = (Button) v.findViewById(R.id.btn_decline);
        btn_decline.setOnClickListener(this);

        nomor = Index.customersharedpreferences.getString("newnomorcustomer", "");
        nama = Index.customersharedpreferences.getString("newnamacustomer", "");
        alamat = Index.customersharedpreferences.getString("newalamatcustomer", "");
        kontak = Index.customersharedpreferences.getString("newkontakcustomer", "");
        kota = Index.customersharedpreferences.getString("newkotacustomer", "");

        tv_nama.setText(nama);
        tv_alamat.setText(alamat);
        tv_kontak.setText(kontak);
        tv_kota.setText(kota);

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_approve){
            String actionUrl = "Customer/approveNewCustomer/";
            new approveTask().execute( actionUrl );
        }
        else if(v.getId() == R.id.btn_decline){
            if(et_alasan.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Reason required for decline", Toast.LENGTH_LONG).show();
            }
            else
            {
                alasan = et_alasan.getText().toString();
                String actionUrl = "Customer/disapproveNewCustomer/";
                new disapproveTask().execute( actionUrl );
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

    private class approveTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("customer_nomor", nomor);
                Index.jsonObject.put("admin_nomor", Index.user_nomor);
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
                        Toast.makeText(getContext(), "New Customer Approved", Toast.LENGTH_LONG).show();

                        Fragment fragment = new NewCustomerList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Approve New Customer failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                Index.pDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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

    private class disapproveTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("customer_nomor", nomor);
                Index.jsonObject.put("admin_nomor", Index.user_nomor);
                Index.jsonObject.put("keterangan", alasan);
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
                        Toast.makeText(getContext(), "New Customer Decline", Toast.LENGTH_LONG).show();

                        Fragment fragment = new NewCustomerList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Decline New Customer failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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