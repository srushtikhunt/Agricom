package com.example.agricom;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    String json_url;
    String JSON_STRING, msg;


    public AsyncResponse delegate = null;

    public BackgroundTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... strings) {
        json_url = "https://agricoma.000webhostapp.com/appservice/";
        String havetoDo;
        String sprlvl = json_url + "set_spr.php";
        String snd = json_url + "set_sound.php";
        String lgt = json_url + "set_light.php";
        havetoDo = strings[0];
        String nm = strings[1];
        URL url = null;
        try {
            if(havetoDo.equals("light"))
            {
                url = new URL(lgt);
            }
            else if(havetoDo.equals("sound"))
            {
                url = new URL(snd);
            }
            else if(havetoDo.equals("spr_lvl"))
            {
                url = new URL(sprlvl);
            }
            assert url != null;
            System.out.println("sss :"+url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(nm, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            bufferedReader.close();
            is.close();
            httpURLConnection.disconnect();
            JSON_STRING = sb.toString().trim();
            System.out.println("lelele "+JSON_STRING);
            return JSON_STRING;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ererer:"+e);
        }
        return "agricom";
    }
    @Override
    protected void onPreExecute() {
        json_url = "http://agricoma.000webhostapp.com/appservice/";
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public interface AsyncResponse {
    void processFinish(String output);

}

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
