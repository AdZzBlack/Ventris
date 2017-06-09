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
public class StockReportListAdapter extends ArrayAdapter<StockReportAdapter> {

    private List<StockReportAdapter> items;
    private int layoutResourceId;
    private Context context;

    public StockReportListAdapter(Context context, int layoutResourceId, List<StockReportAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        StockReportAdapter adapterItem;
        TextView kode;
        TextView nama;
        TextView gudang;
        TextView stock;
        TextView pending;
        TextView satuan1;
        TextView satuan2;
        TextView info1;
        TextView info2;
        TextView shade;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterItem = items.get(position);

        holder.kode = (TextView)row.findViewById(R.id.tv_kodebarang);
        holder.nama = (TextView)row.findViewById(R.id.tv_namabeli);
        holder.gudang = (TextView)row.findViewById(R.id.tv_gudang);
        holder.stock = (TextView)row.findViewById(R.id.tv_stock);
        holder.pending = (TextView)row.findViewById(R.id.tv_pending);
        holder.satuan1 = (TextView)row.findViewById(R.id.tv_satuan);
        holder.satuan2 = (TextView)row.findViewById(R.id.tv_satuan1);
        holder.info1 = (TextView)row.findViewById(R.id.tv_info1);
        holder.info2 = (TextView)row.findViewById(R.id.tv_info2);
        holder.shade = (TextView)row.findViewById(R.id.tv_shade);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.kode.setText(holder.adapterItem.getKode());
        holder.nama.setText(holder.adapterItem.getNama());
        holder.gudang.setText(holder.adapterItem.getGudang());
        holder.stock.setText(GlobalFunction.delimeter(holder.adapterItem.getStock()));
        holder.pending.setText(GlobalFunction.delimeter(holder.adapterItem.getPending()));
        holder.satuan1.setText(holder.adapterItem.getSatuan());
        holder.satuan2.setText(holder.adapterItem.getSatuan());
        holder.shade.setText(holder.adapterItem.getShade());
        if(holder.adapterItem.getInfo1().equals("") || holder.adapterItem.getInfo1().equals("null"))
        {
            holder.info1.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.info1.setText("--> " + holder.adapterItem.getInfo1());
        }
        if(holder.adapterItem.getInfo2().equals("") || holder.adapterItem.getInfo2().equals("null"))
        {
            holder.info2.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.info2.setText("--> " + holder.adapterItem.getInfo2());
        }
    }
}
