package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class SalesTargetList extends Fragment {

    JSONObject jsonObject;

    private SalesTargetListAdapter salestargetadapter;
    private ListView lv_salestarget;

    FloatingActionButton btn_addtarget;

    private Boolean scroll = false;
    public int counter = 0;

    private String bulan, tahun, periode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salestarget_list, container, false);

        bulan = Index.salestargetsharedpreferences.getString("bulan", "");
        tahun = Index.salestargetsharedpreferences.getString("tahun", "");
        periode = Index.salestargetsharedpreferences.getString("_periode", "");

        getActivity().setTitle(periode);

        //---------------------------------------------------------
            salestargetadapter = new SalesTargetListAdapter(getActivity(), R.layout.list_salestarget, new ArrayList<SalesTargetAdapter>());
            lv_salestarget = (ListView) v.findViewById(R.id.lv_salestarget);
            lv_salestarget.setAdapter(salestargetadapter);

            btn_add_onclick(v);

            lv_salestarget.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                            String actionUrl = "Target/getAllTarget/";
                            new getSalesTarget().execute( actionUrl );
                        }
                    }
                }

            });
            lv_salestarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    v.startAnimation(Index.listeffect);

                    TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                    SalesTargetReview.salestarget_nomor = tv_nomor.getText().toString();

                    Fragment fragment = new SalesTargetReview();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        //---------------------------------------------------------

        scroll = false;
        String actionUrl = "Target/getAllTarget/";
        new getSalesTarget().execute( actionUrl );

        return v;
    }


    private void btn_add_onclick(View v){
        btn_addtarget = (FloatingActionButton) v.findViewById(R.id.btn_addsalestarget);
        btn_addtarget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Fragment salestarget = new SalesTarget();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, salestarget);
            transaction.addToBackStack(null);
            transaction.commit();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        counter = 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class getSalesTarget extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("limit", counter);
                Index.jsonObject.put("periode", bulan);
                Index.jsonObject.put("tahun", tahun);
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
                        if(!obj.has("query")){
                            String nomor = (obj.getString("intNomor"));
                            String bex = (obj.getString("vcNama"));
                            String bulan = (obj.getString("intPeriode"));
                            String tahun = (obj.getString("intTahun"));
                            String target = (obj.getString("decTarget"));

                            salestargetadapter.add(new SalesTargetAdapter(nomor, bex, bulan, tahun, target));
                            salestargetadapter.notifyDataSetChanged();
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
}