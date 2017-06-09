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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChooseSatuanHarga extends Fragment implements View.OnClickListener {

    private MoreItemListAdapter moreitemadapter;

    private EditText et_choosesatuanharga;
    private ImageButton ib_search;
    private ListView lv_satuanharga;

    private String nomorbarang, nomorvaluta;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choosesatuanharga, container, false);
        getActivity().setTitle("Choose Unit Price");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_choosesatuanharga = (EditText) v.findViewById(R.id.et_choosesatuanharga);
        moreitemadapter = new MoreItemListAdapter(getActivity(), R.layout.list_more_item, new ArrayList<MoreItemAdapter>());
        lv_satuanharga = (ListView) v.findViewById(R.id.lv_satuanharga);
        lv_satuanharga.setAdapter(moreitemadapter);
        ib_search = (ImageButton) v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        lv_satuanharga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                TextView tv_harga = (TextView) v.findViewById(R.id.tv_data);

                if(Index.positionsharedpreferences.getString("position", "").equals("Sales Order")){
                    SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                    editor.putString( "nomorsatuanharga", tv_nomor.getText().toString());
                    editor.putString( "namasatuanharga", tv_nama.getText().toString());
                    editor.putString( "hargasatuanharga", tv_harga.getText().toString());
                    editor.commit();

                    Fragment fragment = new SalesOrderAddGridBarang();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
            }
        });
        lv_satuanharga.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                        if (nomorbarang.equals("")) {
                            Toast.makeText(getContext(), "Choose Item", Toast.LENGTH_LONG).show();
                        }else {
                            String actionUrl = "Master/alldatasatuanharga/";
                            new searchSatuanHarga().execute(actionUrl);
                        }
                    }
                }
            }

        });
        //-----END DECLARE---------------------------------------------------------------------------------------

        scroll = false;
        nomorbarang = Index.salesordersharedpreferences.getString("nomorbarang", "");
        nomorvaluta = Index.salesordersharedpreferences.getString("nomorvaluta", "");
        if (nomorbarang.equals("")) {
            Toast.makeText(getContext(), "Choose Item", Toast.LENGTH_LONG).show();
        }
        else if (nomorvaluta.equals("")) {
            Toast.makeText(getContext(), "Choose Currency", Toast.LENGTH_LONG).show();
        }
        else {
            String actionUrl = "Master/alldatasatuanharga/";
            new searchSatuanHarga().execute(actionUrl);
        }

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_search){
            moreitemadapter.clear();
            counter = 0;

            if (nomorbarang.equals("")) {
                Toast.makeText(getContext(), "Choose Item", Toast.LENGTH_LONG).show();
            }else{
                String actionUrl = "Master/alldatasatuanharga/";
                new searchSatuanHarga().execute( actionUrl );
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

    private class searchSatuanHarga extends AsyncTask<String, Void, String> {
        String search = et_choosesatuanharga.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("barang_nomor", nomorbarang);
                Index.jsonObject.put("kurs_nomor", nomorvaluta);
                Index.jsonObject.put("search", search);
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

                DecimalFormat format=new DecimalFormat("#,###.#");

                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = jsonarray.length() - 1; i >= 0; i--) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String nomor = (obj.getString("intNomorMSatuan1"));
                            String nama = (obj.getString("nama_satuan1"));
                            Double konversiRaw = Double.parseDouble((obj.getString("decHargaPL1")));
                            String konversi = String.valueOf(format.format(konversiRaw));
                            moreitemadapter.add(new MoreItemAdapter(nomor, nama, konversi));
                            moreitemadapter.notifyDataSetChanged();

                            nomor = (obj.getString("intNomorMSatuan2"));
                            nama = (obj.getString("nama_satuan2"));
                            konversiRaw = Double.parseDouble((obj.getString("decHargaPL2")));
                            konversi = String.valueOf(format.format(konversiRaw));
                            moreitemadapter.add(new MoreItemAdapter(nomor, nama, konversi));
                            moreitemadapter.notifyDataSetChanged();

                            nomor = (obj.getString("intNomorMSatuan3"));
                            nama = (obj.getString("nama_satuan3"));
                            konversiRaw = Double.parseDouble((obj.getString("decHargaPL3")));
                            konversi = String.valueOf(format.format(konversiRaw));
                            moreitemadapter.add(new MoreItemAdapter(nomor, nama, konversi));
                            moreitemadapter.notifyDataSetChanged();
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
                Toast.makeText(getContext(), "Unit Price Load Failed", Toast.LENGTH_LONG).show();
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