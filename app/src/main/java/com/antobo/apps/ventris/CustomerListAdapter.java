package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class CustomerListAdapter extends ArrayAdapter<CustomerAdapter> {

    private List<CustomerAdapter> items;
    private int layoutResourceId;
    private Context context;

    public CustomerListAdapter(Context context, int layoutResourceId, List<CustomerAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        CustomerAdapter adapterCustomer;
        TextView nomor;
        TextView nama;
        TextView alamat;
        TextView hp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterCustomer = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_nama);
        holder.alamat = (TextView)row.findViewById(R.id.tv_alamat);
        holder.hp = (TextView)row.findViewById(R.id.tv_hp);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.nomor.setText(holder.adapterCustomer.getNomor());
        holder.nama.setText(holder.adapterCustomer.getNama());
        holder.alamat.setText(holder.adapterCustomer.getAlamat());
        holder.hp.setText(holder.adapterCustomer.getHp());
    }
}
