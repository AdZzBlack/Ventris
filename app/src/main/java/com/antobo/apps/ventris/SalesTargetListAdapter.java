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
public class SalesTargetListAdapter extends ArrayAdapter<SalesTargetAdapter> {

    private List<SalesTargetAdapter> items;
    private int layoutResourceId;
    private Context context;

    public SalesTargetListAdapter(Context context, int layoutResourceId, List<SalesTargetAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        SalesTargetAdapter adapterItem;
        TextView nomor;
        TextView bex;
        TextView bulan;
        TextView tahun;
        TextView target;
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
        holder.bex = (TextView)row.findViewById(R.id.tv_bex);
        holder.bulan = (TextView)row.findViewById(R.id.tv_bulan);
        holder.tahun = (TextView)row.findViewById(R.id.tv_tahun);
        holder.target = (TextView)row.findViewById(R.id.tv_target);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        String periode = "";
        String bulan = holder.adapterItem.getBulan();

        if(bulan.equals("1")) periode = "Januari";
        else if(bulan.equals("2")) periode = "Februari";
        else if(bulan.equals("3")) periode = "Maret";
        else if(bulan.equals("4")) periode = "April";
        else if(bulan.equals("5")) periode = "Mei";
        else if(bulan.equals("6")) periode = "Juni";
        else if(bulan.equals("7")) periode = "Juli";
        else if(bulan.equals("8")) periode = "Agustus";
        else if(bulan.equals("9")) periode = "September";
        else if(bulan.equals("10")) periode = "Oktober";
        else if(bulan.equals("11")) periode = "November";
        else if(bulan.equals("12")) periode = "Desember";

        holder.nomor.setText(holder.adapterItem.getNomor());
        holder.bex.setText(holder.adapterItem.getBex());
        holder.bulan.setText(periode);
        holder.tahun.setText(holder.adapterItem.getTahun());
        holder.target.setText(GlobalFunction.delimeter(holder.adapterItem.getTarget()) + " IDR");
    }
}
