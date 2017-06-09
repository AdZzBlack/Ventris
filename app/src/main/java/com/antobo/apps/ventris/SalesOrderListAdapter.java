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
 * Created by shirogane on 11/17/2016.
 */
public class SalesOrderListAdapter  extends ArrayAdapter<SalesOrderAdapter> {

    private List<SalesOrderAdapter> items;
    private int layoutResourceId;
    private Context context;

    public SalesOrderListAdapter(Context context, int layoutResourceId, List<SalesOrderAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        SalesOrderAdapter adapterItem;
        TextView nomor;
        TextView kode;
        TextView tanggal;
        TextView tanggalkirim;
        TextView gudang;
        TextView customer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterItem = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.kode = (TextView)row.findViewById(R.id.tv_kode);
        holder.tanggal = (TextView)row.findViewById(R.id.tv_tanggal);
        holder.tanggalkirim = (TextView)row.findViewById(R.id.tv_tanggalkirim);
        holder.gudang = (TextView)row.findViewById(R.id.tv_gudang);
        holder.customer = (TextView)row.findViewById(R.id.tv_customer);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.nomor.setText(holder.adapterItem.getNomor());
        holder.kode.setText(holder.adapterItem.getKode());
        holder.tanggal.setText(holder.adapterItem.getTanggal());
        holder.tanggalkirim.setText(holder.adapterItem.getTanggalkirim());
        holder.gudang.setText(holder.adapterItem.getGudang());
        holder.customer.setText(holder.adapterItem.getCustomer());
    }
}
