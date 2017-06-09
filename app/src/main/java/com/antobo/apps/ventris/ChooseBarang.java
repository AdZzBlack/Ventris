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

import java.util.ArrayList;

public class ChooseBarang extends Fragment implements View.OnClickListener {

    private MoreItemListAdapter moreitemadapter;

    private EditText et_choosebarang;
    private ImageButton ib_search;
    private ListView lv_barang;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choosebarang, container, false);
        getActivity().setTitle("Choose Item");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_choosebarang = (EditText) v.findViewById(R.id.et_choosebarang);
        moreitemadapter = new MoreItemListAdapter(getActivity(), R.layout.list_more_item, new ArrayList<MoreItemAdapter>());
        lv_barang = (ListView) v.findViewById(R.id.lv_barang);
        lv_barang.setAdapter(moreitemadapter);
        ib_search = (ImageButton) v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        lv_barang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                TextView tv_kode = (TextView) v.findViewById(R.id.tv_data);

                if(Index.positionsharedpreferences.getString("position", "").equals("Stock Report")){
                    SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
                    editor.putString( "nomorbarang", tv_nomor.getText().toString());
                    editor.putString( "namabarang", tv_nama.getText().toString());
                    editor.putString( "kodebarang", tv_kode.getText().toString());
                    editor.commit();

                    Fragment fragment = new StockReportFilter();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
                else if(Index.positionsharedpreferences.getString("position", "").equals("Sales Order")){
                    SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                    editor.putString( "nomorbarang", tv_nomor.getText().toString());
                    editor.putString( "namabarang", tv_nama.getText().toString());
                    editor.putString( "kodebarang", tv_kode.getText().toString());
                    editor.commit();

                    Fragment fragment = new SalesOrderAddGridBarang();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
            }
        });
        lv_barang.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                        String actionUrl = "Master/alldatabarang/";
                        new searchBarang().execute( actionUrl );
                    }
                }
            }

        });
        //-----END DECLARE---------------------------------------------------------------------------------------

        scroll = false;
        String actionUrl = "Master/alldatabarang/";
        new searchBarang().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_search){
            moreitemadapter.clear();
            counter = 0;

            String actionUrl = "Master/alldatabarang/";
            new searchBarang().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class searchBarang extends AsyncTask<String, Void, String> {
        String search = et_choosebarang.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
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
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = jsonarray.length() - 1; i >= 0; i--) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String nomor = (obj.getString("intNomor"));
                            String nama = (obj.getString("nama_jual"));
                            String kode = (obj.getString("barang_kode"));

                            moreitemadapter.add(new MoreItemAdapter(nomor, nama, kode));
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
                Toast.makeText(getContext(), "Load Item Failed", Toast.LENGTH_LONG).show();
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