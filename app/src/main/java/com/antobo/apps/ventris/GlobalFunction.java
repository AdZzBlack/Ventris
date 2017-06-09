package com.antobo.apps.ventris;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Created by shirogane on 6/19/2016.
 */
public class GlobalFunction{
    //new
    private static String imageUrl;
    private static String hostUrl;
    private static String reportUrl;
    private static String salesUrl;

    private static JSONObject jsonObject;
    private static String result;
    private static String classDialog;

    public static void setResult(String result) { GlobalFunction.result = result; }
    public static void setHostUrl(String hostUrl) { GlobalFunction.hostUrl = hostUrl; }
    public static void setClassDialog(String classDialog) { GlobalFunction.classDialog = classDialog; }
    public static void setJsonObject(JSONObject jsonObject) { GlobalFunction.jsonObject = jsonObject; }

    public static String getResult() { return result; }
    public static String getImageUrl() { return imageUrl; }
    public static String getHostURL() {
        return hostUrl;
    }
    public static String getClassDialog() { return classDialog; }
    public static JSONObject getJsonObject() { return jsonObject; }

    //private message
    public static String private_user_nomor_tujuan = null;
    public static String private_user_nama_tujuan = null;
    public static int private_run_check = 100;

    //group message
    public static String group_nomor = null;
    public static String group_nama = null;
    public static int group_run_check = 100;

    public static String pdf_folder = "/Ventris/PDF Files";

    public static Context context;

    private static SharedPreferences serversharedpreferences;

    public GlobalFunction(Context mContext)
    {
        context = mContext;
        serversharedpreferences = context.getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = serversharedpreferences.getString("servernow", "");
        hostUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        imageUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        reportUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        salesUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
    }

    public static String delimeter(String txt)
    {
        DecimalFormat format=new DecimalFormat("#,###.00");

        if(txt.equals("null")) return "-";
        Double Raw = Double.parseDouble(txt);
        String result = String.valueOf(format.format(Raw));
        return result;
    }

    // Execute POST JSON and Retrieve Data JSON
    public static String executePost(String targetURL, JSONObject jsonObject){
        serversharedpreferences = context.getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = serversharedpreferences.getString("servernow", "");
        hostUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        imageUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        reportUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        salesUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        Log.d("executee", "a" + hostUrl + targetURL);

        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost( hostUrl + targetURL );

            // 3. convert JSONObject to JSON to String
            String json = jsonObject.toString();

            // 4. ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity stringEntity = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(stringEntity);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    // Execute POST JSON and Retrieve Data JSON
    public static String executePostSales(String targetURL, JSONObject jsonObject){
        serversharedpreferences = context.getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = serversharedpreferences.getString("servernow", "");
        hostUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        imageUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        reportUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        salesUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        Log.d("executee", "a" + hostUrl + targetURL);

        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost( salesUrl + targetURL );

            // 3. convert JSONObject to JSON to String
            String json = jsonObject.toString();

            // 4. ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity stringEntity = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(stringEntity);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    // Execute POST JSON and Retrieve Data JSON
    public static String executePostReport(String targetURL, JSONObject jsonObject){
        serversharedpreferences = context.getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = serversharedpreferences.getString("servernow", "");
        hostUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        imageUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        reportUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        salesUrl                 = "http://" + url + "/webserviceBEX/ventris/index.php/api/";
        Log.d("executee", "a" + hostUrl + targetURL);

        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost( reportUrl + targetURL );

            // 3. convert JSONObject to JSON to String
            String json = jsonObject.toString();

            // 4. ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity stringEntity = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(stringEntity);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
