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
public class GridBiayaEstimasiListAdapter extends ArrayAdapter<GridBiayaEstimasiAdapter> {

    private List<GridBiayaEstimasiAdapter> items;
    private int layoutResourceId;
    private Context context;

    public GridBiayaEstimasiListAdapter(Context context, int layoutResourceId, List<GridBiayaEstimasiAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        GridBiayaEstimasiAdapter adapterGridBiayaEstimasi;
        TextView nomor;
        TextView nama;
        TextView nomorsupplier;
        TextView supplier;
        TextView deskripsi;
        TextView totalsupp;
        TextView totalcust;
        TextView ppn;
        TextView total;
        TextView totalidr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterGridBiayaEstimasi = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_jenisbiaya);
        holder.nomorsupplier = (TextView)row.findViewById(R.id.tv_nomorsupplier);
        holder.supplier = (TextView)row.findViewById(R.id.tv_supplier);
        holder.deskripsi = (TextView)row.findViewById(R.id.tv_deskripsi);
        holder.totalsupp = (TextView)row.findViewById(R.id.tv_totalsupp);
        holder.totalcust = (TextView)row.findViewById(R.id.tv_totalcust);
        holder.ppn = (TextView)row.findViewById(R.id.tv_ppn);
        holder.total = (TextView)row.findViewById(R.id.tv_total);
        holder.totalidr = (TextView)row.findViewById(R.id.tv_totalidr);

        row.setTag(holder);
        setupGridBiayaEstimasi(holder);

        return row;
    }

    private void setupGridBiayaEstimasi(Holder holder) {
        holder.nomor.setText(holder.adapterGridBiayaEstimasi.getNomor());
        holder.nama.setText(holder.adapterGridBiayaEstimasi.getNama());
        holder.nomorsupplier.setText(holder.adapterGridBiayaEstimasi.getNomorsupplier());
        holder.supplier.setText(holder.adapterGridBiayaEstimasi.getSupplier());
        holder.deskripsi.setText(holder.adapterGridBiayaEstimasi.getDeskripsi());
        holder.totalsupp.setText(GlobalFunction.delimeter(holder.adapterGridBiayaEstimasi.getTotalsupp()));
        holder.totalcust.setText(GlobalFunction.delimeter(holder.adapterGridBiayaEstimasi.getTotalcust()));
        holder.ppn.setText(GlobalFunction.delimeter(holder.adapterGridBiayaEstimasi.getPpn()));
        holder.total.setText(GlobalFunction.delimeter(holder.adapterGridBiayaEstimasi.getTotal()));
        holder.totalidr.setText(GlobalFunction.delimeter(holder.adapterGridBiayaEstimasi.getTotalidr()));
    }
}
