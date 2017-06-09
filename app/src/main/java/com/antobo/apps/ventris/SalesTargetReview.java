package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

public class SalesTargetReview extends Fragment implements View.OnClickListener {

    private TextView tv_nomor, tv_periode, tv_nama;
    private EditText et_target;
    private Button btn_edit;

    private String nomor, periode, nama, target;

    public static String salestarget_nomor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salestarget_review, container, false);
        getActivity().setTitle("Sales Target Review");

        tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
        tv_periode = (TextView) v.findViewById(R.id.tv_periode);
        tv_nama = (TextView) v.findViewById(R.id.tv_namabex);
        et_target = (EditText) v.findViewById(R.id.et_target);

        btn_edit = (Button) v.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);

        Log.d("tes1", salestarget_nomor);
        String actionUrl = "Target/getDataTarget/";
        new getDetailDataSalesTarget().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_edit){
            target = et_target.getText().toString().replace(",","").replace(".","");
            String actionUrl = "Target/updateTarget/";
            new updateSalesTarget().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getDetailDataSalesTarget extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("target_nomor", salestarget_nomor);
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
                            String bulan = (obj.getString("intPeriode"));
                            String tahun = (obj.getString("intTahun"));
                            nama = (obj.getString("vcNama"));
                            target = (obj.getString("decTarget"));

                            if(bulan.equals("1")) periode = "Januari ";
                            else if(bulan.equals("2")) periode = "Februari ";
                            else if(bulan.equals("3")) periode = "Maret ";
                            else if(bulan.equals("4")) periode = "April ";
                            else if(bulan.equals("5")) periode = "Mei ";
                            else if(bulan.equals("6")) periode = "Juni ";
                            else if(bulan.equals("7")) periode = "Juli ";
                            else if(bulan.equals("8")) periode = "Agustus ";
                            else if(bulan.equals("9")) periode = "September ";
                            else if(bulan.equals("10")) periode = "Oktober ";
                            else if(bulan.equals("11")) periode = "November ";
                            else if(bulan.equals("12")) periode = "Desember ";
                            periode += tahun;

                            tv_periode.setText(periode);
                            tv_nama.setText(nama);
                            et_target.setText(GlobalFunction.delimeter(target));
                            tv_nomor.setText(salestarget_nomor);
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                Log.d("tes", result);
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

    private class updateSalesTarget extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("target_nomor", salestarget_nomor);
                Index.jsonObject.put("decTarget", target);
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
                        Toast.makeText(getContext(), "Sales Target Updated", Toast.LENGTH_LONG).show();

                        Fragment fragment = new SalesTargetList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(), "Update Sales Target Failed", Toast.LENGTH_LONG).show();
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