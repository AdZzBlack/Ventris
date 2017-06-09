package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class PriceListAdapter extends ArrayAdapter<PriceAdapter> {

    private List<PriceAdapter> items;
    private int layoutResourceId;
    private Context context;

    public PriceListAdapter(Context context, int layoutResourceId, List<PriceAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        PriceAdapter adapterItem;
        TextView kode;
        TextView namaJual;
        TextView luas;
        TextView idr1;
        TextView idr2;
        TextView idr3;
        TextView usd1;
        TextView usd2;
        TextView usd3;
        TextView rmb1;
        TextView rmb2;
        TextView rmb3;
        TextView satuan11;
        TextView satuan12;
        TextView satuan13;
        TextView satuan21;
        TextView satuan22;
        TextView satuan23;
        TextView satuan31;
        TextView satuan32;
        TextView satuan33;
        TextView konversi2;

        TableRow harga11;
        TableRow harga12;
        TableRow harga13;
        TableRow harga21;
        TableRow harga22;
        TableRow harga23;
        TableRow harga31;
        TableRow harga32;
        TableRow harga33;
        TableRow idr;
        TableRow usd;
        TableRow rmb;

        int checkIDR;
        int checkUSD;
        int checkRMB;
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
        holder.namaJual = (TextView)row.findViewById(R.id.tv_namajual);
        holder.luas = (TextView)row.findViewById(R.id.tv_luas);
        holder.idr1 = (TextView)row.findViewById(R.id.tv_idr1);
        holder.idr2 = (TextView)row.findViewById(R.id.tv_idr2);
        holder.idr3 = (TextView)row.findViewById(R.id.tv_idr3);
        holder.usd1 = (TextView)row.findViewById(R.id.tv_usd1);
        holder.usd2 = (TextView)row.findViewById(R.id.tv_usd2);
        holder.usd3 = (TextView)row.findViewById(R.id.tv_usd3);
        holder.rmb1 = (TextView)row.findViewById(R.id.tv_rmb1);
        holder.rmb2 = (TextView)row.findViewById(R.id.tv_rmb2);
        holder.rmb3 = (TextView)row.findViewById(R.id.tv_rmb3);
        holder.satuan11 = (TextView)row.findViewById(R.id.tv_satuan11);
        holder.satuan12 = (TextView)row.findViewById(R.id.tv_satuan12);
        holder.satuan13 = (TextView)row.findViewById(R.id.tv_satuan13);
        holder.satuan21 = (TextView)row.findViewById(R.id.tv_satuan21);
        holder.satuan22 = (TextView)row.findViewById(R.id.tv_satuan22);
        holder.satuan23 = (TextView)row.findViewById(R.id.tv_satuan23);
        holder.satuan31 = (TextView)row.findViewById(R.id.tv_satuan31);
        holder.satuan32 = (TextView)row.findViewById(R.id.tv_satuan32);
        holder.satuan33 = (TextView)row.findViewById(R.id.tv_satuan33);
        holder.konversi2 = (TextView)row.findViewById(R.id.tv_konversi2);

        holder.harga11 = (TableRow)row.findViewById(R.id.tr_harga11);
        holder.harga12 = (TableRow)row.findViewById(R.id.tr_harga12);
        holder.harga13 = (TableRow)row.findViewById(R.id.tr_harga13);
        holder.harga21 = (TableRow)row.findViewById(R.id.tr_harga21);
        holder.harga22 = (TableRow)row.findViewById(R.id.tr_harga22);
        holder.harga23 = (TableRow)row.findViewById(R.id.tr_harga23);
        holder.harga31 = (TableRow)row.findViewById(R.id.tr_harga31);
        holder.harga32 = (TableRow)row.findViewById(R.id.tr_harga32);
        holder.harga33 = (TableRow)row.findViewById(R.id.tr_harga33);
        holder.idr = (TableRow)row.findViewById(R.id.tr_idr);
        holder.usd = (TableRow)row.findViewById(R.id.tr_usd);
        holder.rmb = (TableRow)row.findViewById(R.id.tr_rmb);

        holder.checkIDR = 0;
        holder.checkUSD = 0;
        holder.checkRMB = 0;

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.kode.setText(holder.adapterItem.getKode());
        holder.namaJual.setText(holder.adapterItem.getNamaJual());

        DecimalFormat format=new DecimalFormat("#,###.##");

        Double Raw = Double.parseDouble(holder.adapterItem.getLuas());
        String luas = String.valueOf(format.format(Raw));

        holder.luas.setText(luas);


        Raw = Double.parseDouble(holder.adapterItem.getIdr1());
        String idr1 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getIdr2());
        String idr2 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getIdr3());
        String idr3 = String.valueOf(format.format(Raw));

        Raw = Double.parseDouble(holder.adapterItem.getUsd1());
        String usd1 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getUsd2());
        String usd2 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getUsd3());
        String usd3 = String.valueOf(format.format(Raw));

        Raw = Double.parseDouble(holder.adapterItem.getRmb1());
        String rmb1 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getRmb2());
        String rmb2 = String.valueOf(format.format(Raw));
        Raw = Double.parseDouble(holder.adapterItem.getRmb3());
        String rmb3 = String.valueOf(format.format(Raw));

        holder.idr1.setText(idr1);
        holder.idr2.setText(idr2);
        holder.idr3.setText(idr3);
        holder.usd1.setText(usd1);
        holder.usd2.setText(usd2);
        holder.usd3.setText(usd3);
        holder.rmb1.setText(rmb1);
        holder.rmb2.setText(rmb2);
        holder.rmb3.setText(rmb3);
        holder.satuan11.setText(holder.adapterItem.getSatuan1());
        holder.satuan12.setText(holder.adapterItem.getSatuan2());
        holder.satuan13.setText(holder.adapterItem.getSatuan3());
        holder.satuan21.setText(holder.adapterItem.getSatuan1());
        holder.satuan22.setText(holder.adapterItem.getSatuan2());
        holder.satuan23.setText(holder.adapterItem.getSatuan3());
        holder.satuan31.setText(holder.adapterItem.getSatuan1());
        holder.satuan32.setText(holder.adapterItem.getSatuan2());
        holder.satuan33.setText(holder.adapterItem.getSatuan3());

        holder.konversi2.setText(holder.adapterItem.getSatuan1() + ":" + GlobalFunction.delimeter(holder.adapterItem.getKonversi1()) + ", " + holder.adapterItem.getSatuan2() + ":" + GlobalFunction.delimeter(holder.adapterItem.getKonversi2()) + ", " + holder.adapterItem.getSatuan3() + ":" + GlobalFunction.delimeter(holder.adapterItem.getKonversi3()));
