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
public class OmzetReportListAdapter_new extends ArrayAdapter<OmzetReportAdapter_new> {

    private List<OmzetReportAdapter_new> items;
    private int layoutResourceId;
    private Context context;

    public OmzetReportListAdapter_new(Context context, int layoutResourceId, List<OmzetReportAdapter_new> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        OmzetReportAdapter_new adapterItem;
        TextView sales;
        TextView omzet;
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

        holder.sales = (TextView)row.findViewById(R.id.tv_sales);
        holder.omzet = (TextView)row.findViewById(R.id.tv_omzet);
        holder.target = (TextView)row.findViewById(R.id.tv_target);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.sales.setText(holder.adapterItem.getSales());
        holder.omzet.setText(Index.globalfunction.delimeter(holder.adapterItem.getOmzet()));
        holder.target.setText(Index.globalfunction.delimeter(holder.adapterItem.getTarget()));
    }
}
