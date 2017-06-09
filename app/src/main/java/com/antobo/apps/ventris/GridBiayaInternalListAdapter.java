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
public class GridBiayaInternalListAdapter extends ArrayAdapter<GridBiayaInternalAdapter> {

    private List<GridBiayaInternalAdapter> items;
    private int layoutResourceId;
    private Context context;

    public GridBiayaInternalListAdapter(Context context, int layoutResourceId, List<GridBiayaInternalAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        GridBiayaInternalAdapter adapterGridBiayaInternal;
        TextView nomor;
        TextView nama;
        TextView deskripsi;
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
        holder.adapterGridBiayaInternal = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_jenisbiaya);
        holder.deskripsi = (TextView)row.findViewById(R.id.tv_deskripsi);
        holder.totalcust = (TextView)row.findViewById(R.id.tv_totalcust);
        holder.ppn = (TextView)row.findViewById(R.id.tv_ppn);
        holder.total = (TextView)row.findViewById(R.id.tv_total);
        holder.totalidr = (TextView)row.findViewById(R.id.tv_totalidr);

        row.setTag(holder);
        setupGridBiayaInternal(holder);

        return row;
    }

    private void setupGridBiayaInternal(Holder holder) {
        holder.nomor.setText(holder.adapterGridBiayaInternal.getNomor());
        holder.nama.setText(holder.adapterGridBiayaInternal.getNama());
        holder.deskripsi.setText(holder.adapterGridBiayaInternal.getDeskripsi());
        holder.totalcust.setText(GlobalFunction.delimeter(holder.adapterGridBiayaInternal.getTotalcust()));
        holder.ppn.setText(GlobalFunction.delimeter(holder.adapterGridBiayaInternal.getPpn()));
        holder.total.setText(GlobalFunction.delimeter(holder.adapterGridBiayaInternal.getTotal()));
        holder.totalidr.setText(GlobalFunction.delimeter(holder.adapterGridBiayaInternal.getTotalidr()));
    }
}
