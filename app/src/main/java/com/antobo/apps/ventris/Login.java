package com.antobo.apps.ventris;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username, et_password;
    private Button btn_login, btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/lato-regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/lato-bold.ttf"))
                .addItalic(Typekit.createFromAsset(this, "fonts/lato-italic.ttf"))
                .addBoldItalic(Typekit.createFromAsset(this, "fonts/lato-bolditalic.ttf"));*/


        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_change = (Button) findViewById(R.id.btn_change);

        Index.sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_jabatan = Index.sharedpreferences.getString("user_jabatan", "");
        if (user_jabatan == "") {
            SharedPreferences.Editor editor = Index.sharedpreferences.edit();
            editor.clear();
            editor.commit();
        }
        else
        {
            et_username.setVisibility(View.INVISIBLE);
            et_password.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.INVISIBLE);
            btn_change.setVisibility(View.INVISIBLE);

            String actionUrl = "Login/checkLogin/";
            new checkLogin().execute( actionUrl );
//            Intent intent = new Intent(Login.this, Index.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//            finish();
        }

        btn_login.setOnClickListener(this);
        btn_change.setOnClickListener(this);
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_login){
            String actionUrl = "Login/loginUser/";
            new loginUser().execute( actionUrl );
        }
        else if(v.getId() == R.id.btn_change){
            SharedPreferences.Editor editor = Index.serversharedpreferences.edit();
            editor.putString( "servernow", "");
            editor.commit();

            Intent i = new Intent(Login.this, SelectServer.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }
    }

    private class loginUser extends AsyncTask<String, Void, String> {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("username", username);
                Index.jsonObject.put("password", password);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("tes", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(!obj.has("query")){
                            SharedPreferences.Editor editor = Index.sharedpreferences.edit();
                            editor.putString( "user_nomor",             obj.getString("user_nomor") );
                            editor.putString( "user_id",                obj.getString("user_id") );
                            editor.putString( "user_nama",              obj.getString("user_nama") );
                            editor.putString( "user_jabatan",           obj.getString("user_jabatan") );
                            editor.putString( "cabang_nomor",           obj.getString("cabang_nomor") );
                            editor.putString( "user_usergroup",         obj.getString("user_usergroup") );
                            editor.putString( "user_password",          obj.getString("user_password") );
                            editor.putString( "user_cansignapproval",   obj.getString("user_cansignapproval") );
                            editor.putString( "cabang_kode",            obj.getString("cabang_kode") );
                            editor.putString( "cabang_nama",            obj.getString("cabang_nama") );
                            editor.putString( "hash",                   obj.getString("hash") );
                            editor.putString( "user_sales",             obj.getString("user_sales") );
                            editor.commit();

                            Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Login.this, Index.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            finish();
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class checkLogin extends AsyncTask<String, Void, String> {
        String user_hash = Index.sharedpreferences.getString("hash", "");

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("hash", user_hash);
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
                if(jsonarray.length() > 0){
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        String success = obj.getString("success");
                        if(success.equals("true")){
                            Intent intent = new Intent(Login.this, Index.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            finish();
                        }
                        else
                        {
                            SharedPreferences.Editor editor = Index.sharedpreferences.edit();
                            editor.clear();
                            editor.commit();

                            et_username.setVisibility(View.VISIBLE);
                            et_password.setVisibility(View.VISIBLE);
                            btn_login.setVisibility(View.VISIBLE);
                            btn_change.setVisibility(View.VISIBLE);
                            Toast.makeText(getBaseContext(), "User has login at another device", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                et_username.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                btn_change.setVisibility(View.VISIBLE);
                Toast.makeText(getBaseContext(), "Login Gagal", Toast.LENGTH_LONG).show();
            }
        }
    }
}
