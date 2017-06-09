package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SalesOrderAddHeader extends Fragment implements View.OnClickListener{

    private TextView tv_kode, tv_tanggal, tv_tanggalkirim, tv_lokasi, tv_gudang, tv_jenis, tv_customer, tv_bex, tv_area, tv_valuta, tv_approvalby;
    private Spinner sp_pembayaran;
    private EditText et_jatuhtempo;
    private Button btn_cancel, btn_next;

    private DatePickerDialog dp_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.salesorder_add_header, container, false);
        getActivity().setTitle("Sales Order");


        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_kode = (TextView) v.findViewById(R.id.tv_kode);
        tv_tanggal = (TextView) v.findViewById(R.id.tv_tanggal);
        tv_tanggalkirim = (TextView) v.findViewById(R.id.tv_tanggalkirim);
        tv_tanggalkirim.setOnClickListener(this);
        tv_lokasi = (TextView) v.findViewById(R.id.tv_lokasi);
        tv_gudang = (TextView) v.findViewById(R.id.tv_gudang);
        tv_gudang.setOnClickListener(this);
        tv_jenis = (TextView) v.findViewById(R.id.tv_jenis);
        tv_jenis.setOnClickListener(this);
        tv_customer = (TextView) v.findViewById(R.id.tv_customer);
        tv_customer.setOnClickListener(this);
        tv_bex = (TextView) v.findViewById(R.id.tv_bex);
        tv_area = (TextView) v.findViewById(R.id.tv_area);
        tv_area.setOnClickListener(this);
        tv_valuta = (TextView) v.findViewById(R.id.tv_valuta);
        tv_valuta.setOnClickListener(this);
        tv_approvalby = (TextView) v.findViewById(R.id.tv_approvalby);
        tv_approvalby.setOnClickListener(this);
        sp_pembayaran = (Spinner) v.findViewById(R.id.sp_pembayaran);
        et_jatuhtempo = (EditText) v.findViewById(R.id.et_jatuhtempo);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        // Define DatePicker
        Calendar newCalendar = Calendar.getInstance();
        dp_date = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);

                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                    Date daysBeforeDate = cal.getTime();

                    if(daysBeforeDate.getTime() > newdate.getTime())
                    {
                        tv_tanggalkirim.setText("");
                    }
                    else
                    {
                        tv_tanggalkirim.setText(date);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dp_date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //-----END DECLARE---------------------------------------------------------------------------------------

        Calendar newDate = Calendar.getInstance();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(newDate.getTime());
//        tv_tanggal.setText(formattedDate);

        tv_gudang.setText(Index.salesordersharedpreferences.getString("namagudang", ""));
        tv_jenis.setText(Index.salesordersharedpreferences.getString("namajenispenjualan", ""));
        tv_bex.setText(Index.user_nama);
        tv_lokasi.setText(Index.cabang_nama);
        tv_customer.setText(Index.salesordersharedpreferences.getString("namacustomer", ""));
        tv_area.setText(Index.salesordersharedpreferences.getString("namaarea", ""));
        tv_valuta.setText(Index.salesordersharedpreferences.getString("namavaluta", "") + "  =  " + Index.salesordersharedpreferences.getString("kursvaluta", ""));
        tv_approvalby.setText(Index.salesordersharedpreferences.getString("namaapprovalby", ""));

        tv_tanggal.setText(formattedDate);
        tv_tanggalkirim.setText(Index.salesordersharedpreferences.getString("tanggal", ""));
        et_jatuhtempo.setText(Index.salesordersharedpreferences.getString("jatuhtempo", ""));

        et_jatuhtempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SharedPreferences.Editor poseditor = Index.salesordersharedpreferences.edit();
                    poseditor.putString( "jatuhtempo", et_jatuhtempo.getText().toString());
                    poseditor.commit();
                }
            }
        });

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "Sales Order");
        poseditor.commit();

        SharedPreferences.Editor editor = Index.salesordersharedpreferences.edit();

        editor.putString( "tanggal", tv_tanggal.getText().toString());
        editor.putString( "tanggalkirim", tv_tanggalkirim.getText().toString());
        editor.putString( "pembayaran", sp_pembayaran.getSelectedItem().toString());
        editor.putString( "jatuhtempo", et_jatuhtempo.getText().toString());
        editor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.tv_tanggalkirim){
            dp_date.show();
        }
        else if(v.getId() == R.id.tv_gudang){
            Fragment fragment = new ChooseGudang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_jenis){
            Fragment fragment = new ChooseJenisPenjualan();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_customer){
            Fragment fragment = new ChooseCustomer();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_area){
            Fragment fragment = new ChooseArea();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_valuta){
            Fragment fragment = new ChooseValuta();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_approvalby){
            Fragment fragment = new ChooseApprovalBy();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_cancel){
            getActivity().getSharedPreferences("salesorder", Context.MODE_PRIVATE).edit().clear().commit();

            Fragment fragment = new SalesOrderList();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_next){
            String tanggalkirim = tv_tanggal.getText().toString();
            String jatuhtempo = et_jatuhtempo.getText().toString();
            String jenis = tv_jenis.getText().toString();
            String customer = tv_customer.getText().toString();
            String area = tv_area.getText().toString();
            String approval = tv_approvalby.getText().toString();

            if(tanggalkirim.equals("")||jatuhtempo.equals("")||jenis.equals("")||customer.equals("")||area.equals("")||approval.equals(""))
            {
                Toast.makeText(getContext(), "Some field required", Toast.LENGTH_LONG).show();
            }
            else
            {
                Fragment fragment = new SalesOrderAddGridBarang();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
}