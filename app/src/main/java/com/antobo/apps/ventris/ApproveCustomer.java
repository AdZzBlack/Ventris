package com.antobo.apps.ventris;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApproveCustomer extends Fragment implements View.OnClickListener{

    private TextView tv_jenis, tv_area, tv_kota;
    private EditText et_nama, et_alamat, et_telp;
    private Button btn_check;

    private String nama, alamat, telp, jenis, area, kota;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_add, container, false);
        getActivity().setTitle("Add Customer");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_jenis = (TextView) v.findViewById(R.id.tv_jenis);
        tv_jenis.setOnClickListener(this);
        tv_area = (TextView) v.findViewById(R.id.tv_area);
        tv_area.setOnClickListener(this);
        tv_kota = (TextView) v.findViewById(R.id.tv_kota);
        tv_kota.setOnClickListener(this);
        et_nama = (EditText) v.findViewById(R.id.et_nama);
        et_alamat = (EditText) v.findViewById(R.id.et_alamat);
        et_telp = (EditText) v.findViewById(R.id.et_telepon);

        btn_check = (Button) v.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        nama = Index.customersharedpreferences.getString("namacustomer", "");
        alamat = Index.customersharedpreferences.getString("alamatcustomer", "");
        telp = Index.customersharedpreferences.getString("telpcustomer", "");
        jenis = Index.customersharedpreferences.getString("namajenis", "");
        area = Index.salesordersharedpreferences.getString("namaarea", "");
        kota = Index.customersharedpreferences.getString("namakota", "");

        tv_jenis.setText(jenis);
        tv_area.setText(area);
        tv_kota.setText(kota);
        et_nama.setText(nama);
        et_alamat.setText(alamat);
        et_telp.setText(telp);

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "customer");
        poseditor.commit();

        SharedPreferences.Editor editor = Index.customersharedpreferences.edit();
        editor.putString( "namacustomer", et_nama.getText().toString());
        editor.putString( "alamatcustomer", et_alamat.getText().toString());
        editor.putString( "telpcustomer", et_telp.getText().toString());
        editor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_check)
        {
            boolean check = true;

            if(Index.customersharedpreferences.getString("namacustomer", "").equals("") || Index.customersharedpreferences.getString("alamatcustomer", "").equals("") || Index.customersharedpreferences.getString("telpcustomer", "").equals("") || Index.customersharedpreferences.getString("namajenis", "").equals("") || Index.customersharedpreferences.getString("namakota", "").equals("") || Index.salesordersharedpreferences.getString("namaarea", "").equals(""))
            {
                check = false;
            }

            if(check)
            {
                String actionUrl = "Customer/createTempCustomer/";
                new add().execute( actionUrl );
            }
            else
            {
                Toast.makeText(getContext(), "Complete all field", Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == R.id.tv_jenis){
            Fragment fragment = new ChooseJenisCustomer();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_area){
            Fragment fragment = new ChooseArea();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_kota){
            Fragment fragment = new ChooseKota();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class add extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("nama", Index.customersharedpreferences.getString("namacustomer", ""));
                Index.jsonObject.put("alamat", Index.customersharedpreferences.getString("alamatcustomer", ""));
                Index.jsonObject.put("telepon", Index.customersharedpreferences.getString("telpcustomer", ""));
                Index.jsonObject.put("nomor_jeniscustomer", Index.customersharedpreferences.getString("nomorjenis", ""));
                Index.jsonObject.put("nomor_area", Index.salesordersharedpreferences.getString("nomorarea", ""));
                Index.jsonObject.put("nomor_kota", Index.customersharedpreferences.getString("nomorkota", ""));
                Index.jsonObject.put("nomor_sales", Index.user_nomor);
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
                        Toast.makeText(getContext(), "Add Customer Completed. Wait for approval...", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = Index.customersharedpreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor saleseditor = Index.salesordersharedpreferences.edit();
                        saleseditor.clear();
                        saleseditor.commit();

                        Fragment fragment = new ApproveCustomer();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Add Customer Failed", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Add Customer Failed", Toast.LENGTH_LONG).show();
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