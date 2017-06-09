package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
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

public class ChooseValuta extends Fragment implements View.OnClickListener {

    private MoreItemListAdapter moreitemadapter;

    private EditText et_choosearea;
    private ImageButton ib_search;
    private ListView lv_area;

    private Boolean scroll = false;
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choosevaluta, container, false);
        getActivity().setTitle("Choose Currency");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_choosearea = (EditText) v.findViewById(R.id.et_choosevaluta);
        moreitemadapter = new MoreItemListAdapter(getActivity(), R.layout.list_more_item, new ArrayList<MoreItemAdapter>());
        lv_area = (ListView) v.findViewById(R.id.lv_valuta);
        lv_area.setAdapter(moreitemadapter);
        ib_search = (ImageButton) v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        lv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);
                TextView tv_kurs = (TextView) v.findViewById(R.id.tv_data);

                if(Index.positionsharedpreferences.getString("position", "").equals("Sales Order")){
                    SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                    editor.putString( "nomorvaluta", tv_nomor.getText().toString());
                    editor.putString( "namavaluta", tv_nama.getText().toString());
                    editor.putString( "kursvaluta", tv_kurs.getText().toString());
                    editor.commit();

                    Fragment fragment = new SalesOrderAddHeader();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
                else if(Index.positionsharedpreferences.getString("position", "").equals("pdf Piutang Report")){
                    SharedPreferences sp = getActivity().getSharedPreferences("piutang", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString( "nomorvaluta", tv_nomor.getText().toString());
                    editor.putString( "namavaluta", tv_nama.getText().toString());
                    editor.putString( "kursvaluta", tv_kurs.getText().toString());
                    editor.commit();

                    Fragment fragment = new pdf_piutang();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
                else if(Index.positionsharedpreferences.getString("position", "").equals("pdf Hutang Report")){
                    SharedPreferences sp = getActivity().getSharedPreferences("hutang", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString( "nomorvaluta", tv_nomor.getText().toString());
                    editor.putString( "namavaluta", tv_nama.getText().toString());
                    editor.putString( "kursvaluta", tv_kurs.getText().toString());
                    editor.commit();

                    Fragment fragment = new pdf_hutang();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
            }
        });
        lv_area.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                        String actionUrl = "Master/alldatavaluta/";
                        new searchValuta().execute( actionUrl );
                    }
                }
            }

        });
        //-----END DECLARE---------------------------------------------------------------------------------------

        scroll = false;
        String actionUrl = "Master/alldatavaluta/";
        new searchValuta().execute( actionUrl );

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.ib_search){
            moreitemadapter.clear();
            counter = 0;

            String actionUrl = "Master/alldatavaluta/";
            new searchValuta().execute( actionUrl );
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class searchValuta extends AsyncTask<String, Void, String> {
        String search = et_choosearea.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
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
                            String nomor = (obj.getString("intNomor"));
                            String nama = (obj.getString("vcNama")) + " : " + (obj.getString("dtTanggal"));
                            Double omzetRaw = Double.parseDouble((obj.getString("decKurs")));
                            String omzet = String.valueOf(format.format(omzetRaw));

                            moreitemadapter.add(new MoreItemAdapter(nomor, nama, omzet));
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
                Toast.makeText(getContext(), "Currency Load Failed", Toast.LENGTH_LONG).show();
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