package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

public class ChoosePeriode extends Fragment {

    private ItemListAdapter itemadapter;

    private ListView lv_periode;

    FloatingActionButton btn_addtarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chooseperiode, container, false);
        getActivity().setTitle("Periode List");

        Index.salestargetsharedpreferences.edit().clear().commit();

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_add_onclick(v);

        itemadapter = new ItemListAdapter(getActivity(), R.layout.list_item, new ArrayList<ItemAdapter>());
        lv_periode = (ListView) v.findViewById(R.id.lv_periode);
        lv_periode.setAdapter(itemadapter);
        lv_periode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                v.startAnimation(Index.listeffect);

                TextView tv_nomor = (TextView) v.findViewById(R.id.tv_nomor);
                TextView tv_nama = (TextView) v.findViewById(R.id.tv_nama);

                SharedPreferences.Editor editor = Index.salestargetsharedpreferences.edit();

                String[] pieces = tv_nomor.getText().toString().split("\\|");
                editor.putString( "bulan", pieces[0]);
                editor.putString( "tahun", pieces[1]);
                editor.putString( "_periode", tv_nama.getText().toString());
                editor.commit();

                Fragment fragment = new SalesTargetList();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //-----END DECLARE---------------------------------------------------------------------------------------
        String actionUrl = "Target/getAllPeriode/";
        new searchPeriode().execute( actionUrl );

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class searchPeriode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Index.jsonObject = new JSONObject();
            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resulttt", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0) {
                    for (int i = jsonarray.length() - 1; i >= 0; i--) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if (!obj.has("query")) {
                            String nomor = (obj.getString("intPeriode") + "|" + obj.getString("intTahun"));

                            String bulan = obj.getString("intPeriode");
                            String nama = "";

                            if(bulan.equals("1")) nama = "Januari ";
                            else if(bulan.equals("2")) nama = "Februari ";
                            else if(bulan.equals("3")) nama = "Maret ";
                            else if(bulan.equals("4")) nama = "April ";
                            else if(bulan.equals("5")) nama = "Mei ";
                            else if(bulan.equals("6")) nama = "Juni ";
                            else if(bulan.equals("7")) nama = "Juli ";
                            else if(bulan.equals("8")) nama = "Agustus ";
                            else if(bulan.equals("9")) nama = "September ";
                            else if(bulan.equals("10")) nama = "Oktober ";
                            else if(bulan.equals("11")) nama = "November ";
                            else if(bulan.equals("12")) nama = "Desember ";

                            nama += " " + obj.getString("intTahun");

                            itemadapter.add(new ItemAdapter(nomor, nama));
                            itemadapter.notifyDataSetChanged();
                        }
                    }
                }

                Index.pDialog.dismiss();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Periode Load Failed", Toast.LENGTH_LONG).show();
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