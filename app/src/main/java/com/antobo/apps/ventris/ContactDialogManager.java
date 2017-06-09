package com.antobo.apps.ventris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by antonnw on 23/05/2016.
 */
public class ContactDialogManager {

    public void showDialog(final Context context, final String title, String question, int image, final String phoneNumber) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogview = inflater.inflate(R.layout.intent_dialog, null);

        TextView tv_question = (TextView) dialogview.findViewById(R.id.tv_question);
        tv_question.setText(question);
        ImageView iv_dialog = (ImageView) dialogview.findViewById(R.id.iv_dialog);
        iv_dialog.setImageResource(image);

        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
        alertdialog.setTitle(title);
        alertdialog.setCancelable(true);
        alertdialog.setView(dialogview);
        alertdialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                context.startActivity(i);
                dialog.cancel();
            }
        });
        alertdialog.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null));
                context.startActivity(i);
                dialog.cancel();
            }
        });
        alertdialog.create().show();
    }
}
