package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PriceListFilter extends Fragment implements View.OnClickListener{

    private EditText et_katakunci;
    private Button btn_check;

    private String katakunci;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pricelist_filter, container, false);
        getActivity().setTitle("Price List Filter");

        //-----START DECLARE---------------------------------------------------------------------------------------
        et_katakunci = (EditText) v.findViewById(R.id.et_katakunci);
        btn_check = (Button) v.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);

        //-----END DECLARE---------------------------------------------------------------------------------------

        katakunci = Index.stocksharedpreferences.getString("katakunci", "");

        et_katakunci.setText(katakunci);

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Price List");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_check){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "katakunci", et_katakunci.getText().toString());
            editor.commit();

            Fragment fragment = new PriceList();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}