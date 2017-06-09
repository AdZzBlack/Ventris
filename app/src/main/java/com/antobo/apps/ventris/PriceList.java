package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PriceList extends Fragment implements View.OnClickListener{

    private PriceListAdapter priceadapter;

    private ListView lv_report;
    private Button btn_close;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pricelist, container, false);
        getActivity().setTitle("Price List");

        counter =0;

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_close = (Button) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        priceadapter = new PriceListAdapter(getActivity(), R.layout.list_price, new ArrayList<PriceAdapter>());
        lv_report = (ListView) v.findViewById(R.id.lv_report);
        lv_report.setAdapter(priceadapter);
        //-----END DECLARE---------------------------------------------------------------------------------------

        lv_report.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Scroll Down
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if((lastInScreen == totalItemCount) && totalItemCount != 0){
                    if(scroll == true)
                    {
                        scroll = false;
                        counter += 1;

                        String actionUrl = "Pricelist/getPriceList/";
                        new getPriceList().execute( actionUrl );
                    }
                }
            }
        });

        scroll = false;
        String actionUrl = "Pricelist/getPriceList/";
        new getPriceList().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_close){
            getActivity().getSharedPreferences("pricelist", Context.MODE_PRIVATE).edit().clear().commit();

            Fragment fragment = new PriceListFilter();
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

    private class getPriceList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("filter", Index.stocksharedpreferences.getString("katakunci", ""));
                Index.jsonObject.put("limit", counter);
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
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        Log.d("price", obj.toString());
                        if(!obj.has("query")){
                            String kodebarang = (obj.getString("vcKode"));
                            String namabeli = (obj.getString("vcNamaBeli"));
                            String namajual = (obj.getString("vcNamaJual"));
                            String luas = (obj.getString("decLuas"));
                            String idr1 = (obj.getString("decHargaPL1"));
                            String idr2 = (obj.getString("decHargaPL2"));
                            String idr3 = (obj.getString("decHargaPL3"));
                            String usd1 = (obj.getString("decHargaPL1USD"));
                            String usd2 = (obj.getString("decHargaPL2USD"));
                            String usd3 = (obj.getString("decHargaPL3USD"));
                            String rmb1 = (obj.getString("decHargaPL1RMB"));
                            String rmb2 = (obj.getString("decHargaPL2RMB"));
                            String rmb3 = (obj.getString("decHargaPL3RMB"));
                            String satuan1 = (obj.getString("satuanPL1"));
                            String satuan2 = (obj.getString("satuanPL2"));
                            String satuan3 = (obj.getString("satuanPL3"));
                            String konversi1 = (obj.getString("Konversi_1"));
                            String konversi2 = (obj.getString("Konversi_2"));
                            String konversi3 = (obj.getString("Konversi_3"));


                            priceadapter.add(new PriceAdapter(kodebarang, namabeli, namajual, luas, idr1, idr2, idr3, usd1, usd2, usd3, rmb1, rmb2, rmb3, satuan1, satuan2, satuan3, konversi1, konversi2, konversi3));
                            priceadapter.notifyDataSetChanged();

                        }
                    }
                }

                if(jsonarray.length() < 10){
                    scroll = false;
                }
                else{
                    scroll = true;
                }

                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Report Failed", Toast.LENGTH_LONG).show();
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