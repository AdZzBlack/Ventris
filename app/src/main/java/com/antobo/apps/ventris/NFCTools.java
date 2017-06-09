package com.antobo.apps.ventris;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

public class NFCTools extends Fragment implements View.OnClickListener {

    private TextView tv_data;
    private EditText et_data;
    private Button btn_writetag, btn_readonly;

    private NfcManager manager;
    private NfcAdapter adapter;

    private boolean nfc_write, nfc_read_only = false;

    private static final int PENDING_INTENT_TECH_DISCOVERED = 1;
    private static final int DIALOG_WRITE_TEXT = 1;
    private static final int DIALOG_READ_ONLY = 2;

    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nfctools, container, false);
        getActivity().setTitle("NFC Tools");

        //---------------------------------------------------------

        tv_data = (TextView) v.findViewById(R.id.tv_data);
        et_data = (EditText) v.findViewById(R.id.et_data);
        btn_writetag = (Button) v.findViewById(R.id.btn_writetag);
        btn_readonly = (Button) v.findViewById(R.id.btn_readonly);

        btn_writetag.setOnClickListener(this);
        btn_readonly.setOnClickListener(this);

        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
            Toast.makeText(getActivity(), "Your device does not support NFC", Toast.LENGTH_SHORT).show();

            Fragment fragment = new Dashboard();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else{
            manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
            adapter = manager.getDefaultAdapter();
            if (adapter != null && adapter.isEnabled()) {}
            else{
                Intent i = new Intent(Settings.ACTION_NFC_SETTINGS);
                Index.intentalert.showDialog(getActivity(), "NFC Service", "Aktifkan NFC?", R.drawable.nfctools_dark, i);
            }
        }

        //---------------------------------------------------------

        return v;
    }


    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if (v.getId() == R.id.btn_writetag) {
            nfc_write = true;
            showDialog(DIALOG_WRITE_TEXT);
        } else if (v.getId() == R.id.btn_readonly) {
            nfc_read_only = true;
            showDialog(DIALOG_READ_ONLY);
        }
    }

    public void showDialog(int dialogId){

        Dialog d = mDialogs.get(dialogId);
        if (d == null){

            d = onCreateDialog(dialogId);
            mDialogs.put(dialogId, d);
        }
        if (d != null){
            onPrepareDialog(d, dialogId);
            d.show();
        }

    }

    public void dismissDialog(int dialogId){

        Dialog d = mDialogs.get(dialogId);
        if (d == null){

            d = onCreateDialog(dialogId);
            mDialogs.put(dialogId, d);
        }
        if (d != null){
            onPrepareDialog(d, dialogId);
            d.dismiss();
        }
    }

    public Dialog onCreateDialog(int dialogId){
        //just create your Dialog here, once
        switch (dialogId) {
            case DIALOG_WRITE_TEXT :
                return new AlertDialog.Builder(getActivity())
                        .setTitle("Write to Tag")
                        .setMessage("Approach an NFC Tag")
                        .setCancelable(true)
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int arg) {
                                d.cancel();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface d) {
                                nfc_write = false;
                            }
                        }).create();
            case DIALOG_READ_ONLY :
                return new AlertDialog.Builder(getActivity())
                        .setTitle("Make Tag Read Only")
                        .setMessage("Approach an NFC Tag")
                        .setCancelable(true)
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int arg) {
                                d.cancel();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface d) {
                                nfc_read_only = false;
                            }
                        }).create();
        }
        return null;
    }

    public void onPrepareDialog(Dialog d, int dialogId){
        // change something inside already created Dialogs here
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        PendingIntent pi = getActivity().createPendingResult(PENDING_INTENT_TECH_DISCOVERED, new Intent(), 0);

        adapter.enableForegroundDispatch(
                getActivity(), pi, new IntentFilter[]{ new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) },
                new String[][]{ new String[]{ "android.nfc.tech.NdefFormatable" },
                        new String[]{ "android.nfc.tech.Ndef" }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        adapter.disableForegroundDispatch(getActivity());
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PENDING_INTENT_TECH_DISCOVERED :
                resolveIntent(data, true);
                break;
        }
    }

    private void resolveIntent(Intent data, boolean foregroundDispatch) {
        String action = data.getAction();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Tag tag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (foregroundDispatch && nfc_write) {
                // write tag
                String kode = et_data.getText().toString();

                byte[] bytes = kode.getBytes(Charset.forName("UTF-8"));
                byte[] payload = new byte[bytes.length + 1];
                payload[0] = 0;
                System.arraycopy(bytes, 0, payload, 1, bytes.length);

                NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                        NdefRecord.RTD_TEXT,
                        new byte[0],
                        payload);

                NdefMessage msg = new NdefMessage(new NdefRecord[]{ textRecord} );

                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag != null) {
                    try {
                        ndefTag.connect();
                        ndefTag.writeNdefMessage(msg);
                    } catch (Exception e){ }
                    finally {
                        try {
                            ndefTag.close();
                        } catch (Exception e) { }
                    }
                }
                else{
                    NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
                    if (ndefFormatableTag != null) {
                        try {
                            ndefFormatableTag.connect();
                            ndefFormatableTag.format(msg);
                        } catch (Exception e) { }
                        finally {
                            try {
                                ndefFormatableTag.close();
                            } catch (Exception e) { }
                        }
                    }
                }

                nfc_write = false;

                dismissDialog(DIALOG_WRITE_TEXT);

                et_data.setText("");

                Index.notifalert.showDialog(getActivity(), "Congratulation", "Your tag has been written", R.drawable.success);
            }
            else if (foregroundDispatch && nfc_read_only) {
                // make tag read only
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag != null) {
                    try {
                        ndefTag.connect();
                        ndefTag.makeReadOnly();
                    } catch (Exception e){ }
                    finally {
                        try {
                            ndefTag.close();
                        } catch (Exception e) { }
                    }
                }

                nfc_read_only = false;

                dismissDialog(DIALOG_READ_ONLY);

                Index.notifalert.showDialog(getActivity(), "Congratulation", "Your tag is now read only", R.drawable.success);
            }
            else {
                Parcelable[] ndefRaw = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage[] ndefMsgs = null;

                if (ndefRaw != null) {
                    ndefMsgs = new NdefMessage[ndefRaw.length];
                }

                for (int i = 0; i < ndefMsgs.length; ++i) {
                    ndefMsgs[i] = (NdefMessage) ndefRaw[i];
                }

                if (ndefMsgs != null) {
                    for (int i = 0; i < ndefMsgs.length; ++i) {
                        NdefRecord[] records = ndefMsgs[i].getRecords();
                        if (records != null) {
                            for (int j = 0; j < records.length; ++j) {
                                if ((records[j].getTnf() == NdefRecord.TNF_WELL_KNOWN) && Arrays.equals(records[j].getType(), NdefRecord.RTD_TEXT)) {
                                    byte[] payload = records[j].getPayload();
                                    String kode = new String(Arrays.copyOfRange(payload, 1, payload.length), Charset.forName("UTF-8"));
                                    tv_data.setText(kode);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}