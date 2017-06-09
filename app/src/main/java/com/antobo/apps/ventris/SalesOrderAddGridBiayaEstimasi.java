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

public class SalesOrderAddGridBiayaEstimasi extends Fragment implements View.OnClickListener{

    private TextView tv_supplier, tv_jenisbiaya, tv_total, tv_totalidr;
    private EditText et_deskripsi, et_totalsupp, et_totalcust, et_ppn;
    private Button btn_add, btn_back, btn_next;
    private ListView lv_gridbiayaestimasi;

    private GridBiayaEstimasiListAdapter gridbiayaestimasiadapter;
    private Set<String> setData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_add_grid_biayaestimasi, container, false);
        getActivity().setTitle("Estimation Cost");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_supplier = (TextView) v.findViewById(R.id.tv_supplier);
        tv_supplier.setOnClickListener(this);
        tv_jenisbiaya = (TextView) v.findViewById(R.id.tv_jenisbiaya);
        tv_jenisbiaya.setOnClickListener(this);
        et_totalsupp = (EditText) v.findViewById(R.id.et_totalsupp);
        et_totalcust = (EditText) v.findViewById(R.id.et_totalcust);
        et_ppn = (EditText) v.findViewById(R.id.et_ppn);
        tv_total = (TextView) v.findViewById(R.id.tv_total);
        tv_totalidr = (TextView) v.findViewById(R.id.tv_totalidr);
        et_deskripsi = (EditText) v.findViewById(R.id.et_deskripsi);
        gridbiayaestimasiadapter = new GridBiayaEstimasiListAdapter(getActivity(), R.layout.list_grid_biayaestimasi, new ArrayList<GridBiayaEstimasiAdapter>());
        lv_gridbiayaestimasi = (ListView) v.findViewById(R.id.lv_gridbiayaestimasi);
        lv_gridbiayaestimasi.setAdapter(gridbiayaestimasiadapter);
        btn_add = (Button) v.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_back = (Button) v.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        tv_supplier.setText(Index.salesordersharedpreferences.getString("namasupplier", ""));
        tv_jenisbiaya.setText(Index.salesordersharedpreferences.getString("namajenisbiayaestimasi", ""));

        et_totalsupp.setText(Index.salesordersharedpreferences.getString("jumlah", ""));
        et_totalcust.setText(Index.salesordersharedpreferences.getString("jumlahunit", ""));
        et_ppn.setText(Index.salesordersharedpreferences.getString("ppn_estimasi", ""));

        lv_gridbiayaestimasi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                alertbox("Estimation Cost Detail", "Delete Estimation Cost Data?", getActivity(), pos);
                return true;
            }
        });

        et_totalsupp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "totalsupp_estimasi", et_totalsupp.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        et_totalcust.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "totalcust_estimasi", et_totalcust.getText().toString());
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
                    poseditor.putString( "ppn_estimasi", et_ppn.getText().toString());
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
        if(v.getId() == R.id.tv_supplier){
            Fragment fragment = new ChooseSupplier();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_jenisbiaya){
            Fragment fragment = new ChooseJenisBiayaEstimasi();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_add){
            calculateTotal();

            String nomorbiaya = Index.salesordersharedpreferences.getString("nomorjenisbiayaestimasi", "");
            String jenisbiaya = tv_jenisbiaya.getText().toString();
            String nomorsupplier = Index.salesordersharedpreferences.getString("nomorsupplier", "");
            String supplier = tv_supplier.getText().toString();
            String deskripsi = et_deskripsi.getText().toString();
            String totalsupp = et_totalsupp.getText().toString();
            String totalcust = et_totalcust.getText().toString();
            String ppn = et_ppn.getText().toString();
//            String total = tv_total.getText().toString();
//            String totalidr = tv_totalidr.getText().toString();
            String total = Index.salesordersharedpreferences.getString("total", "");;
            String totalidr = Index.salesordersharedpreferences.getString("totalidr", "");;

            if(jenisbiaya.equals("")||supplier.equals("")||totalsupp.equals("")||totalcust.equals("")||ppn.equals(""))
            {
                Toast.makeText(getContext(), "Some field required", Toast.LENGTH_LONG).show();
            }
            else
            {
                String dataOldListbiayaestimasi = Index.salesordersharedpreferences.getString("datalistbiayaestimasi", "");
                SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                editor.putString( "datalistbiayaestimasi", dataOldListbiayaestimasi + nomorbiaya + "~" + jenisbiaya + "~" + nomorsupplier + "~" + supplier + "~" + deskripsi + "~" + totalsupp + "~" + totalcust + "~" + ppn + "~" + total + "~" + totalidr + "|");
                editor.commit();
                gridbiayaestimasiadapter.add(new GridBiayaEstimasiAdapter(nomorbiaya, jenisbiaya, nomorsupplier, supplier, deskripsi, totalsupp, totalcust, ppn, total, totalidr));
                gridbiayaestimasiadapter.notifyDataSetChanged();
            }
        }
        else if(v.getId() == R.id.btn_back){
            Fragment fragment = new SalesOrderAddGridBarang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_next){
            Fragment fragment = new SalesOrderAddGridBiayaInternal();
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
        Double totalsupp, totalcust, ppn, kurs;
        try {
            totalsupp = Double.parseDouble(et_totalsupp.getText().toString());
        }catch (Exception ex){
            totalsupp = 0.0;
        }
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
            Double temptotal = totalsupp + totalcust;
            Double ppn_nominal = (temptotal * ppn) / 100;
            Double nett = ppn_nominal + temptotal;
            Double nett_idr = nett * kurs;

            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString("total", String.format("%.0f", nett));
            editor.putString("totalidr", String.format("%.0f", nett_idr));
            editor.commit();

            tv_total.setText(GlobalFunction.delimeter(String.valueOf(nett)));
            tv_totalidr.setText(GlobalFunction.delimeter(String.valueOf(nett_idr)));
        }catch (Exception e){

        }
    }

    public void refreshGrid(){
        gridbiayaestimasiadapter.clear();
        String datalistbiayaestimasi = Index.salesordersharedpreferences.getString("datalistbiayaestimasi", "");
        if(!datalistbiayaestimasi.equals("")){
            String[] pieces = datalistbiayaestimasi.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");
                gridbiayaestimasiadapter.add(new GridBiayaEstimasiAdapter(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9]));
                gridbiayaestimasiadapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteGrid(final int pos){
        String realData = "";
        String datalistbiayaestimasi = Index.salesordersharedpreferences.getString("datalistbiayaestimasi", "");
        if(!datalistbiayaestimasi.equals("")){
            String[] pieces = datalistbiayaestimasi.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                if(i != pos){
                    realData += pieces[i] + "|";
                }
            }
            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString( "datalistbiayaestimasi", realData);
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