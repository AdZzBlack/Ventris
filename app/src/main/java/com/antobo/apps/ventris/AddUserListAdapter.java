package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class AddUserListAdapter extends ArrayAdapter<AddUserAdapter> {

    private List<AddUserAdapter> items;
    private int layoutResourceId;
    private Context context;

    public AddUserListAdapter(Context context, int layoutResourceId, List<AddUserAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        AddUserAdapter adapterAddUser;
        CheckBox centang;
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
        holder.adapterAddUser = items.get(position);

        holder.centang = (CheckBox)row.findViewById(R.id.cb_user);
        setCheckboxChangeListener(holder);
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
        holder.centang.setChecked(holder.adapterAddUser.getCentang());
        holder.nomor.setText(holder.adapterAddUser.getNomor());
        holder.nama.setText(holder.adapterAddUser.getNama());
        if(holder.adapterAddUser.getJabatan().equals("MANAGER")){
            holder.jabatan.setImageResource(R.drawable.manager);
        }
        else if(holder.adapterAddUser.getJabatan().equals("OWNER")){
            holder.jabatan.setImageResource(R.drawable.owner);
        }
        else if(holder.adapterAddUser.getJabatan().equals("SALES")){
            holder.jabatan.setImageResource(R.drawable.bex);
        }
        holder.hp.setText(holder.adapterAddUser.getHp());
        holder.lokasi.setText(holder.adapterAddUser.getLokasi());
        holder.latitude.setText(holder.adapterAddUser.getLatitude());
        holder.longitude.setText(holder.adapterAddUser.getLongitude());
    }

    private void setCheckboxChangeListener(final Holder holder) {
        holder.centang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.centang.isChecked()){
                    holder.adapterAddUser.setCentang(true);
                }else{
                    holder.adapterAddUser.setCentang(false);
                }
            }
        });
    }
}
