package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SalesOrderList extends Fragment {

    JSONObject jsonObject;

    private SalesOrderListAdapter salesorderadapter;
    private ListView lv_salesorder;

    Button btn_addsavebarang, btn_addcancelbarang;
    FloatingActionButton btn_addsales;
    EditText et_namabarang;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_list, container, false);
        getActivity().setTitle("Sales Order");

        //---------------------------------------------------------
            salesorderadapter = new SalesOrderListAdapter(getActivity(), R.layout.list_salesorder, new ArrayList<SalesOrderAdapter>());
            lv_salesorder = (ListView) v.findViewById(R.id.lv_salesorder);
            lv_salesorder.setAdapter(salesorderadapter);

            btn_add_onclick(v);

            lv_salesorder.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                            String actionUrl = "Orderjual/alldataorderjual/";
                            new getSalesOrder().execute( actionUrl );
                        }
                    }
                }

            });
            lv_salesorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    v.startAnimation(Index.listeffect);
                }
            });
        //---------------------------------------------------------

        scroll = false;
        String actionUrl = "Orderjual/alldataorderjual/";
        new getSalesOrder().execute( actionUrl );

        return v;
    }


    private void btn_add_onclick(View v){
        btn_addsales = (FloatingActionButton) v.findViewById(R.id.btn_addsalesorder);
        btn_addsales.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Fragment salesorderaddheader = new SalesOrderAddHeader();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, salesorderaddheader);
            transaction.addToBackStack(null);
            transaction.commit();
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getSalesOrder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                if (Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")) {
                    Index.jsonObject.put("bex_nomor", Index.user_nomor);
                }
                Index.jsonObject.put("cabang_nomor", Index.cabang_nomor);
                Index.jsonObject.put("limit", counter);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePostSales(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String nomor = (obj.getString("intNomor"));
                            String kode = (obj.getString("vcKode"));
                            String tanggal = (obj.getString("dtTanggal"));
                            String tanggalkirim = (obj.getString("dtTanggalKirim"));
                            String gudang = (obj.getString("gudang_nama"));
                            String customer = (obj.getString("customer_nama"));

                            salesorderadapter.add(new SalesOrderAdapter(nomor, kode, tanggal, tanggalkirim, gudang, customer));
                            salesorderadapter.notifyDataSetChanged();
                        }
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Log.d("tes", result);
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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