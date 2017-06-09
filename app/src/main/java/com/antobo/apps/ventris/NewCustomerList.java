package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewCustomerList extends Fragment
{
    private ListView lv_newcustomer;
    private ItemListAdapter itemadapter;

    private ArrayList<Data> list = new ArrayList<Data>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newcustomer_list, container, false);
        getActivity().setTitle("New Customer");

        //---------------------------------------------------------

        initializeCountDrawer();
        lv_newcustomer = (ListView) v.findViewById(R.id.lv_area);

        itemadapter = new ItemListAdapter(getActivity(), R.layout.list_item, new ArrayList<ItemAdapter>());
        lv_newcustomer.setAdapter(itemadapter);

        String actionUrl = "Customer/getDataNewCustomer/";
        new getCustomer().execute( actionUrl );

        lv_newcustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);


                SharedPreferences.Editor editor = Index.customersharedpreferences.edit();
                editor.putString( "newnomorcustomer", list.get(position).getNomor());
                editor.putString( "newnamacustomer", list.get(position).getNama());
                editor.putString( "newkontakcustomer", list.get(position).getKontak());
                editor.putString( "newalamatcustomer", list.get(position).getAlamat());
                editor.putString( "newteleponcustomer", list.get(position).getTelepon());
                editor.putString( "newkotacustomer", list.get(position).getKota());
                editor.commit();

                Fragment fragment = new NewCustomerReview();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });

        //---------------------------------------------------------

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)
        {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("customer_tipe", "0");
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
                            String nomor = obj.getString("customer_nomor");
                            String nama = obj.getString("customer_nama");
                            String kontak = obj.getString("customer_kontak");
                            String alamat = obj.getString("customer_alamat");
                            String telepon = obj.getString("customer_telepon");
                            String kota = obj.getString("kota_nama");

                            Data d = new Data();
                            d.setNomor(nomor);
                            d.setNama(nama);
                            d.setKontak(kontak);
                            d.setAlamat(alamat);
                            d.setTelepon(telepon);
                            d.setKota(kota);
                            list.add(d);

                            itemadapter.add(new ItemAdapter(nomor, nama));
                            itemadapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "New Customer Load Failed", Toast.LENGTH_LONG).show();
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

    public void initializeCountDrawer(){
        //Gravity property aligns the text

        String actionUrl = "Customer/getCounterDataNewCustomer/";
        new getCounterNewCustomer().execute( actionUrl );

        Index.approve.setGravity(Gravity.CENTER_VERTICAL);
        Index.approve.setTypeface(null, Typeface.BOLD);
        Index.approve.setTextColor(getResources().getColor(R.color.colorDanger));
        Index.approve.setText("0");
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

                            Index.approve.setGravity(Gravity.CENTER_VERTICAL);
                            Index.approve.setTypeface(null, Typeface.BOLD);
                            Index.approve.setTextColor(getResources().getColor(R.color.colorDanger));
                            Index.approve.setText(counter);
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

    public class Data {
        private String nomor;
        private String nama;
        private String kontak;
        private String alamat;
        private String telepon;
        private String kota;

        public String getNomor() {
            return nomor;
        }
        public void setNomor(String nomor) {
            this.nomor = nomor;
        }
        public String getNama() {
            return nama;
        }
        public void setNama(String nama) {
            this.nama = nama;
        }
        public String getKontak() {
            return kontak;
        }
        public void setKontak(String kontak) {
            this.kontak = kontak;
        }
        public String getAlamat() {
            return alamat;
        }
        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }
        public String getTelepon() {
            return telepon;
        }
        public void setTelepon(String telepon) {
            this.telepon = telepon;
        }
        public String getKota() {
            return kota;
        }
        public void setKota(String kota) {
            this.kota = kota;
        }
    }
}