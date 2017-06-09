package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Password extends Fragment implements View.OnClickListener {

    private EditText et_oldpassword, et_newpassword, et_confirmation;
    private Button btn_change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.password, container, false);
        getActivity().setTitle("Change Password");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_newpassword = (EditText) v.findViewById(R.id.et_newpassword);
        et_oldpassword = (EditText) v.findViewById(R.id.et_oldpassword);
        et_confirmation = (EditText) v.findViewById(R.id.et_confirmation);

        btn_change = (Button) v.findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);

        //-----END DECLARE---------------------------------------------------------------------------------------

        return v;
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_change){
            String newpass = et_newpassword.getText().toString();
            String confirmation = et_confirmation.getText().toString();
            if(newpass.equals(confirmation))
            {
                String actionUrl = "Login/updatePassword/";
                new changePassword().execute( actionUrl );
            }
            else
            {
                Toast.makeText(getContext(), "Confirmation Password not equal", Toast.LENGTH_LONG).show();
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


    private class changePassword extends AsyncTask<String, Void, String> {
        String oldpass = et_oldpassword.getText().toString();
        String newpass = et_newpassword.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("old_password", oldpass);
                Index.jsonObject.put("new_password", newpass);
                Index.jsonObject.put("user_nomor", Index.user_nomor);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = jsonarray.length() - 1; i >= 0; i--) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    String success = obj.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_LONG).show();

                        Fragment fragment = new Dashboard();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.commit();
                    }
                    else if(success.equals("2")){
                        Toast.makeText(getContext(), "Change Password Failed", Toast.LENGTH_LONG).show();
                    }
                    else if(success.equals("3")){
                        Toast.makeText(getContext(), "Wrong Old Password", Toast.LENGTH_LONG).show();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                Index.pDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Index.pDialog = new ProgressDialog(getActivity());
            Index.pDialog.setMessage("Loading...");
            Index.pDialog.setCancelable(true);
            Index.pDialog.show();
        }
    }
}