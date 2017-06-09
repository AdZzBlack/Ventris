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

public class StockReport extends Fragment implements View.OnClickListener{

    private StockReportListAdapter stockreportadapter;

    private ListView lv_report;
    private Button btn_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stockreport, container, false);
        getActivity().setTitle("Stock Report");

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_close = (Button) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        stockreportadapter = new StockReportListAdapter(getActivity(), R.layout.list_stockreport, new ArrayList<StockReportAdapter>());
        lv_report = (ListView) v.findViewById(R.id.lv_report);
        lv_report.setAdapter(stockreportadapter);
        //-----END DECLARE---------------------------------------------------------------------------------------

        String actionUrl = "Stockreport/getStockKomoditi/";
        new getStockKomoditi().execute( actionUrl );

        lv_report.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_kodebarang = (TextView) v.findViewById(R.id.tv_kodebarang);
                TextView tv_namabarang = (TextView) v.findViewById(R.id.tv_namabeli);

                FragmentManager fragmentmanager = getFragmentManager();
                ImageShow dialogFragment = new ImageShow ();
                dialogFragment.show(fragmentmanager, "Image");
                dialogFragment.imageUrl = tv_kodebarang.getText().toString();
                dialogFragment.caption = tv_namabarang.getText().toString();
            }
        });

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_close){
            getActivity().getSharedPreferences("stock", Context.MODE_PRIVATE).edit().clear().commit();

            Fragment fragment = new StockReportFilter();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
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

    private class getStockKomoditi extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("nama", "%" + Index.stocksharedpreferences.getString("namabarangnamabeli", "") + "%");
                Index.jsonObject.put("brand", Index.stocksharedpreferences.getString("namabrand", ""));
                Index.jsonObject.put("tipe", Index.stocksharedpreferences.getString("namatipe", ""));
                Index.jsonObject.put("group", Index.stocksharedpreferences.getString("namagroupbarang", ""));
                Index.jsonObject.put("gudang", Index.stocksharedpreferences.getString("namagudang", ""));
                Index.jsonObject.put("tanggal_akhir", Index.stocksharedpreferences.getString("enddate", ""));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resss", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String kodebarang = (obj.getString("KodeBarang"));
                            String namabarang = (obj.getString("NamaJual"));
                            String gudang = (obj.getString("Gudang"));
                            String stock = (obj.getString("Fisik"));
                            String pending = (obj.getString("Pending"));
                            String satuan = (obj.getString("Satuan"));
                            String info1 = (obj.getString("Konversi2"));
                            String info2 = (obj.getString("Konversi3"));
                            String shade = (obj.getString("Shade"));

                            stockreportadapter.add(new StockReportAdapter(kodebarang, namabarang, gudang, stock, pending, satuan, info1, info2, shade));
                            stockreportadapter.notifyDataSetChanged();
                        }
                    }
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