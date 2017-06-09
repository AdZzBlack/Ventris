package com.antobo.apps.ventris;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Report extends Fragment implements View.OnClickListener{

    Button btn_neraca, btn_labarugi, btn_hutang, btn_piutang, btn_kas;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report, container, false);
        getActivity().setTitle("Report");

        btn_neraca = (Button) v.findViewById(R.id.btn_neraca);
        btn_neraca.setOnClickListener(this);
        btn_labarugi = (Button) v.findViewById(R.id.btn_labarugi);
        btn_labarugi.setOnClickListener(this);
        btn_hutang = (Button) v.findViewById(R.id.btn_hutang);
        btn_hutang.setOnClickListener(this);
        btn_piutang = (Button) v.findViewById(R.id.btn_piutang);
        btn_piutang.setOnClickListener(this);
        btn_kas = (Button) v.findViewById(R.id.btn_kas);
        btn_kas.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_neraca){
            Fragment fragment = new pdf_neraca();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_labarugi){
            Fragment fragment = new pdf_labarugi();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_piutang){
            Fragment fragment = new pdf_piutang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_hutang){
            Fragment fragment = new pdf_hutang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_kas){
            Fragment fragment = new pdf_kas();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}