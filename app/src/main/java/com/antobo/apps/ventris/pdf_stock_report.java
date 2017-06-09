package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.path;

public class pdf_stock_report extends Fragment implements View.OnClickListener{

    private TextView tv_gudang, tv_namabeli, tv_brand, tv_tipe, tv_group, tv_enddate;
    private EditText et_pdf;
    private Button btn_check;
    private ImageButton btn_gudang, btn_nama, btn_brand, btn_tipe, btn_group;
    private DatePickerDialog dp_end;

    private String nomorgudang, namagudang, nomorbarangnamabeli, namabarangnamabeli, nomorbrand, namabrand, nomortipe, namatipe, nomorgroup, namagroup, enddate;

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
        View v = inflater.inflate(R.layout.pdf_stock_filter, container, false);
        getActivity().setTitle("Stock Report");

        //-----START DECLARE---------------------------------------------------------------------------------------
        tv_gudang = (TextView) v.findViewById(R.id.tv_gudang);
        tv_gudang.setOnClickListener(this);
        tv_namabeli = (TextView) v.findViewById(R.id.tv_namabeli);
        tv_namabeli.setOnClickListener(this);
        tv_brand = (TextView) v.findViewById(R.id.tv_brand);
        tv_brand.setOnClickListener(this);
        tv_tipe = (TextView) v.findViewById(R.id.tv_tipe);
        tv_tipe.setOnClickListener(this);
        tv_group = (TextView) v.findViewById(R.id.tv_group);
        tv_group.setOnClickListener(this);
        tv_gudang = (TextView) v.findViewById(R.id.tv_gudang);
        tv_gudang.setOnClickListener(this);
        tv_enddate = (TextView) v.findViewById(R.id.tv_enddate);
        tv_enddate.setOnClickListener(this);
        btn_check = (Button) v.findViewById(R.id.btn_create);
        btn_check.setOnClickListener(this);

        et_pdf = (EditText) v.findViewById(R.id.et_pdf);

        btn_gudang = (ImageButton) v.findViewById(R.id.btn_clear_gudang);
        btn_gudang.setOnClickListener(this);
        btn_nama = (ImageButton) v.findViewById(R.id.btn_clear_nama);
        btn_nama.setOnClickListener(this);
        btn_brand = (ImageButton) v.findViewById(R.id.btn_clear_brand);
        btn_brand.setOnClickListener(this);
        btn_tipe = (ImageButton) v.findViewById(R.id.btn_clear_tipe);
        btn_tipe.setOnClickListener(this);
        btn_group = (ImageButton) v.findViewById(R.id.btn_clear_group);
        btn_group.setOnClickListener(this);

        Calendar endDate = Calendar.getInstance();
        dp_end = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newdate = sdf.parse(date);
                    date = sdf.format(newdate);
                    tv_enddate.setText(date);

                    SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
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

        nomorgudang = Index.stocksharedpreferences.getString("nomorgudang", "");
        namagudang = Index.stocksharedpreferences.getString("namagudang", "");
        nomorbarangnamabeli = Index.stocksharedpreferences.getString("nomorbarangnamabeli", "");
        namabarangnamabeli = Index.stocksharedpreferences.getString("namabarangnamabeli", "");
        nomorbrand = Index.stocksharedpreferences.getString("nomorbrand", "");
        namabrand = Index.stocksharedpreferences.getString("namabrand", "");
        nomortipe = Index.stocksharedpreferences.getString("nomortipe", "");
        namatipe = Index.stocksharedpreferences.getString("namatipe", "");
        nomorgroup = Index.stocksharedpreferences.getString("nomorgroupbarang", "");
        namagroup = Index.stocksharedpreferences.getString("namagroupbarang", "");
        enddate = Index.stocksharedpreferences.getString("enddate", "");

