package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class GridSalesListAdapter extends ArrayAdapter<GridSalesAdapter> {

    private List<GridSalesAdapter> items;
    private int layoutResourceId;
    private Context context;

    public GridSalesListAdapter(Context context, int layoutResourceId, List<GridSalesAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        GridSalesAdapter adapterGridSales;
        TextView nomor;
        TextView nama;
        TextView target;
        TextView periode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterGridSales = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_sales);
        holder.target = (TextView)row.findViewById(R.id.tv_target);
        holder.periode = (TextView)row.findViewById(R.id.tv_periode);

        row.setTag(holder);
        setupGridBarang(holder);

        return row;
    }

    private void setupGridBarang(Holder holder) {
        holder.nomor.setText(holder.adapterGridSales.getNomor());
        holder.nama.setText(holder.adapterGridSales.getNama());

        DecimalFormat format=new DecimalFormat("#,###.##");

        Double Raw = Double.parseDouble(holder.adapterGridSales.getTarget());
        String target = String.valueOf(format.format(Raw));
        holder.target.setText(target);

        String periode = holder.adapterGridSales.getTahun();
        if(holder.adapterGridSales.getBulan().equals("1")) periode = "Januari " + periode;
        else if(holder.adapterGridSales.getBulan().equals("2")) periode = "Februari " + periode;
        else if(holder.adapterGridSales.getBulan().equals("3")) periode = "Maret " + periode;
        else if(holder.adapterGridSales.getBulan().equals("4")) periode = "April " + periode;
        else if(holder.adapterGridSales.getBulan().equals("5")) periode = "Mei " + periode;
        else if(holder.adapterGridSales.getBulan().equals("6")) periode = "Juni " + periode;
        else if(holder.adapterGridSales.getBulan().equals("7")) periode = "Juli " + periode;
        else if(holder.adapterGridSales.getBulan().equals("8")) periode = "Agustus " + periode;
        else if(holder.adapterGridSales.getBulan().equals("9")) periode = "September " + periode;
        else if(holder.adapterGridSales.getBulan().equals("10")) periode = "Oktober " + periode;
        else if(holder.adapterGridSales.getBulan().equals("11")) periode = "November " + periode;
        else if(holder.adapterGridSales.getBulan().equals("12")) periode = "Desember " + periode;


        holder.periode.setText(periode);
    }
}
