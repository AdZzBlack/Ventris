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
public class UserListAdapter extends ArrayAdapter<UserAdapter> {

    private List<UserAdapter> items;
    private int layoutResourceId;
    private Context context;

    public UserListAdapter(Context context, int layoutResourceId, List<UserAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        UserAdapter adapterUser;
        TextView nomor;
        TextView nama;
        ImageView jabatan;
        TextView hp;
        TextView lokasi;
        TextView latitude;
        TextView longitude;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterUser = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.nama = (TextView)row.findViewById(R.id.tv_nama);
        holder.jabatan = (ImageView) row.findViewById(R.id.iv_jabatan);
        holder.hp = (TextView)row.findViewById(R.id.tv_hp);
        holder.lokasi = (TextView)row.findViewById(R.id.tv_location);
        holder.latitude = (TextView)row.findViewById(R.id.tv_latitude);
        holder.longitude = (TextView)row.findViewById(R.id.tv_longitude);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.nomor.setText(holder.adapterUser.getNomor());
        holder.nama.setText(holder.adapterUser.getNama());
        if(holder.adapterUser.getJabatan().equals("MANAGER")){
            holder.jabatan.setImageResource(R.drawable.manager);
        }
        else if(holder.adapterUser.getJabatan().equals("OWNER")){
            holder.jabatan.setImageResource(R.drawable.owner);
        }
        else if(holder.adapterUser.getJabatan().equals("SALES")){
            holder.jabatan.setImageResource(R.drawable.bex);
        }
        else if(holder.adapterUser.getJabatan().equals("SALES ADMIN")){
            holder.jabatan.setImageResource(R.drawable.admin);
        }
        holder.hp.setText(holder.adapterUser.getHp());
        holder.lokasi.setText(holder.adapterUser.getLokasi());
        holder.latitude.setText(holder.adapterUser.getLatitude());
        holder.longitude.setText(holder.adapterUser.getLongitude());
    }
}
