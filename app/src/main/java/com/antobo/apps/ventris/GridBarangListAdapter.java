package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class GridBarangListAdapter extends ArrayAdapter<GridBarangAdapter> {

    private List<GridBarangAdapter> items;
    private int layoutResourceId;
    private Context context;

    public GridBarangListAdapter(Context context, int layoutResourceId, List<GridBarangAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        GridBarangAdapter adapterGridBarang;
        TextView nomor;
        TextView nama;
        TextView nomorsatuanharga;
        TextView nomorsatuanunit;
        TextView jumlahharga;
        TextView satuanharga;
        TextView harga;
        TextView jumlahunit;
        TextView satuanunit;
        TextView disc1;
        TextView disc2;
        TextView disc3;
        TextView netto;
        TextView total;
        TextView colorshade;
        TextView jumlahcolorshade;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterGridBarang = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_barang);
        holder.nomorsatuanharga = (TextView)row.findViewById(R.id.tv_nomorsatuanharga);
        holder.nomorsatuanunit = (TextView)row.findViewById(R.id.tv_nomorsatuanunit);
        holder.jumlahharga = (TextView)row.findViewById(R.id.tv_jumlahharga);
        holder.satuanharga = (TextView)row.findViewById(R.id.tv_satuanharga);
        holder.harga = (TextView)row.findViewById(R.id.tv_harga);
        holder.jumlahunit = (TextView)row.findViewById(R.id.tv_jumlahunit);
        holder.satuanunit = (TextView)row.findViewById(R.id.tv_satuanunit);
        holder.disc1 = (TextView)row.findViewById(R.id.tv_discount1);
        holder.disc2 = (TextView)row.findViewById(R.id.tv_discount2);
        holder.disc3 = (TextView)row.findViewById(R.id.tv_discount3);
        holder.netto = (TextView)row.findViewById(R.id.tv_netto);
        holder.total = (TextView)row.findViewById(R.id.tv_total);
        holder.colorshade = (TextView)row.findViewById(R.id.tv_colorshade);
        holder.jumlahcolorshade = (TextView)row.findViewById(R.id.tv_jumlahcolorshade);

        row.setTag(holder);
        setupGridBarang(holder);

        return row;
    }

    private void setupGridBarang(Holder holder) {
        holder.nomor.setText(holder.adapterGridBarang.getNomor());
        holder.nama.setText(holder.adapterGridBarang.getNama());
        holder.nomorsatuanharga.setText(holder.adapterGridBarang.getNomorsatuanharga());
        holder.nomorsatuanunit.setText(holder.adapterGridBarang.getNomorsatuanunit());
        holder.jumlahharga.setText(holder.adapterGridBarang.getJumlahharga());
        holder.satuanharga.setText(holder.adapterGridBarang.getSatuanharga());
        holder.harga.setText(holder.adapterGridBarang.getHarga());
        holder.jumlahunit.setText(holder.adapterGridBarang.getJumlahunit());
        holder.satuanunit.setText(holder.adapterGridBarang.getSatuanunit());
        holder.disc1.setText(holder.adapterGridBarang.getDisc1());
        holder.disc2.setText(holder.adapterGridBarang.getDisc2());
        holder.disc3.setText(holder.adapterGridBarang.getDisc3());
        holder.netto.setText(GlobalFunction.delimeter(holder.adapterGridBarang.getNetto()));
        holder.total.setText(GlobalFunction.delimeter(holder.adapterGridBarang.getTotal()));
        holder.colorshade.setText(holder.adapterGridBarang.getColorshade());
        holder.jumlahcolorshade.setText(holder.adapterGridBarang.getJumlahcolorshade());
    }
}
