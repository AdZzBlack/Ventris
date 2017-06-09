package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class pdf_piutang extends Fragment implements View.OnClickListener{

    private TextView tv_customer, tv_bex, tv_valuta, tv_enddate;
    private EditText et_pdf;
    private Button btn_check;
    private ImageButton btn_customer, btn_bex, btn_valuta;
    private DatePickerDialog dp_end;

    private String nomorcustomer, namacustomer, nomorbex, namabex, nomorvaluta, namavaluta, enddate;

    private SharedPreferences sp;

    private PdfPCell cell;
    private Image bgImage;
    private String path;
    private File dir;
    private File file;

    //use to set background color
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pdf_piutang_filter, container, false);
        getActivity().setTitle("Piutang");

        sp = getActivity().getSharedPreferences("piutang", Context.MODE_PRIVATE);

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_customer = (TextView) v.findViewById(R.id.tv_customer);
        tv_customer.setOnClickListener(this);
        tv_bex = (TextView) v.findViewById(R.id.tv_bex);
        tv_valuta = (TextView) v.findViewById(R.id.tv_valuta);
        tv_valuta.setOnClickListener(this);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_create);
        btn_check.setOnClickListener(this);

        et_pdf = (EditText) v.findViewById(R.id.et_pdf);

        btn_customer = (ImageButton) v.findViewById(R.id.btn_clear_customer);
        btn_customer.setOnClickListener(this);
        btn_bex = (ImageButton) v.findViewById(R.id.btn_clear_bex);
        btn_valuta = (ImageButton) v.findViewById(R.id.btn_clear_valuta);
        btn_valuta.setOnClickListener(this);

        if (!Index.user_jabatan.equals("SALES")) {
            btn_bex.setOnClickListener(this);
            tv_bex.setOnClickListener(this);
        }
        else
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString( "nomorbex", Index.user_nomor);
            editor.putString( "namabex", Index.user_nama);
            editor.commit();
        }

        Calendar endDate = Calendar.getInstance();
        dp_end = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_enddate.setText(date);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString( "enddate", date);
                    editor.commit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        //-----END DECLARE---------------------------------------------------------------------------------------

        nomorcustomer = sp.getString("nomorcustomer", "");
        namacustomer = sp.getString("namacustomer", "");
        nomorbex = sp.getString("nomorbex", "");
        namabex = sp.getString("namabex", "");
        nomorvaluta = sp.getString("nomorvaluta", "");
        namavaluta = sp.getString("namavaluta", "");
        enddate = sp.getString("enddate", "");

        tv_customer.setText(namacustomer);
        tv_bex.setText(namabex);
        tv_valuta.setText(namavaluta);
        tv_enddate.setText(enddate);

        //creating new file path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + GlobalFunction.pdf_folder;
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "pdf Piutang Report");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_create){
            Log.d("enddate", sp.getString("enddate", "") + "123");
            String actionUrl = "Report/getPiutang/";
            new getPiutang().execute( actionUrl );
        }
        else if(v.getId() == R.id.btn_clear_customer){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString( "nomorcustomer", "");
            editor.putString( "namacustomer", "");
            editor.commit();
            tv_customer.setText("");
        }
        else if(v.getId() == R.id.btn_clear_bex){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString( "nomorbex", "");
            editor.putString( "namabex", "");
            editor.commit();
            tv_bex.setText("");
        }
        else if(v.getId() == R.id.btn_clear_valuta){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString( "nomorvaluta", "");
            editor.putString( "namavaluta", "");
            editor.commit();
            tv_valuta.setText("");
        }
        else if(v.getId() == R.id.tv_customer){
            Fragment fragment = new ChooseCustomer();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_bex){
            Fragment fragment = new ChooseBEX();
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
        else if(v.getId() == R.id.tv_enddate){
            dp_end.show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void createPDF(String result) throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);

            String namee = "temp.pdf";
            if(!et_pdf.getText().toString().equals(""))
            {
                namee = et_pdf.getText().toString() + ".pdf";
            }

            file = new File(dir, namee);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            try {
                PdfPTable table = new PdfPTable(10);

                float[] columnWidth = new float[]{8, 8, 10, 10, 7, 14, 7, 12, 12, 12};
                table.setWidths(columnWidth);

                Boolean head = true;
                Double total1 = 0.0, total2 = 0.0, total3 = 0.0;
                Double grandtotal1 = 0.0, grandtotal2 = 0.0, grandtotal3 = 0.0;
                String subhead = "";

                try {
                    JSONArray jsonarray = new JSONArray(result);
                    if(jsonarray.length() > 0){

                        Font fheader = new Font(Font.FontFamily.TIMES_ROMAN,10.0f, Font.BOLD, BaseColor.BLACK);
                        Font fsubheader = new Font(Font.FontFamily.TIMES_ROMAN,6.0f, Font.BOLD, BaseColor.BLACK);
                        Font ffooter = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.BOLD, BaseColor.BLACK);
                        Font f = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.NORMAL, BaseColor.BLACK);
                        Font fbold = new Font(Font.FontFamily.TIMES_ROMAN,6.0f, Font.BOLDITALIC, BaseColor.BLACK);
                        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.NORMAL, BaseColor.RED);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            if(!obj.has("query")){
                                if(head)
                                {
                                    head = false;

                                    PdfPTable pTable = new PdfPTable(1);
                                    pTable.setWidthPercentage(100);
                                    cell = new PdfPCell(new Phrase("LAPORAN OUTSTANDING FAKTUR JUAL", fheader));
                                    cell.setColspan(10);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);

                                    if(sp.getString("enddate", "").equals(""))
                                    {
                                        Calendar cal = Calendar.getInstance();
                                        Date dateNow = cal.getTime();

                                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                                        String tanggal = format.format(dateNow);

                                        cell = new PdfPCell(new Phrase("Per " + tanggal, fheader));
                                        cell.setColspan(10);
                                        cell.setBorder(Rectangle.NO_BORDER);
                                        table.addCell(cell);
                                    }
                                    else
                                    {
                                        cell = new PdfPCell(new Phrase("Per " + sp.getString("enddate", ""), fheader));
                                        cell.setColspan(10);
                                        cell.setBorder(Rectangle.NO_BORDER);
                                        table.addCell(cell);
                                    }
                                }
                                String separator = (obj.getString("vcSeparator"));
                                String kode = (obj.getString("vcKode"));
                                String nama = (obj.getString("vcNama"));
                                String tanggal = (obj.getString("dtTanggal"));
                                String jt = (obj.getString("dtJatuhTempo"));
                                String kodetransaksi = (obj.getString("vcKodeTransaksi"));
                                String kodeso = (obj.getString("vcKodeSO"));
                                String namasales = (obj.getString("vcNamaSales"));
                                String keterangan = (obj.getString("vcKeterangan"));
                                String valuta = (obj.getString("vcValuta"));
                                String kurs = (obj.getString("decKurs"));
                                String invoice = (obj.getString("decInvoice"));
                                String sisa = (obj.getString("decSisa"));
                                String sisaidr = (obj.getString("decSisaIDR"));

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date newtgl = format.parse(tanggal);
                                Date newjt = format.parse(jt);

                                format = new SimpleDateFormat("dd/MMM/yyyy");
                                tanggal = format.format(newtgl);
                                jt = format.format(newjt);


                                if(!subhead.equals(kode + " - " + nama))
                                {
                                    if(!subhead.equals(""))
                                    {
                                        cell = new PdfPCell(new Phrase("TOTAL " + subhead, fsubheader));
                                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                        cell.setColspan(7);
                                        table.addCell(cell);

                                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total1)), fsubheader));
                                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                        table.addCell(cell);

                                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total2)), fsubheader));
                                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                        table.addCell(cell);

                                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total3)), fsubheader));
                                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                        table.addCell(cell);
                                    }

                                    subhead = kode + " - " + nama;
                                    total1 = 0.0;
                                    total2 = 0.0;
                                    total3 = 0.0;

                                    cell = new PdfPCell(new Phrase(kode + " - " + nama, fsubheader));
                                    cell.setColspan(10);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Tgl", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("JT", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Kode Invoice", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Kode Order", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Bex", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Keterangan", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Valuta", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Total Default", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Sisa Default", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Sisa IDR", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                }

                                total1 += Double.parseDouble(invoice);
                                total2 += Double.parseDouble(sisa);
                                total3 += Double.parseDouble(sisaidr);
                                grandtotal1 += Double.parseDouble(invoice);
                                grandtotal2 += Double.parseDouble(sisa);
                                grandtotal3 += Double.parseDouble(sisaidr);

                                cell = new PdfPCell(new Phrase(tanggal, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(jt, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(kodetransaksi, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(kodeso, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(namasales, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(keterangan, f));
                                cell.setBorder(12);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(valuta, f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(invoice), f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(sisa), f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(sisaidr), f));
                                cell.setBorder(12);
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                table.addCell(cell);
                            }
                        }
                        cell = new PdfPCell(new Phrase("TOTAL " + subhead, fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        cell.setColspan(7);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total1)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total2)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total3)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(" ", f));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setColspan(10);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("GRAND TOTAL", fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        cell.setColspan(7);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(grandtotal1)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(grandtotal2)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(grandtotal3)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Load Report Failed", Toast.LENGTH_LONG).show();
                }



                doc.add(table);
                Toast.makeText(getContext(), "created PDF", Toast.LENGTH_LONG).show();
                displaypdf(file);
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getPiutang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("customer", sp.getString("nomorcustomer", ""));
                Index.jsonObject.put("bex", sp.getString("nomorbex", ""));
                Index.jsonObject.put("valuta", sp.getString("nomorvaluta", ""));
                Index.jsonObject.put("tanggal_akhir", sp.getString("enddate", ""));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Index.globalfunction.executePost(urls[0], Index.jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("resss", result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length() > 0){
                    try {
                        createPDF(result);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                Index.pDialog.dismiss();
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Load Report Failed", Toast.LENGTH_LONG).show();
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

    public void displaypdf(File pdf) {

        File file = pdf;
//        file = new File(Environment.getExternalStorageDirectory() + path + "/" + pdf);
        Toast.makeText(getContext(), file.toString() , Toast.LENGTH_LONG).show();
        if(file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        else
            Toast.makeText(getContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }
}