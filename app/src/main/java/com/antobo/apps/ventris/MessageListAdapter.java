package com.antobo.apps.ventris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by antonnw on 22/06/2016.
 */
public class MessageListAdapter extends ArrayAdapter<MessageAdapter> {

    private List<MessageAdapter> items;
    private int layoutResourceId;
    private Context context;

    private RelativeLayout.LayoutParams paramtime, paramnama, parammessage;

    public MessageListAdapter(Context context, int layoutResourceId, List<MessageAdapter> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class Holder {
        MessageAdapter adapterMessage;
        TextView nama;
        TextView message;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new Holder();
        holder.adapterMessage = items.get(position);

        holder.nama = (TextView)row.findViewById(R.id.tv_nama);
        holder.message = (TextView)row.findViewById(R.id.tv_message);
        holder.time = (TextView)row.findViewById(R.id.tv_time);

        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        if(holder.adapterMessage.getNama().equals("You")){
            paramnama = (RelativeLayout.LayoutParams) holder.nama.getLayoutParams();
            paramnama.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramnama.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.nama.setLayoutParams(paramnama);

            paramtime = (RelativeLayout.LayoutParams) holder.time.getLayoutParams();
            paramtime.removeRule(RelativeLayout.RIGHT_OF);
            paramtime.addRule(RelativeLayout.LEFT_OF, holder.nama.getId());
            holder.time.setLayoutParams(paramtime);

            parammessage = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
            parammessage.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            parammessage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.message.setBackgroundColor(Color.parseColor("#DCF8C7"));
            holder.message.setLayoutParams(parammessage);

        }

        // Change to today format
        String date = holder.adapterMessage.getTime();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newdate = sdf.parse(date);
            date = sdf.format(newdate);

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d_today_db = sdf.parse(date);
            String s_today_db = dateformat.format(d_today_db);

            String s_today_system = dateformat.format(new Date());
            Date d_today_system = dateformat.parse(s_today_system);
            s_today_system = dateformat.format(d_today_system);

            if(s_today_db.equals(s_today_system)){
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                Date dt_today_db = sdf.parse(date);
                String st_today_db = timeformat.format(dt_today_db);
                date = st_today_db;
            }
            else{
                SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date dt_today_db = sdf.parse(date);
                String st_today_db = timeformat.format(dt_today_db);
                date = st_today_db;
            }
            holder.time.setText(date);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        holder.nama.setText(holder.adapterMessage.getNama());
        holder.message.setText(holder.adapterMessage.getMessage());
        holder.message.setTextIsSelectable(true);
    }
}
