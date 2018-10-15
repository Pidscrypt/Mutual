package pidscrypt.world.mutual.mutal.http;

import android.app.Service;
import android.os.AsyncTask;
import android.os.PowerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.MediaPath;

/**
 * Created by PidScrypt on 2/3/2018.
 * @TODO find null pointer in the code <expected_cause>doInBackgroundReturnsEmpty</expected_cause>
 */

public class HttpRequest extends AsyncTask<HttpCall, Integer, String> {

    private String jsonResponse;

    public HttpRequest(){

    }

    @Override
    protected String doInBackground(HttpCall... params) {
        HttpURLConnection urlConnection = null;
        HttpCall httpCall = params[0];
        StringBuilder response = new StringBuilder();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            String dataParams = "";
            URL url;
            if(httpCall.getParams() != null){
                dataParams = getDataString(httpCall.getParams(), httpCall.getMethodType());
                url = new URL(httpCall.getMethodType() == HttpCall.GET ? httpCall.getUrl() + dataParams:httpCall.getUrl());
            }else{
                url = new URL(httpCall.getUrl());
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpCall.getMethodType() == HttpCall.GET ? "GET":"POST");

            if((httpCall.getParams() != null) && (httpCall.getMethodType() == HttpCall.POST)){
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.append(dataParams);
                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                if(httpCall.isFOR_FILE()){
                    int fileLength = urlConnection.getContentLength();
                    String fileName = "";
                    String contenType = urlConnection.getContentType();
                    fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    fileName = "/AUD_"+fileName+".m4a";

                    inputStream = urlConnection.getInputStream();
                    //@TODO: change file name here ...
                    outputStream = new FileOutputStream(android.os.Environment.getExternalStorageDirectory()+"/"+MediaPath.AUDIO_RECIEVED_FOLDER+fileName);
                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;

                    while((count = inputStream.read()) != -1){
                        if(isCancelled()){
                            inputStream.close();
                            return  null;
                        }
                        total += count;
                        if(fileLength > 0){
                            publishProgress((int)(total * 100 / fileLength));
                        }

                        outputStream.write(data,0,count);
                    }
                }else{
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while((line = br.readLine()) != null){
                        response.append(line);
                    }
                    jsonResponse = response.toString();
                }
            }

        } catch (UnsupportedEncodingException e) {
            jsonResponse = e.getMessage();
            e.printStackTrace();
        } catch (MalformedURLException e) {
            jsonResponse = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            jsonResponse = e.getMessage();
            e.printStackTrace();
        }finally {
            try {
                if(outputStream != null)
                    outputStream.close();
                if(inputStream != null)
                    inputStream.close();
            }catch (IOException ignored){
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                return null;
            }
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //jsonResponse(s);
        requestResponse(s);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        requestPreExecute();
    }

    public void requestPreExecute() {

    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        requestProgress(values);
    }

    public void requestProgress(Integer... progress) {

    }

    public void requestResponse(String jsonResponse){
        //MainActivity.fetchErrorView.setText(response);
        //return jsonResponse;

    }

    private String getDataString(HashMap<String, String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(HashMap.Entry<String, String> entry : params.entrySet()){
            if(isFirst){
                isFirst = false;
                if(methodType == HttpCall.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