        tv_gudang.setText(namagudang);
        tv_namabeli.setText(namabarangnamabeli);
        tv_brand.setText(namabrand);
        tv_tipe.setText(namatipe);
        tv_group.setText(namagroup);
        tv_gudang.setText(namagudang);
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
        poseditor.putString( "position", "pdf Stock Report");
        poseditor.commit();
        
        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_create){
            String actionUrl = "Stockreport/getStockKomoditi/";
            new getStockKomoditi().execute( actionUrl );
        }
        else if(v.getId() == R.id.btn_clear_gudang){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorgudang", "");
            editor.putString( "namagudang", "");
            editor.commit();
            tv_gudang.setText("");
        }
        else if(v.getId() == R.id.btn_clear_nama){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorbarangnamabeli", "");
            editor.putString( "namabarangnamabeli", "");
            editor.commit();
            tv_namabeli.setText("");
        }
        else if(v.getId() == R.id.btn_clear_brand){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorbrand", "");
            editor.putString( "namabrand", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.btn_clear_tipe){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomortipe", "");
            editor.putString( "namatipe", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.btn_clear_group){
            SharedPreferences.Editor editor = Index.stocksharedpreferences.edit();
            editor.putString( "nomorgroupbarang", "");
            editor.putString( "namagroupbarang", "");
            editor.commit();
            tv_brand.setText("");
        }
        else if(v.getId() == R.id.tv_gudang){
            Fragment fragment = new ChooseGudang();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_namabeli){
            Fragment fragment = new ChooseNamaBeli();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_brand){
            Fragment fragment = new ChooseBrand();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_tipe){
            Fragment fragment = new ChooseTipe();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v.getId() == R.id.tv_group){
            Fragment fragment = new ChooseGroupBarang();
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
            //create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = pdf_stock_report.this.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Venus Stock Report"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(7);

                float[] columnWidth = new float[]{16, 50, 35, 15, 15, 12, 12};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(7);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(7);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(7);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("Kode"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Nama"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Gudang"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Stock"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Pending"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Satuan"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Shade"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(9);

                try {
                    JSONArray jsonarray = new JSONArray(result);
                    if(jsonarray.length() > 0){

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            if(!obj.has("query")){
                                String kodebarang = (obj.getString("KodeBarang"));
                                String namabarang = (obj.getString("NamaJual"));
                                String gudang = (obj.getString("Gudang"));
                                String stock = (obj.getString("Fisik"));
                                String pending = (obj.getString("Pending"));
                                String satuan = (obj.getString("Satuan"));
                                String info1 = (obj.getString("Konversi2"));
                                String info2 = (obj.getString("Konversi3"));
                                String shade = (obj.getString("Shade"));

                                table.addCell(kodebarang);
                                table.addCell(namabarang);
                                table.addCell(gudang);
                                table.addCell(GlobalFunction.delimeter(stock));
                                table.addCell(GlobalFunction.delimeter(pending));
                                table.addCell(satuan);
                                table.addCell(shade);
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Load Report Failed", Toast.LENGTH_LONG).show();
                }

//                PdfPTable ftable = new PdfPTable(9);
//                ftable.setWidthPercentage(100);
//                float[] columnWidthaa = new float[]{30, 10, 30, 10, 30, 10};
//                ftable.setWidths(columnWidthaa);
//                cell = new PdfPCell();
//                cell.setColspan(6);
//                cell.setBackgroundColor(myColor1);
//                cell = new PdfPCell(new Phrase("Total Nunber"));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor1);
//                ftable.addCell(cell);
//                cell = new PdfPCell(new Paragraph("Footer"));
//                cell.setColspan(6);
//                ftable.addCell(cell);
//                cell = new PdfPCell();
//                cell.setColspan(6);
//                cell.addElement(ftable);
//                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getContext(), "created PDF", Toast.LENGTH_LONG).show();
                displaypdf(file);
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getStockKomoditi extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("nama", Index.stocksharedpreferences.getString("namabarangnamabeli", ""));
                Index.jsonObject.put("brand", Index.stocksharedpreferences.getString("namabrand", ""));
                Index.jsonObject.put("tipe", Index.stocksharedpreferences.getString("namatipe", ""));
                Index.jsonObject.put("group", Index.stocksharedpreferences.getString("namagroupbarang", ""));
                Index.jsonObject.put("gudang", Index.stocksharedpreferences.getString("namagudang", ""));
                Index.jsonObject.put("tanggal_akhir", Index.stocksharedpreferences.getString("enddate", ""));
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