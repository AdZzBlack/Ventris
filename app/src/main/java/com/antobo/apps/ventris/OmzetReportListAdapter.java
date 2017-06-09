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
public class OmzetReportListAdapter extends ArrayAdapter<OmzetReportAdapter> {

    private List<OmzetReportAdapter> items;
    private int layoutResourceId;
    private Context context;

    public OmzetReportListAdapter(Context context, int layoutResourceId, List<OmzetReportAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        OmzetReportAdapter adapterItem;
        TextView customer;
        TextView dpp;
        TextView brand;
        TextView area;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterItem = items.get(position);

        holder.customer = (TextView)row.findViewById(R.id.tv_customer);
        holder.dpp = (TextView)row.findViewById(R.id.tv_dpp);
        holder.brand = (TextView)row.findViewById(R.id.tv_brand);
        holder.area = (TextView)row.findViewById(R.id.tv_area);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.customer.setText(holder.adapterItem.getCustomer());
        holder.dpp.setText(Index.globalfunction.delimeter(holder.adapterItem.getDpp()));
        holder.brand.setText(holder.adapterItem.getBrand());
        holder.area.setText(holder.adapterItem.getArea());
    }
}
