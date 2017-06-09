package com.antobo.apps.ventris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectServer extends AppCompatActivity implements View.OnClickListener {

    private EditText et_newserver, et_newname, et_newport;
    private Button btn_add, btn_select;
    private Spinner sp_server;
    private String server_;
    private List<String> listserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);

        findViewById(R.id.et_newserver).requestFocus();
        et_newserver = (EditText) findViewById(R.id.et_newserver);
        et_newname = (EditText) findViewById(R.id.et_newname);
        et_newport = (EditText) findViewById(R.id.et_newport);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);

        listserver = new ArrayList<String>();

        Index.serversharedpreferences = getSharedPreferences("server", Context.MODE_PRIVATE);
        server_ = Index.serversharedpreferences.getString("server", "");

        List<String> spinnerArray =  new ArrayList<String>();
        String[] pieces = server_.trim().split("\\|");
        for(int i=0 ; i < pieces.length ; i++){
            if(!pieces[i].equals(""))
            {
                String[] piece = pieces[i].trim().split("\\~");

                String ip, name, port;

                ip = piece[0];
                name = piece[1];
                if(piece[2].equals("0")) port = "";
                else port = ":" + piece[2];

                spinnerArray.add(name + " (" + ip + port + ")");
                listserver.add(ip+port);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_server = (Spinner) findViewById(R.id.sp_server);
        sp_server.setAdapter(adapter);
    }

    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_add){
            String server = et_newserver.getText().toString();
            String name = et_newname.getText().toString();
            String port = et_newport.getText().toString();

            if(server.equals("") || name.equals(""))
            {
                Toast.makeText(getBaseContext(), "IP and name required", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(port.equals("")) port = "";
                else port = ":" + port;

                String newserver = "";
                String[] pieces = server_.trim().split("\\|");
                for(int i=0 ; i < pieces.length ; i++){
                    int cek = -1;
                    if(!pieces[i].equals(""))
                    {
                        String[] piece = pieces[i].trim().split("\\~");

                        String name_ = piece[1];
                        if(name.equals(name_))
                        {
                            cek = i;
                        }
                    }

                    if(cek==-1)
                    {
                        newserver += pieces[i] + "|";
                    }
                }

                SharedPreferences.Editor editor = Index.serversharedpreferences.edit();
                editor.putString( "servernow",server + port);
                if(port.equals("")) port = "0";
                editor.putString( "server",newserver + server + "~" + name + "~" + port + "|");
                editor.commit();

                Intent i = new Intent(SelectServer.this, Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        }
        else
        {
            String selected = listserver.get(sp_server.getSelectedItemPosition());

            SharedPreferences.Editor editor = Index.serversharedpreferences.edit();
            editor.putString( "servernow",selected);
            editor.commit();

            Intent i = new Intent(SelectServer.this, Login.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            finish();
        }
    }
}
