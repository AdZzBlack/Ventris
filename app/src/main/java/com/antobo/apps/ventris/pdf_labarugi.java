package com.antobo.apps.ventris;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class pdf_labarugi extends Fragment implements View.OnClickListener{

    private EditText et_pdf, et_bulan, et_tahun;
    private Button btn_check;
    private DatePickerDialog dp_end;

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
        View v = inflater.inflate(R.layout.pdf_neraca_filter, container, false);
        getActivity().setTitle("Laba Rugi");

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_check = (Button) v.findViewById(R.id.btn_create);
        btn_check.setOnClickListener(this);

        et_pdf = (EditText) v.findViewById(R.id.et_pdf);
        et_bulan = (EditText) v.findViewById(R.id.et_bulan);
        et_tahun = (EditText) v.findViewById(R.id.et_tahun);

        //-----END DECLARE---------------------------------------------------------------------------------------

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
        poseditor.putString( "position", "pdf Labarugi");
        poseditor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_create){
            String actionUrl = "Report/getLabaRugi/";
            new getLabaRugi().execute( actionUrl );
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
                PdfPTable table = new PdfPTable(4);

                float[] columnWidth = new float[]{40, 20, 20, 20};
                table.setWidths(columnWidth);

                Boolean head = true;
                Double total1 = 0.0, total2 = 0.0, total3 = 0.0;
                String subhead = "";

                try {
                    JSONArray jsonarray = new JSONArray(result);
                    if(jsonarray.length() > 0){

                        Font fheader = new Font(Font.FontFamily.TIMES_ROMAN,10.0f, Font.BOLD, BaseColor.BLACK);
                        Font fsubheader = new Font(Font.FontFamily.TIMES_ROMAN,7.0f, Font.BOLD, BaseColor.BLACK);
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
                                    String bulanIni = (obj.getString("vcBulanIni"));
                                    String bulanLalu = (obj.getString("vcBulanLalu"));
                                    String tahunIni = (obj.getString("vcTahunIni"));
                                    String filterperiode = (obj.getString("vcFilterPeriode"));

                                    PdfPTable pTable = new PdfPTable(1);
                                    pTable.setWidthPercentage(100);
                                    cell = new PdfPCell(new Phrase("LAPORAN LABA / RUGI", fheader));
                                    cell.setColspan(4);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(filterperiode, fheader));
                                    cell.setColspan(4);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("Deskripsi", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(bulanIni, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(bulanLalu, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(tahunIni, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell();
                                    cell.setColspan(4);
                                }
                                String group = (obj.getString("vcGroup"));
                                String nama = (obj.getString("vcNama"));
                                String bulanini = (obj.getString("decBulanIni"));
                                String bulanLalu = (obj.getString("decBulanLalu"));
                                String tahunini = (obj.getString("decTahunIni"));

                                total1 += Double.parseDouble(bulanini);
                                total2 += Double.parseDouble(bulanLalu);
                                total3 += Double.parseDouble(tahunini);


                                if(!subhead.equals(group))
                                {
                                    subhead = group;
                                    cell = new PdfPCell(new Phrase(group, fbold));
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("", f));
                                    cell.setBorder(12);
                                    table.addCell(cell);
                                    table.addCell(cell);
                                    table.addCell(cell);
                                }

                                cell = new PdfPCell(new Phrase("     " + nama, f));
                                cell.setBorder(12);
                                table.addCell(cell);

                                if(GlobalFunction.delimeter(bulanini).equals(".00"))
                                {
                                    cell = new PdfPCell(new Phrase("", f));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }
                                else
                                {
                                    if(Float.parseFloat(bulanini)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(bulanini), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(bulanini), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(bulanLalu).equals(".00"))
                                {
                                    cell = new PdfPCell(new Phrase("", f));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }
                                else
                                {
                                    if(Float.parseFloat(bulanLalu)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(bulanLalu), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(bulanLalu), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(tahunini).equals(".00"))
                                {
                                    cell = new PdfPCell(new Phrase("", f));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }
                                else
                                {
                                    if(Float.parseFloat(tahunini)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(tahunini), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(tahunini), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setBorder(Rectangle.LEFT);
                                    cell.setBorder(Rectangle.RIGHT);
                                    table.addCell(cell);
                                }
                            }
                        }
                        PdfPTable ftable = new PdfPTable(4);
                        ftable.setWidthPercentage(100);
                        float[] columnWidthaa = new float[]{40, 20, 20, 20};
                        ftable.setWidths(columnWidthaa);

                        cell = new PdfPCell(new Phrase("TOTAL", ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        cell.setBorder(Rectangle.NO_BORDER);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total1)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total2)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total3)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(4);
                        cell.addElement(ftable);

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

    private class getLabaRugi extends AsyncTask<String, Void, String> {
        String bulan = et_bulan.getText().toString();
        String tahun = et_tahun.getText().toString();

        @Override
        protected String doInBackground(String... urls) {
            try {
                Index.jsonObject = new JSONObject();
                Index.jsonObject.put("bulan", bulan);
                Index.jsonObject.put("tahun", tahun);
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