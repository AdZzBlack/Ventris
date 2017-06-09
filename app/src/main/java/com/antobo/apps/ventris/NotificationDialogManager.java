package com.antobo.apps.ventris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by antonnw on 12/07/2016.
 */
public class NotificationDialogManager {

    public void showDialog(final Context context, final String title, String question, int image) {
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
        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertdialog.create().show();
    }

}
