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

public class ChooseColorShade extends Fragment implements View.OnClickListener {

    private ItemListAdapter itemadapter;

    private EditText et_choosecolorshade;
    private ImageButton ib_search;
    private ListView lv_colorshade;

    private String nomorbarang;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choosecolorshade, container, false);
        getActivity().setTitle("Choose Color Shade");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_choosecolorshade = (EditText) v.findViewById(R.id.et_choosecolorshade);
        itemadapter = new ItemListAdapter(getActivity(), R.layout.list_item, new ArrayList<ItemAdapter>());
        lv_colorshade = (ListView) v.findViewById(R.id.lv_colorshade);
        lv_colorshade.setAdapter(itemadapter);
        ib_search = (ImageButton) v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        lv_colorshade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);

                if(Index.positionsharedpreferences.getString("position", "").equals("Stock Report")){
                    SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
                    editor.putString( "nomorcolorshade", tv_nomor.getText().toString());
                    editor.putString( "namacolorshade", tv_nama.getText().toString());
                    editor.commit();

                    Fragment fragment = new StockReportFilter();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
                else if(Index.positionsharedpreferences.getString("position", "").equals("Sales Order")){
                    SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                    editor.putString( "nomorcolorshade", tv_nomor.getText().toString());
                    editor.putString( "namacolorshade", tv_nama.getText().toString());
                    editor.commit();

                    Fragment salesorderaddgridbarang = new SalesOrderAddGridBarang();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, salesorderaddgridbarang);
                    transaction.commit();
                }
            }
        });
        lv_colorshade.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            String actionUrl = "Master/alldatacolorshade/";
                            new searchColorShade().execute(actionUrl);
                        }
                    }
                }
            }

        });
        //-----START DECLARE---------------------------------------------------------------------------------------

        scroll = false;
        if(Index.positionsharedpreferences.getString("position", "").equals("Stock Report")){
            nomorbarang = Index.stocksharedpreferences.getString("nomorbarang", "");
        }
        else if(Index.positionsharedpreferences.getString("position", "").equals("Sales Order")){
            nomorbarang = Index.salesordersharedpreferences.getString("nomorbarang", "");
        }

        if (nomorbarang.equals("")) {
            Toast.makeText(getContext(), "Choose Item", Toast.LENGTH_LONG).show();
        }else {
            String actionUrl = "Master/alldatacolorshade/";
            new searchColorShade().execute(actionUrl);
        }

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_search){
            itemadapter.clear();
            counter = 0;

            if (nomorbarang.equals("")) {
                Toast.makeText(getContext(), "Choose Item", Toast.LENGTH_LONG).show();
            }else {
                String actionUrl = "Master/alldatacolorshade/";
                new searchColorShade().execute(actionUrl);
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

    private class searchColorShade extends AsyncTask<String, Void, String> {
        String search = et_choosecolorshade.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("barang_nomor", nomorbarang);
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
                            String nomor = (obj.getString("vcShade"));
                            String nama = (obj.getString("vcShade"));

                            itemadapter.add(new ItemAdapter(nomor, nama));
                            itemadapter.notifyDataSetChanged();
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
                Toast.makeText(getContext(), "Color Shade Load Failed", Toast.LENGTH_LONG).show();
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