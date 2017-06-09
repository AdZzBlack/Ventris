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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class pdf_neraca extends Fragment implements View.OnClickListener{

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
        getActivity().setTitle("Neraca");

        //-----START DECLARE---------------------------------------------------------------------------------------
        btn_check = (Button) v.findViewById(R.id.btn_create);
        btn_check.setOnClickListener(this);

        et_pdf = (EditText) v.findViewById(R.id.et_pdf);
        et_bulan = (EditText) v.findViewById(R.id.et_bulan);
        et_tahun = (EditText) v.findViewById(R.id.et_tahun);

        //-----END DECLARE---------------------------------------------------------------------------------------

        //creating new file path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + GlobalFunction.pdf_folder;
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "Trinity/PDF Files";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return v;
    }

    public void onClick(View v) {
        SharedPreferences.Editor poseditor = Index.positionsharedpreferences.edit();
        poseditor.putString( "position", "pdf Neraca");
        poseditor.commit();

        v.startAnimation(Index.buttoneffect);
        if(v.getId() == R.id.btn_create){
            String actionUrl = "Report/getNeraca/";
            new getNeraca().execute( actionUrl );
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
                PdfPTable table = new PdfPTable(6);

                float[] columnWidth = new float[]{20, 15, 15, 20, 15, 15};
                table.setWidths(columnWidth);

                Boolean head = true;
                Double total1 = 0.0, total2 = 0.0, total3 = 0.0, total4 = 0.0;
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    if(jsonarray.length() > 0){

                        Font fheader = new Font(Font.FontFamily.TIMES_ROMAN,10.0f,Font.BOLD,BaseColor.BLACK);
                        Font fsubheader = new Font(Font.FontFamily.TIMES_ROMAN,7.0f,Font.BOLD,BaseColor.BLACK);
                        Font ffooter = new Font(Font.FontFamily.TIMES_ROMAN,5.0f,Font.BOLD,BaseColor.BLACK);
                        Font f = new Font(Font.FontFamily.TIMES_ROMAN,5.0f,Font.NORMAL,BaseColor.BLACK);
                        Font fbold = new Font(Font.FontFamily.TIMES_ROMAN,6.0f,Font.BOLD,BaseColor.BLACK);
                        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,5.0f,Font.NORMAL,BaseColor.RED);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            if(!obj.has("query")){
                                if(head)
                                {
                                    head = false;
                                    String tglawal = (obj.getString("dtTanggalAwal"));
                                    String tglakhir = (obj.getString("dtTanggalAkhir"));
                                    String filterperiode = (obj.getString("vcFilterPeriode"));

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date newtglawal = format.parse(tglawal);
                                    Date newtglakhir = format.parse(tglakhir);

                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(newtglawal);
                                    cal.add(Calendar.DATE, -1);
                                    Date dateBefore = cal.getTime();

                                    format = new SimpleDateFormat("dd MMM yyyy");
                                    tglawal = format.format(dateBefore);
                                    tglakhir = format.format(newtglakhir);


                                    PdfPTable pTable = new PdfPTable(1);
                                    pTable.setWidthPercentage(100);
                                    cell = new PdfPCell(new Phrase("NERACA", fheader));
                                    cell.setColspan(6);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(filterperiode, fheader));
                                    cell.setColspan(6);
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    table.addCell(cell);

                                    cell = new PdfPCell(new Phrase("URAIAN", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(tglakhir, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(tglawal, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase("URAIAN", fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(tglakhir, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);
                                    cell = new PdfPCell(new Phrase(tglawal, fsubheader));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                                    table.addCell(cell);

                                    cell = new PdfPCell();
                                    cell.setColspan(6);
                                }
                                String namagroupkiri = (obj.getString("vcNamaGroupKiri"));
                                String accountkiri = (obj.getString("vcNamaAccountKiri"));
                                String lalukiri = (obj.getString("decLaluKiri"));
                                String kinikiri = (obj.getString("decKiniKiri"));
                                String namagroupkanan = (obj.getString("vcNamaGroupKanan"));
                                String accountkanan = (obj.getString("vcNamaAccountKanan"));
                                String lalukanan = (obj.getString("decLaluKanan"));
                                String kinikanan = (obj.getString("decKiniKanan"));

                                total1 += Double.parseDouble(lalukiri);
                                total2 += Double.parseDouble(kinikiri);
                                total3 += Double.parseDouble(lalukanan);
                                total4 += Double.parseDouble(kinikanan);


                                if(namagroupkiri.equals(""))
                                {
                                    cell = new PdfPCell(new Phrase("     " + accountkiri, f));
                                    table.addCell(cell);
                                }
                                else
                                {
                                    cell = new PdfPCell(new Phrase(namagroupkiri, fbold));
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(kinikiri).equals(".00"))
                                {
                                    table.addCell("");
                                }
                                else
                                {
                                    if(Float.parseFloat(kinikiri)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(kinikiri), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(kinikiri), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(lalukiri).equals(".00"))
                                {
                                    table.addCell("");
                                }
                                else
                                {
                                    if(Float.parseFloat(lalukiri)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(lalukiri), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(lalukiri), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    table.addCell(cell);
                                }

                                if(namagroupkanan.equals(""))
                                {
                                    cell = new PdfPCell(new Phrase("     " + accountkanan, f));
                                    table.addCell(cell);
                                }
                                else
                                {
                                    cell = new PdfPCell(new Phrase(namagroupkanan, fbold));
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(kinikanan).equals(".00"))
                                {
                                    table.addCell("");
                                }
                                else
                                {
                                    if(Float.parseFloat(kinikanan)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(kinikanan), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(kinikanan), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    table.addCell(cell);
                                }

                                if(GlobalFunction.delimeter(lalukanan).equals(".00"))
                                {
                                    table.addCell("");
                                }
                                else
                                {
                                    if(Float.parseFloat(lalukanan)<0) cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(lalukanan), f1));
                                    else cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(lalukanan), f));
                                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                                    table.addCell(cell);
                                }
                            }
                        }
                        PdfPTable ftable = new PdfPTable(6);
                        ftable.setWidthPercentage(100);
                        float[] columnWidthaa = new float[]{20, 15, 15, 20, 15, 15};
                        ftable.setWidths(columnWidthaa);

                        cell = new PdfPCell(new Phrase("TOTAL ASET", ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total2)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total1)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase("TOTAL KEWAJIBAN & EKUITAS",ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total4)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);
                        cell = new PdfPCell(new Phrase(GlobalFunction.delimeter(String.valueOf(total3)),ffooter));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                        ftable.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(6);
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

    private class getNeraca extends AsyncTask<String, Void, String> {
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