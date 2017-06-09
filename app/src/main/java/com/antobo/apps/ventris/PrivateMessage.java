package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrivateMessage extends Fragment implements View.OnClickListener {

    private ListView lv_chat;
    private EditText et_newmessage;
    private Button btn_send;

    private PrivateMessageListAdapter messageadapter;

    private int counter = 0;

    private boolean isLoading = true;

    Thread myThread = null;

//    private boolean checkStart = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.privatemessage, container, false);
        getActivity().setTitle("Message to " + Index.globalfunction.private_user_nama_tujuan);

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_newmessage = (EditText) v.findViewById(R.id.et_newmessage);
        btn_send = (Button) v.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        messageadapter = new PrivateMessageListAdapter(getActivity(), R.layout.list_privatemessage, new ArrayList<PrivateMessageAdapter>());
        lv_chat = (ListView) v.findViewById(R.id.lv_chat);
        lv_chat.setAdapter(messageadapter);
        lv_chat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                String actionUrl = "Message/readPrivateMessage/";
                new readPrivateMessage().execute(actionUrl);
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Scroll Down
                if(!isLoading){
                    if(listIsAtTop()){
                        counter += 1;
                        refresh_chat();
                    }
                }

            }
        });
        lv_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                v.startAnimation(Index.listeffect);

                TextView tv_message = (TextView) v.findViewById(R.id.tv_message);

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Private Message", tv_message.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        //-----END DECLARE---------------------------------------------------------------------------------------

        counter = 0;
        refresh_chat();

        Runnable runnable = new checkMsg();
        myThread= new Thread(runnable);
        myThread.start();

        return v;
    }

    private boolean listIsAtTop()   {
        if(lv_chat.getChildCount() == 0) return true;
        return lv_chat.getChildAt(0).getTop() == 0;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if (v.getId() == R.id.btn_send) {
            if (et_newmessage.getText().toString().trim().length() > 0) {
                String actionUrl = "Message/sendPrivateMessage/";
                new sendMessage().execute(actionUrl);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());
                String nama = "You";

                messageadapter.add(new PrivateMessageAdapter(nama, et_newmessage.getText().toString(), date, "1"));
                messageadapter.notifyDataSetChanged();
                lv_chat.smoothScrollToPosition(messageadapter.getCount() -1);

                et_newmessage.setText("");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class sendMessage extends AsyncTask<String, Void, String> {
        String user_nomor_asal = Index.user_nomor;
        String user_nomor_tujuan = Index.globalfunction.private_user_nomor_tujuan;
        String new_message = et_newmessage.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor_asal", user_nomor_asal);
                Index.jsonObject.put("user_nomor_tujuan", user_nomor_tujuan);
                Index.jsonObject.put("new_message", new_message);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0],Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resultt", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    String success = obj.getString("success");
                    if(success.equals("true")){
                        lv_chat.smoothScrollToPosition(messageadapter.getCount() - 1);
                    }else{
//                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Failed send message", Toast.LENGTH_LONG).show();
                    }
                }
                isLoading = false;
            }catch(Exception e)
            {
                e.printStackTrace();
//                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Failed send message", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }
    }

    public void refresh_chat(){
        String actionUrl = "Message/getPrivateMessage/";
        new getMessage().execute( actionUrl );
    }

    private class getMessage extends AsyncTask<String, Void, String> {
        String user_nomor_asal = ((Index)getActivity()).user_nomor;
        String user_nomor_tujuan = Index.globalfunction.private_user_nomor_tujuan;

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("limit", counter);
                Index.jsonObject.put("user_nomor_asal", user_nomor_asal);
                Index.jsonObject.put("user_nomor_tujuan", user_nomor_tujuan);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0],Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(counter == 0){
                fillMessageStart(result);

//                if(!runCheck)
//                {
//                    runCheck = true;
//                    Runnable runnable = new checkMsg();
//                    myThread= new Thread(runnable);
//                    myThread.start();
//                }
            }else{
                fillMessageNext(result);
            }

            Index.pDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            Index.pDialog = new ProgressDialog(getActivity());
            Index.pDialog.setMessage("Loading...");
            Index.pDialog.setCancelable(true);
            Index.pDialog.show();
        }
    }

    public void fillMessageStart(String result){
        try {
            JSONArray jsonarray = new JSONArray(result);
            if(jsonarray.length() > 0){
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    if(!obj.has("query")){
                        String userfrom = (obj.getString("intNomorMUser_from"));
                        String namafrom = (obj.getString("vcNama_from"));
                        String namato = (obj.getString("vcNama_from"));
                        String message = (obj.getString("txtMessage"));
                        String date = (obj.getString("dtInsertTime"));
                        String status = (obj.getString("intStatus"));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date newdate = sdf.parse(date);
                        date = sdf.format(newdate);

                        if (Index.user_nomor.equals(userfrom)) {
                            namafrom = "You";
                            messageadapter.add(new PrivateMessageAdapter(namafrom, message, date, status));
                            messageadapter.notifyDataSetChanged();
                        } else {
                            status = "1";
                            messageadapter.add(new PrivateMessageAdapter(namato, message, date, status));
                            messageadapter.notifyDataSetChanged();
                        }

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        isLoading = false;
                                    }
                                },
                                1000);

                        lv_chat.setSelection(messageadapter.getCount() - (counter * 10) - 1);
                    }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed load message", Toast.LENGTH_LONG).show();
            Index.pDialog.dismiss();
        }
    }

    public void fillMessageNext(String result){
        try {
            JSONArray jsonarray = new JSONArray(result);
            if(jsonarray.length() > 0){
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    if(!obj.has("query")){
                        String userfrom = (obj.getString("intNomorMUser_from"));
                        String namafrom = (obj.getString("vcNama_from"));
                        String namato = (obj.getString("vcNama_from"));
                        String message = (obj.getString("txtMessage"));
                        String date = (obj.getString("dtInsertTime"));
                        String status = (obj.getString("intStatus"));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date newdate = sdf.parse(date);
                        date = sdf.format(newdate);

                        if (Index.user_nomor.equals(userfrom)) {
                            namafrom = "You";
                            messageadapter.insert(new PrivateMessageAdapter(namafrom, message, date, status), 0);
                            messageadapter.notifyDataSetChanged();
                        } else {
                            status = "1";
                            messageadapter.insert(new PrivateMessageAdapter(namato, message, date, status), 0);
                            messageadapter.notifyDataSetChanged();
                        }

//                        lv_chat.setSelection(10);
                        lv_chat.setSelection(messageadapter.getCount() - (counter * 10));

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() { isLoading = false; }
                                },
                                1000);
                    }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed load message", Toast.LENGTH_LONG).show();
            Index.pDialog.dismiss();
        }
    }

    private class readPrivateMessage extends AsyncTask<String, Void, String> {
        String user_nomor_asal = Index.user_nomor;
        String user_nomor_tujuan = Index.globalfunction.private_user_nomor_tujuan;

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("user_nomor_asal", user_nomor_asal);
                Index.jsonObject.put("user_nomor_tujuan", user_nomor_tujuan);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0],Index.jsonObject);
        }
    }

    private class checkNewMessage extends AsyncTask<String, Void, String> {
        String user_nomor_asal = ((Index)getActivity()).user_nomor;
        String user_nomor_tujuan = Index.globalfunction.private_user_nomor_tujuan;

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("limit", 0);
                Index.jsonObject.put("user_nomor_asal", user_nomor_asal);
                Index.jsonObject.put("user_nomor_tujuan", user_nomor_tujuan);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0],Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            String userfrom = (obj.getString("intNomorMUser_from"));
                            String namafrom = (obj.getString("vcNama_from"));
                            String namato = (obj.getString("vcNama_from"));
                            String message = (obj.getString("txtMessage"));
                            String date = (obj.getString("dtInsertTime"));
                            String status = (obj.getString("intStatus"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date newdate = sdf.parse(date);
                            date = sdf.format(newdate);

                            if (Index.user_nomor.equals(userfrom)) {
                                namafrom = "You";
                            } else {
//                                if(status.equals("1")){
                                    for (int j = 0; j < messageadapter.getCount(); j++) {
                                        PrivateMessageAdapter adapter = messageadapter.getItem(j);
                                        String tempnama = adapter.getNama().toString();
                                        String tempdate = adapter.getTime().toString();
                                        String namaanddate = tempnama + "+" + tempdate;
                                        if(adapter.getNama().toString().equals("You")){
                                            adapter.setStatus("2");
                                            messageadapter.notifyDataSetChanged();
                                        }
                                    }

                                    Boolean kembar = false;
                                    for (int j = 0; j < messageadapter.getCount(); j++) {
                                        PrivateMessageAdapter adapter = messageadapter.getItem(j);
                                        String tempnama = adapter.getNama().toString();
                                        String tempdate = adapter.getTime().toString();
                                        String namaanddate = tempnama + "+" + tempdate;
                                        if(namaanddate.equals(namato + "+" + date)){
                                            kembar = true;
                                            break;
                                        }
                                    }
                                    if(!kembar){
                                        messageadapter.add(new PrivateMessageAdapter(namato, message, date, status));
                                        messageadapter.notifyDataSetChanged();
                                        lv_chat.smoothScrollToPosition(messageadapter.getCount() -1);
                                    }
                                    String actionUrlread = "Message/readPrivateMessage/";
                                    new readPrivateMessage().execute(actionUrlread);
//                                }
                            }
                        }
                    }
                }

//                new android.os.Handler().postDelayed(
//                        new Runnable() {
//                            public void run() {
//                                String actionUrlget = "Message/getPrivateMessage/";
//                                new checkNewMessage().execute( actionUrlget );
//                            }
//                        },
//                        1000);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            isLoading = false;
            Index.globalfunction.private_run_check++;
        }
    }

    class checkMsg implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    if(Index.globalfunction.private_run_check < 5 && !isLoading)
                    {
                        isLoading = true;
                        String actionUrlget = "Message/getPrivateMessage/";
                        new checkNewMessage().execute( actionUrlget );
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
}