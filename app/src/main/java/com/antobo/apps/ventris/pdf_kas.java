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

public class pdf_kas extends Fragment implements View.OnClickListener{

    private TextView tv_startdate, tv_enddate, tv_kas;
    private ImageButton btn_kas;
    private EditText et_pdf;
    private Button btn_check;
    private DatePickerDialog dp_start, dp_end;

    private String startdate, enddate;
    private String nomorkas, namakas, kodekas;

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
        View v = inflater.inflate(R.layout.pdf_kas_filter, container, false);
        getActivity().setTitle("Kas");

        sp = getActivity().getSharedPreferences("kas", Context.MODE_PRIVATE);

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_kas = (TextView) v.findViewById(R.id.tv_kas);
        tv_kas.setOnClickListener(this);
        tv_startdate = (TextView) v.findViewById(R.id.tv_startdate);
        tv_startdate.setOnClickListener(this);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_create);
        btn_check.setOnClickListener(this);

        et_pdf = (EditText) v.findViewById(R.id.et_pdf);

        btn_kas = (ImageButton) v.findViewById(R.id.btn_clear_kas);
        btn_kas.setOnClickListener(this);

        Calendar startDate = Calendar.getInstance();
        dp_start = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_startdate.setText(date);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString( "startdate", date);
                    editor.commit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        },startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

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

        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = format.format(dateNow);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString( "startdate", tanggal);
        editor.putString( "enddate", tanggal);
        editor.commit();

        startdate = sp.getString("startdate", "");
        enddate = sp.getString("enddate", "");
        nomorkas = sp.getString("nomorkas", "");
        namakas = sp.getString("namakas", "");
        kodekas = sp.getString("kode", "");

        tv_startdate.setText(startdate);
        tv_enddate.setText(enddate);
        tv_kas.setText(namakas);

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
        poseditor.putString( "position", "pdf Kas");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_create){
            String actionUrl = "Report/getKas/";
            new getKas().execute( actionUrl );
        }
        else if(v.getId() == R.id.tv_startdate){
            dp_start.show();
        }
        else if(v.getId() == R.id.tv_enddate){
            dp_end.show();
        }
        else if(v.getId() == R.id.tv_kas){
            Fragment fragment = new ChooseKas();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.btn_clear_kas){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString( "nomorkas", "");
            editor.putString( "namakas", "");
            editor.putString( "kode", "");
            editor.commit();
            tv_kas.setText("");
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
                PdfPTable table = new PdfPTable(7);

                float[] columnWidth = new float[]{8, 5, 18, 24, 15, 15, 15};
                table.setWidths(columnWidth);

                Boolean head = true;
                Double totalD = 0.0, totalK = 0.0, saldo = 0.0, saldoAwal = 0.0;
                String kode = "", accountHeader = "";
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    if(jsonarray.length() > 0){

                        Font fheader = new Font(Font.FontFamily.TIMES_ROMAN,10.0f, Font.BOLD, BaseColor.BLACK);
                        Font fsubheader = new Font(Font.FontFamily.TIMES_ROMAN,7.0f, Font.BOLD, BaseColor.BLACK);
                        Font ffooter = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.BOLD, BaseColor.BLACK);
                        Font f = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.NORMAL, BaseColor.BLACK);
                        Font fbold = new Font(Font.FontFamily.TIMES_ROMAN,6.0f, Font.BOLD, BaseColor.BLACK);
                        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,5.0f, Font.NORMAL, BaseColor.RED);


                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            if(!obj.has("query")){
                                kode = sp.getString("kode", "");
                                accountHeader = (obj.getString("vcNamaAccountHeader"));
                                if(head)
                                {
                                    head = false;
                                    String tglawal = (obj.getString("dtTanggal"));
                                    String tglakhir = sp.getString("enddate", "");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date newtglawal = format.parse(tglawal);
                                    Date newtglakhir = format.parse(tglakhir);

                                    format = new SimpleDateFormat("dd MMMM yyyy");
                                    tglawal = format.format(newtglawal);
                                    tglakhir = format.format(newtglakhir);


                                    PdfPTable pTable = new PdfPTable(1);
                                    pTable.setWidthPercentage(100);
                                    cell = new PdfPCell(new Phrase("LAPORAN DETAIL KAS HARIAN", fheader));
                                    cell.setColspan(7);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("Periode : " + tglawal + " - " + tglakhir, fheader));
                                    cell.setColspan(7);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("ACCOUNT : " + kode + " - " + accountHeader, fsubheader));
                                    cell.setColspan(7);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Tgl", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("Jenis", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("Account", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("Kode Transaksi - Deskripsi", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("D", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("K", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("Saldo", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell();
                                    cell.setColspan(7);
                                }

                                String tanggal = (obj.getString("dtTanggal"));
                                String kodejenis = (obj.getString("vcKodeJenisDetailKas"));
                                String kodeaccount = (obj.getString("vcKodeAccount"));
                                String namaaccount = (obj.getString("vcNamaAccount"));
                                String kodeheader = (obj.getString("vcKodeHeader"));
                                String deskripsi = (obj.getString("vcDeskripsi"));
                                String d = (obj.getString("decD"));
                                String k = (obj.getString("decK"));

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date newtglawal = format.parse(tanggal);

                                format = new SimpleDateFormat("dd/MMM/yyyy");
                                tanggal = format.format(newtglawal);

                                totalD += Double.parseDouble(d);
                                totalK += Double.parseDouble(k);
                                saldo = totalD - totalK;

                                if(kodeaccount.equals("")) saldoAwal = Double.parseDouble(d) - Double.parseDouble(k);

                                cell = new PdfPCell(new Phrase(tanggal, f));
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                cell.setBorder(12);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(kodejenis, f));
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                cell.setBorder(12);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(kodeaccount + " - " + namaaccount, f));
                                cell.setBorder(12);
                                table.addCell(cell);

                                cell = new PdfPCell(new Phrase(kodeheader + " - " + deskripsi, f));
                                cell.setBorder(12);
                                table.addCell(cell);

                                if(GlobalFunction.delimeter(d).equals(".00"))
                                {
                                    cell = new PdfPCell(new Phrase("-", f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                }
                                else
                                {
                                    cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(d), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(k).equals(".00"))
                                {
                                    cell = new PdfPCell(new Phrase("-", f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                }
                                else {
                                    cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(k), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                }

                                cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(saldo)), f));
                                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                cell.setBorder(12);
                                table.addCell(cell);
                            }
                        }
                        cell = new PdfPCell(new Phrase("GRAND TOTAL", fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        cell.setColspan(4);
                        cell.setBorder(Rectangle.TOP);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(totalD)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(totalK)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(saldo)), fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(" ", f));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setColspan(7);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Account", fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setColspan(5);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Awal", fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Akhir", fsubheader));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(kode + " - " + accountHeader, f));
                        cell.setColspan(5);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(saldoAwal)), f));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(saldo)), f));
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

    private class getKas extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("kode", sp.getString("kode", ""));
                Index.jsonObject.put("tanggal_awal", sp.getString("startdate", ""));
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