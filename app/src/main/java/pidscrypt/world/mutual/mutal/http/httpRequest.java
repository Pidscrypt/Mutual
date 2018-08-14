package pidscrypt.world.mutual.mutal.http;

import android.app.Service;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by PidScrypt on 2/3/2018.
 * @TODO find null pointer in the code <expected_cause>doInBackgroundReturnsEmpty</expected_cause>
 */

public class httpRequest extends AsyncTask<HttpCall, String, String> {

    private String jsonResponse;

    @Override
    protected String doInBackground(HttpCall... params) {
        HttpURLConnection urlConnection = null;
        HttpCall httpCall = params[0];
        StringBuilder response = new StringBuilder();

        try{

            String dataParams = getDataString(httpCall.getParams(), httpCall.getMethodType());
            URL url = new URL(httpCall.getMethodType() == HttpCall.GET ? httpCall.getUrl() + dataParams:httpCall.getUrl());

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
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while((line = br.readLine()) != null){
                    response.append(line);
                }
                jsonResponse = response.toString();
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
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //jsonResponse(s);
        requestResponse(s);

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