//        holder.konversi2.setText(holder.adapterItem.getSatuan1() + ":" + holder.adapterItem.getKonversi1() + ", " + holder.adapterItem.getSatuan2() + ":" + holder.adapterItem.getKonversi2() + ", " + holder.adapterItem.getSatuan3() + ":" + holder.adapterItem.getKonversi3());


        if(holder.adapterItem.getIdr1().equals("0.000000"))
        {
            holder.idr1.setVisibility(View.INVISIBLE);
            holder.satuan11.setVisibility(View.INVISIBLE);
            holder.harga11.setVisibility(View.INVISIBLE);
            holder.harga11.removeAllViews();
            holder.checkIDR++;
        }

        if(holder.adapterItem.getIdr2().equals("0.000000"))
        {
            holder.idr2.setVisibility(View.INVISIBLE);
            holder.satuan12.setVisibility(View.INVISIBLE);
            holder.harga12.setVisibility(View.INVISIBLE);
            holder.harga12.removeAllViews();
            holder.checkIDR++;
        }

        if(holder.adapterItem.getIdr3().equals("0.000000"))
        {
            holder.idr3.setVisibility(View.INVISIBLE);
            holder.satuan13.setVisibility(View.INVISIBLE);
            holder.harga13.setVisibility(View.INVISIBLE);
            holder.harga13.removeAllViews();
            holder.checkIDR++;
        }

        if(holder.adapterItem.getUsd1().equals("0.000000"))
        {
            holder.usd1.setVisibility(View.INVISIBLE);
            holder.satuan21.setVisibility(View.INVISIBLE);
            holder.harga21.setVisibility(View.INVISIBLE);
            holder.harga21.removeAllViews();
            holder.checkUSD++;
        }

        if(holder.adapterItem.getUsd2().equals("0.000000"))
        {
            holder.usd2.setVisibility(View.INVISIBLE);
            holder.satuan22.setVisibility(View.INVISIBLE);
            holder.harga22.setVisibility(View.INVISIBLE);
            holder.harga22.removeAllViews();
            holder.checkUSD++;
        }

        if(holder.adapterItem.getUsd3().equals("0.000000"))
        {
            holder.usd3.setVisibility(View.INVISIBLE);
            holder.satuan23.setVisibility(View.INVISIBLE);
            holder.harga23.setVisibility(View.INVISIBLE);
            holder.harga23.removeAllViews();
            holder.checkUSD++;
        }

        if(holder.adapterItem.getRmb1().equals("0.000000"))
        {
            holder.rmb1.setVisibility(View.INVISIBLE);
            holder.satuan31.setVisibility(View.INVISIBLE);
            holder.harga31.setVisibility(View.INVISIBLE);
            holder.harga31.removeAllViews();
            holder.checkRMB++;
        }

        if(holder.adapterItem.getRmb2().equals("0.000000"))
        {
            holder.rmb2.setVisibility(View.INVISIBLE);
            holder.satuan32.setVisibility(View.INVISIBLE);
            holder.harga32.setVisibility(View.INVISIBLE);
            holder.harga32.removeAllViews();
            holder.checkRMB++;
        }

        if(holder.adapterItem.getRmb3().equals("0.000000"))
        {
            holder.rmb3.setVisibility(View.INVISIBLE);
            holder.satuan33.setVisibility(View.INVISIBLE);
            holder.harga33.setVisibility(View.INVISIBLE);
            holder.harga33.removeAllViews();
            holder.checkRMB++;
        }

        if(holder.checkIDR==3)
        {
            holder.idr.removeAllViews();
        }
        if(holder.checkUSD==3)
        {
            holder.usd.removeAllViews();
        }
        if(holder.checkRMB==3)
        {
            holder.rmb.removeAllViews();
        }
    }
}
