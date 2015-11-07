package com.example.weather;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    List<String> daystemp = new ArrayList<String>();
    String weather = "ppp";
    DBHelper dbHelper;
    SQLiteDatabase db;

    //String weather_item = "ppp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        DownloadFiles d = new DownloadFiles();
        d.execute();


        try {
            weather = d.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (weather != null) {
            db= dbHelper.getWritableDatabase();
            onFillDB(weather, db);
            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToFirst()) {
                int idCityIndex = c.getColumnIndex("city");
                int idDateIndex = c.getColumnIndex("date");
                int idTempIndex = c.getColumnIndex("temp");
                int idDescIndex = c.getColumnIndex("description");
                daystemp.add(c.getString(idCityIndex));

                do {

                    daystemp.add(c.getString(idDateIndex)+"\n"+c.getString(idTempIndex)+"\n"+c.getString(idDescIndex));

                }while(c.moveToNext());
                c.close();
            }

        }
            ListView lvMain = (ListView) findViewById(R.id.lvMain);
            String[] days = new String[daystemp.size()];
            daystemp.toArray(days);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
            lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);

                }
            });
            lvMain.setAdapter(adapter);
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount()>0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    public String onJsonParse(String jsontext,String field,int day_num){
        String result="null";
        try{
            JSONObject json = new JSONObject(jsontext);
            JSONArray json_list;
            JSONObject temp;

            switch(field){
                case "city":
                    JSONObject json_city=json.getJSONObject("city");
                    result=json_city.getString("name")+", "+json_city.getString("country");

                    break;
                case "date":

                    json_list=json.getJSONArray("list");
                    temp = json_list .getJSONObject(day_num);

                    result=temp.getString("dt_txt").substring(0,10);
                    break;
                case "temp":
                    json_list=json.getJSONArray("list");
                    temp = json_list .getJSONObject(day_num);
                    result=temp.getJSONObject("main").getString("temp");
                    float tempc= (float) (Float.parseFloat(result) - 273);
                    result=Float.toString(tempc)+" \u2103";
                    break;
                case "description":
                    json_list=json.getJSONArray("list");
                    temp = json_list .getJSONObject(day_num);
                    json_list=temp.getJSONArray("weather");
                    result=json_list.getJSONObject(0).getString("main")+": "+json_list.getJSONObject(0).getString("description");
                    break;
                case "wind":
                    
                    break;
            }
            return result;

        }
        catch(Exception je){
            return "FIELD DOESNT MATCH";

        }



    }
    public void onFillDB(String weather,SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        db.delete("mytable", null, null);
        for (int i = 0; i <= 2; i++) {
            cv.put("city", onJsonParse(weather, "city", 0));

            cv.put("date", onJsonParse(weather, "date", 8*i));
            cv.put("temp",onJsonParse(weather,"temp",8*i));
           // cv.put("description", onJsonParse(weather, "description", 8*i));

            db.insert("mytable", null, cv);
        }


    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table mytable ("
                    + "city,"
                    + "date,"
                    + "temp,"
                    + "description,"
                    + "wind,"
                    + "iconurl" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
