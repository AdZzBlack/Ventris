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
public class ScheduleTaskListAdapter extends ArrayAdapter<ScheduleTaskAdapter> {

    private List<ScheduleTaskAdapter> items;
    private int layoutResourceId;
    private Context context;

    public ScheduleTaskListAdapter(Context context, int layoutResourceId, List<ScheduleTaskAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        ScheduleTaskAdapter adapterScheduleTask;
        TextView nomor;
        TextView managernomor;
        TextView tipe;
        TextView tanggal;
        TextView jam;
        TextView namacustomer;
        TextView nama;
        TextView jenisjadwal;
        TextView keterangan;
        TextView jabatan;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterScheduleTask = items.get(position);

        holder.nomor = (TextView)row.findViewById(R.id.tv_nomor);
        holder.managernomor = (TextView)row.findViewById(R.id.tv_managernomor);
        holder.tipe = (TextView)row.findViewById(R.id.tv_tipe);
        holder.tanggal = (TextView)row.findViewById(R.id.tv_tanggal);
        holder.jam = (TextView)row.findViewById(R.id.tv_jam);
        holder.namacustomer = (TextView)row.findViewById(R.id.tv_namacustomer);
        holder.nama = (TextView)row.findViewById(R.id.tv_nama);
        holder.jenisjadwal = (TextView)row.findViewById(R.id.tv_jenisjadwal);
        holder.keterangan = (TextView)row.findViewById(R.id.tv_keterangan);
        holder.jabatan = (TextView)row.findViewById(R.id.tv_jabatan);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        holder.nomor.setText(holder.adapterScheduleTask.getNomor());
        holder.managernomor.setText(holder.adapterScheduleTask.getManagernomor());
        holder.tipe.setText(holder.adapterScheduleTask.getTipe());
        holder.tanggal.setText(holder.adapterScheduleTask.getTanggal());
        holder.jam.setText(holder.adapterScheduleTask.getJam());
        holder.namacustomer.setText(holder.adapterScheduleTask.getNamacustomer());
        holder.nama.setText(holder.adapterScheduleTask.getNama());
        holder.jenisjadwal.setText(holder.adapterScheduleTask.getJenisJadwal());
        holder.keterangan.setText(holder.adapterScheduleTask.getKeterangan());

        if(Integer.parseInt(Index.user_nomor) == Integer.parseInt(holder.adapterScheduleTask.getManagernomor())) {
            if(Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")){
                holder.jabatan.setText("Pembuat : ");
            }
            else{
                holder.jabatan.setText("BEX     : ");
            }
        }else{
            if(Index.user_jabatan.equals("SALES") || Index.user_jabatan.equals("SALES ADMIN")){
                holder.jabatan.setText("Manager : ");
            }
            else{
                holder.jabatan.setText("BEX     : ");
            }
        }
    }
}
