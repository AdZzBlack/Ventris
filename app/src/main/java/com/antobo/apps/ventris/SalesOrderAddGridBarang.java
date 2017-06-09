package com.antobo.apps.ventris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SalesOrderAddGridBarang extends Fragment implements View.OnClickListener{

    private TextView tv_jumlah, tv_jumlahunit, tv_barang, tv_satuanharga, tv_satuanunit, tv_harga, tv_netto, tv_total, tv_colorshade;
    private EditText et_discount1, et_discount2, et_discount3, et_jumlahcolorshade;
    private Button btn_add, btn_back, btn_next;
    private ListView lv_gridbarang;

    private GridBarangListAdapter gridbarangadapter;
    private Set<String> setData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_add_grid_barang, container, false);
        getActivity().setTitle("Item");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_barang = (TextView) v.findViewById(R.id.tv_barang);
        tv_barang.setOnClickListener(this);
        tv_satuanharga = (TextView) v.findViewById(R.id.tv_satuanharga);
        tv_satuanharga.setOnClickListener(this);
        tv_satuanunit = (TextView) v.findViewById(R.id.tv_satuanunit);
        tv_satuanunit.setOnClickListener(this);
        tv_harga = (TextView) v.findViewById(R.id.tv_harga);
        tv_netto = (TextView) v.findViewById(R.id.tv_netto);
        tv_total = (TextView) v.findViewById(R.id.tv_total);
        tv_colorshade = (TextView) v.findViewById(R.id.tv_colorshade);
        tv_colorshade.setOnClickListener(this);
        tv_jumlah = (TextView) v.findViewById(R.id.tv_jumlah);
        tv_jumlahunit = (TextView) v.findViewById(R.id.tv_jumlahunit);
        et_discount1 = (EditText) v.findViewById(R.id.et_discount1);
        et_discount2 = (EditText) v.findViewById(R.id.et_discount2);
        et_discount3 = (EditText) v.findViewById(R.id.et_discount3);
        et_jumlahcolorshade = (EditText) v.findViewById(R.id.et_jumlahcolorshade);
        gridbarangadapter = new GridBarangListAdapter(getActivity(), R.layout.list_grid_barang, new ArrayList<GridBarangAdapter>());
        lv_gridbarang = (ListView) v.findViewById(R.id.lv_gridbarang);
        lv_gridbarang.setAdapter(gridbarangadapter);
        btn_add = (Button) v.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_back = (Button) v.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        //-----END DECLARE---------------------------------------------------------------------------------------

        tv_barang.setText(Index.salesordersharedpreferences.getString("namabarang", ""));
        tv_satuanharga.setText(Index.salesordersharedpreferences.getString("namasatuanharga", ""));
        tv_satuanunit.setText(Index.salesordersharedpreferences.getString("namasatuanunit", ""));
        tv_colorshade.setText(Index.salesordersharedpreferences.getString("namacolorshade", ""));
        tv_harga.setText(Index.salesordersharedpreferences.getString("hargasatuanharga", ""));

        et_discount1.setText(Index.salesordersharedpreferences.getString("diskon1", ""));
        et_discount2.setText(Index.salesordersharedpreferences.getString("diskon2", ""));
        et_discount3.setText(Index.salesordersharedpreferences.getString("diskon3", ""));
        et_jumlahcolorshade.setText(Index.salesordersharedpreferences.getString("jumlahcolorshade", ""));

        lv_gridbarang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                alertbox("Item Detail", "Delete Item Data ?", getActivity(), pos);
                return true;
            }
        });

        et_discount1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "diskon1", et_discount1.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        et_discount2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "diskon2", et_discount2.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        et_discount3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "diskon3", et_discount3.getText().toString());
                    poseditor.commit();

                    calculateTotal();
                }
            }
        });

        et_jumlahcolorshade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "jumlahcolorshade", et_jumlahcolorshade.getText().toString());
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
        if(v.getId() == R.id.tv_barang){
            Fragment fragment = new ChooseBarang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_satuanharga){
            Fragment fragment = new ChooseSatuanHarga();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_satuanunit){
            Fragment fragment = new ChooseSatuanUnit();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_colorshade){
            Fragment fragment = new ChooseColorShade();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_add){
            calculateTotal();

            String nomorbarang = Index.salesordersharedpreferences.getString("nomorbarang", "");
            String kodedannama = Index.salesordersharedpreferences.getString("kodebarang", "") + " - " + Index.salesordersharedpreferences.getString("namabarang", "");
            String nomorsatuanharga = Index.salesordersharedpreferences.getString("nomorsatuanharga", "");
            String nomorsatuanunit = Index.salesordersharedpreferences.getString("nomorsatuanunit", "");
            String jumlahharga = tv_jumlah.getText().toString();
            String satuanharga = tv_satuanharga.getText().toString();
            String harga = tv_harga.getText().toString();
            String jumlahunit = tv_jumlahunit.getText().toString();
            String satuanunit = tv_satuanunit.getText().toString();
            String disc1 = et_discount1.getText().toString();
            String disc2 = et_discount2.getText().toString();
            String disc3 = et_discount3.getText().toString();
//            String netto = tv_netto.getText().toString();
//            String total = tv_total.getText().toString();
            String netto = Index.salesordersharedpreferences.getString("harganetto", "");
            String total = Index.salesordersharedpreferences.getString("hargatotal", "");
            String colorshade = tv_colorshade.getText().toString();
            String jumlahcolorshade = et_jumlahcolorshade.getText().toString();

            if(nomorbarang.equals("")||kodedannama.equals("")||nomorsatuanharga.equals("")||nomorsatuanunit.equals("")||jumlahharga.equals("")||satuanharga.equals("")||harga.equals("")||jumlahunit.equals("")||satuanunit.equals("")||colorshade.equals("")||jumlahcolorshade.equals(""))
            {
                Toast.makeText(getContext(), "Some field required", Toast.LENGTH_LONG).show();
            }
            else
            {
                String dataOldListBarang = Index.salesordersharedpreferences.getString("datalistbarang", "");

                Boolean same = false;

                if(!dataOldListBarang.equals("")){
                    String[] pieces = dataOldListBarang.trim().split("\\|");
                    for(int i=0 ; i < pieces.length ; i++){
                        String string = pieces[i];
                        String[] parts = string.trim().split("\\~");

                        if(nomorbarang.equals(parts[0]))
                        {
                            same = true;
                        }
                    }
                }

                if(same)
                {
                    Toast.makeText(getContext(), "Item already ordered", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
                    editor.putString( "datalistbarang", dataOldListBarang + nomorbarang + "~" + kodedannama + "~" + nomorsatuanharga + "~" + nomorsatuanunit + "~" + jumlahharga + "~" + satuanharga + "~" + harga + "~" + jumlahunit + "~" + satuanunit + "~" + disc1 + "~" + disc2 + "~" + disc3 + "~" + netto + "~" + total + "~" + colorshade + "~" + jumlahcolorshade + "|");
                    editor.commit();
                    gridbarangadapter.add(new GridBarangAdapter(nomorbarang, kodedannama, nomorsatuanharga, nomorsatuanunit, jumlahharga, satuanharga, harga, jumlahunit, satuanunit, disc1, disc2, disc3, netto, total, colorshade, jumlahcolorshade));
                    gridbarangadapter.notifyDataSetChanged();
                }
            }
        }
        else if(v.getId() == R.id.btn_back){
            Fragment fragment = new SalesOrderAddHeader();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_next){
            String dataListBarang = Index.salesordersharedpreferences.getString("datalistbarang", "");

            if(dataListBarang.equals(""))
            {
                Toast.makeText(getContext(), "Must select at least one item", Toast.LENGTH_LONG).show();
            }
            else
            {
                Fragment fragment = new SalesOrderAddGridBiayaEstimasi();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

    public void calculateTotal(){
        Double harga;
        int jumlahshade, diskon1, diskon2, diskon3;

        try {
            String hargaRaw = (tv_harga.getText().toString().replace(",", ""));
            harga = Double.parseDouble(hargaRaw);
        }catch (Exception ex){
            harga = 0.0;
        }
        try {
            diskon1 = Integer.parseInt(et_discount1.getText().toString());
        }catch (Exception ex){
            diskon1 = 0;
        }
        try {
            diskon2 = Integer.parseInt(et_discount2.getText().toString());
        }catch (Exception ex){
            diskon2 = 0;
        }
        try {
            diskon3 = Integer.parseInt(et_discount3.getText().toString());
        }catch (Exception ex){
            diskon3 = 0;
        }
        try {
            jumlahshade = Integer.parseInt(et_jumlahcolorshade.getText().toString());
        }catch (Exception ex){
            jumlahshade = 0;
        }

        try{
            int jumlah = jumlahshade * Integer.parseInt(Index.salesordersharedpreferences.getString("konversisatuanunit", "0.0"));
            Double dis1_nominal = (harga * diskon1) / 100;
            harga = harga - dis1_nominal;
            Double dis2_nominal = (harga * diskon2) / 100;
            harga = harga - dis2_nominal;
            Double dis3_nominal = (harga * diskon3) / 100;
            harga = harga - dis3_nominal;
            Double total = jumlah * harga;

            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString("harganetto", String.valueOf(harga));
            editor.putString("hargatotal", String.format("%.0f", total));
            editor.commit();

            tv_jumlah.setText(String.valueOf(jumlah));
            tv_jumlahunit.setText(String.valueOf(jumlahshade));
            tv_netto.setText(GlobalFunction.delimeter(String.valueOf(harga)));
            tv_total.setText(GlobalFunction.delimeter(String.valueOf(total)));
        }catch (Exception e){

        }
    }

    public void refreshGrid(){
        gridbarangadapter.clear();
        String datalistbarang = Index.salesordersharedpreferences.getString("datalistbarang", "");
        if(!datalistbarang.equals("")){
            String[] pieces = datalistbarang.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                String string = pieces[i];
                String[] parts = string.trim().split("\\~");

                String pieces14, pieces15;
                try {
                    pieces14 = parts[14];
                }catch (Exception ex){
                    pieces14 = "";
                }
                try {
                    pieces15 = parts[15];
                }catch (Exception ex){
                    pieces15 = "";
                }

                gridbarangadapter.add(new GridBarangAdapter(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9], parts[10], parts[11], parts[12], parts[13], pieces14, pieces15));
                gridbarangadapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteGrid(final int pos){
        String realData = "";
        String datalistbarang = Index.salesordersharedpreferences.getString("datalistbarang", "");
        if(!datalistbarang.equals("")){
            String[] pieces = datalistbarang.trim().split("\\|");
            for(int i=0 ; i < pieces.length ; i++){
                if(i != pos){
                    realData += pieces[i] + "|";
                }
            }
            SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();
            editor.putString( "datalistbarang", realData);
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