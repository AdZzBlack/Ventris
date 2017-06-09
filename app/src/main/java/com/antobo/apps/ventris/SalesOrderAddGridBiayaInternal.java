package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SalesOrderAddGridBiayaInternal extends Fragment implements View.OnClickListener{

    private TextView tv_jenisbiaya, tv_total, tv_totalidr;
    private EditText et_deskripsi, et_totalcust, et_ppn;
    private Button btn_add, btn_back, btn_next;
    private ListView lv_gridbiayainternal;

    private GridBiayaInternalListAdapter gridbiayainternaladapter;
    private Set<String> setData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_add_grid_biayainternal, container, false);
        getActivity().setTitle("Internal Cost Sales Order");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_jenisbiaya = (TextView) v.findViewById(R.id.tv_jenisbiaya);
        tv_jenisbiaya.setOnClickListener(this);
        et_totalcust = (EditText) v.findViewById(R.id.et_totalcust);
        et_ppn = (EditText) v.findViewById(R.id.et_ppn);
        tv_total = (TextView) v.findViewById(R.id.tv_total);
        tv_totalidr = (TextView) v.findViewById(R.id.tv_totalidr);
        et_deskripsi = (EditText) v.findViewById(R.id.et_deskripsi);
        gridbiayainternaladapter = new GridBiayaInternalListAdapter(getActivity(), R.layout.list_grid_biayainternal, new ArrayList<GridBiayaInternalAdapter>());
        lv_gridbiayainternal = (ListView) v.findViewById(R.id.lv_gridbiayainternal);
        lv_gridbiayainternal.setAdapter(gridbiayainternaladapter);
        btn_add = (Button) v.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_back = (Button) v.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        tv_jenisbiaya.setText(Index.salesordersharedpreferences.getString("namajenisbiayainternal", ""));

        et_totalcust.setText(Index.salesordersharedpreferences.getString("totalcust_internal", ""));
        et_ppn.setText(Index.salesordersharedpreferences.getString("ppn_internal", ""));

        lv_gridbiayainternal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                alertbox("Internal Cost Detail", "Delete Internal Cost Data?", getActivity(), pos);
                return true;
            }
        });

        et_totalcust.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "totalcust_internal", et_totalcust.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        et_ppn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "ppn_internal", et_ppn.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        refreshGrid();
        calculateTotal();

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Sales Order");
        poseditor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.tv_jenisbiaya){
            Fragment fragment = new ChooseJenisBiayaInternal();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_add){
            calculateTotal();

            String nomorbiaya = Index.salesordersharedpreferences.getString("nomorjenisbiayainternal", "");
            String jenisbiaya = tv_jenisbiaya.getText().toString();
            String deskripsi = et_deskripsi.getText().toString();
            String totalcust = et_totalcust.getText().toString();
            String ppn = et_ppn.getText().toString();
//            String total = tv_total.getText().toString();
//            String totalidr = tv_totalidr.getText().toString();
            String total = Index.salesordersharedpreferences.getString("internaltotal", "");
            String totalidr = Index.salesordersharedpreferences.getString("internaltotalidr", "");

            if(jenisbiaya.equals("")||totalcust.equals("")||ppn.equals(""))
            {
                Toast.makeText(getContext(), "Some field required", Toast.LENGTH_LONG).show();
            }
            else
            {
                String dataOldListbiayaestimasi = Index.salesordersharedpreferences.getString("datalistbiayainternal", "");
                SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                editor.putString( "datalistbiayainternal", dataOldListbiayaestimasi + nomorbiaya + "~" + jenisbiaya + "~" + deskripsi + "~" + totalcust + "~" + ppn + "~" + total + "~" + totalidr + "|");
                editor.commit();
                gridbiayainternaladapter.add(new GridBiayaInternalAdapter(nomorbiaya, jenisbiaya, deskripsi, totalcust, ppn, total, totalidr));
                gridbiayainternaladapter.notifyDataSetChanged();
            }
        }
        else if(v.getId() == R.id.btn_back){
            Fragment fragment = new SalesOrderAddGridBiayaEstimasi();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_next){
            Fragment fragment = new SalesOrderAddHeaderKeterangan();
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

    public void calculateTotal(){
        Double totalcust, ppn, kurs;
        try {
            totalcust = Double.parseDouble(et_totalcust.getText().toString());
        }catch (Exception ex){
            totalcust = 0.0;
        }
        try {
            ppn = Double.parseDouble(et_ppn.getText().toString());
        }catch (Exception ex){
            ppn = 0.0;
        }
        try {
            String kursRaw = (Index.salesordersharedpreferences.getString("kursvaluta", "0.0").replace(",", ""));
            kurs = Double.parseDouble(kursRaw);
        }catch (Exception ex){
            kurs = 0.0;
        }

        try{
            Double ppn_nominal = (totalcust * ppn) / 100;
            Double nett = ppn_nominal + totalcust;
            Double nett_idr = nett * kurs;

            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString( "internaltotal", String.format("%.0f", nett));
            editor.putString( "internaltotalidr", String.format("%.0f", nett_idr));
            editor.commit();

            tv_total.setText(GlobalFunction.delimeter(String.valueOf(nett)));
            tv_totalidr.setText(GlobalFunction.delimeter(String.valueOf(nett_idr)));
        }catch (Exception e){

        }
    }

    public void refreshGrid(){
        gridbiayainternaladapter.clear();
        String datalistbiayainternal = Index.salesordersharedpreferences.getString("datalistbiayainternal", "");
        if(!datalistbiayainternal.equals("")){
            String[] pieces = datalistbiayainternal.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");
                gridbiayainternaladapter.add(new GridBiayaInternalAdapter(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
                gridbiayainternaladapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteGrid(final int pos){
        String realData = "";
        String datalistbiayainternal = Index.salesordersharedpreferences.getString("datalistbiayainternal", "");
        if(!datalistbiayainternal.equals("")){
            String[] pieces = datalistbiayainternal.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                if(i != pos){
                    realData += pieces[i] + "|";
                }
            }
            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString( "datalistbiayainternal", realData);
            editor.commit();

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    refreshGrid();
                }
            }, 300);
        }
    }

    public void alertbox(String title, String message, final Activity activity, final int pos) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteGrid(pos);
            } });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });
        alertDialog.show();
    }
}