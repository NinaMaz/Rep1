package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFiles extends AsyncTask<Void,Integer,String> {
    protected String doInBackground(Void...params) {
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=ecf5219218accf2239f4c60d53c560ff");
            HttpURLConnection c = (HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            BufferedReader reader;
            reader=new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line=null;
            while ((line = reader.readLine())!=null){
                buf.append(line+"\n");
            }
            return buf.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Long result) {

    }
}
