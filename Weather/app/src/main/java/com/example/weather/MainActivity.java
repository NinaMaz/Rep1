package com.example.weather;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    List<String> daystemp = new ArrayList<String>();
    String weather = "ppp";
    DBHelper dbHelper;
    SQLiteDatabase db;
    final Context context = this;
    String city_final;
    Boolean metric;
    Menu menu1;
    TextView tView;
    int flag;

    //String weather_item = "ppp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag=0;
        tView = (TextView)findViewById(R.id.textcity);
        initNavigationView();


        Intent intent = getIntent();
        city_final = intent.getStringExtra("city");

        metric = intent.getBooleanExtra("metric", true);

        if (city_final == null) {
            city_final = "Rostov-on-Don";
        }

        DownloadFiles d = new DownloadFiles();
        d.execute(city_final);


        try {
            weather = d.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        if (weather != null) {
            onFillDB(weather, db);
        }
            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToPosition(c.getCount()-5)) {
                int idCityIndex = c.getColumnIndex("city");
                int idDateIndex = c.getColumnIndex("date");
                int idTempIndex = c.getColumnIndex("temp");
                tView.setText(c.getString(idCityIndex));
                //daystemp.add(c.getString(idCityIndex));



                do {

                        daystemp.add(c.getString(idDateIndex) + "  " + c.getString(idTempIndex) + "\n");



                } while (c.moveToNext());
                c.close();
            }


       /* else
        {
            db = dbHelper.getWritableDatabase();
            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToPosition(c.getCount()-5)) {
                int idCityIndex = c.getColumnIndex("city");
                int idDateIndex = c.getColumnIndex("date");
                int idTempIndex = c.getColumnIndex("temp");
                daystemp.add(c.getString(idCityIndex));

                do {

                    daystemp.add(c.getString(idDateIndex) + "  " + c.getString(idTempIndex) + "\n");

                } while (c.moveToNext());
                c.close();
            }*/

        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        String[] days = new String[daystemp.size()];
        daystemp.toArray(days);
        ListViewAdapter adapter = new ListViewAdapter(this,days);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("day", position);
                startActivity(intent);


            }
        });
        lvMain.setAdapter(adapter);
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerlayout.closeDrawers();

                        int id = menuItem.getItemId();
                        if (id == R.id.city1) {
                            enterTheCity(menuItem);
                        }
                        if (id == R.id.metric) {
                            changeMetric(menuItem);
                        }

                        return true;
                    }
                });

        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.getItem(R.id.metric).setTitle("\u2109");
        // MenuItem item = menu.getItem(R.id.metric);
        //updateMenuTitles(menu);
        updateMenuTitles(navigationView.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();
        if (id == R.id.update){
            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
            mainIntent.putExtra("city", city_final);
            mainIntent.putExtra("metric", metric);

            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }


        return super.onOptionsItemSelected(item);
    }

    ;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public String onJsonParse(String jsontext, String field, int day_num) {
        String result = "null";
        try {
            JSONObject json = new JSONObject(jsontext);
            JSONArray json_list;
            JSONObject temp;

            switch (field) {
                case "city":
                    JSONObject json_city = json.getJSONObject("city");
                    result = json_city.getString("name") + ", " + json_city.getString("country");

                    break;
                case "date":

                    json_list = json.getJSONArray("list");
                    temp = json_list.getJSONObject(day_num);

                    result = temp.getString("dt_txt").substring(8, 10)+"."+temp.getString("dt_txt").substring(5, 7);
                    break;
                case "temp":
                    String symbol = "℃";
                    json_list = json.getJSONArray("list");
                    temp = json_list.getJSONObject(day_num);
                    result = temp.getJSONObject("main").getString("temp");
                    float tempc = (float) (Float.parseFloat(result) - 273);
                    if (metric == false) {
                        tempc = (float) ((Float.parseFloat(result) - 273) * 1.8 + 32);
                        symbol = "\u2109";
                    }
                    result = String.format("%.4s %s", Float.toString(tempc), symbol);
                    break;
                case "description":
                    json_list = json.getJSONArray("list");
                    temp = json_list.getJSONObject(day_num);
                    json_list = temp.getJSONArray("weather");
                    result = json_list.getJSONObject(0).getString("main") + ": " + json_list.getJSONObject(0).getString("description");
                    break;
                case "wind":
                    json_list = json.getJSONArray("list");
                    temp = json_list.getJSONObject(day_num);
                    result = temp.getJSONObject("wind").getString("speed") + "м/c";
                    break;
                case "icon":
                    json_list = json.getJSONArray("list");
                    temp = json_list.getJSONObject(day_num);
                    json_list = temp.getJSONArray("weather");
                    result = "http://openweathermap.org/img/w/" + json_list.getJSONObject(0).getString("icon") + ".png";
                    break;
            }
            return result;

        } catch (Exception je) {
            return "FIELD DOESNT MATCH";

        }


    }

    public void onFillDB(String weather, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        //db.delete("mytable", null, null);
        for (int i = 0; i <= 4; i++) {
            cv.put("city", onJsonParse(weather, "city", 0));
            cv.put("date", onJsonParse(weather, "date", 8 * i));
            cv.put("temp", onJsonParse(weather, "temp", 8 * i));
            cv.put("description", onJsonParse(weather, "description", 8 * i));
            cv.put("wind", onJsonParse(weather, "wind", 8 * i));
            cv.put("iconurl", onJsonParse(weather, "icon", 8 * i));
            db.insert("mytable", null, cv);
        }


    }
    private void updateMenuTitles(Menu menu) {
        MenuItem item = menu.findItem(R.id.metric);
        if (metric==true) {
            item.setTitle("\u2109");
        } else {
            item.setTitle("℃");
        }
    }

    public void enterTheCity(MenuItem item){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                city_final = userInput.getText().toString();

                                Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                                mainIntent.putExtra("city", city_final);
                                MainActivity.this.startActivity(mainIntent);

                                MainActivity.this.finish();

                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();

    }
    public void changeMetric(MenuItem item){
        if (metric == true) {
            metric = false;
            //item.setTitle("\u2109");
        } else {
            metric = true;
           // item.setTitle("℃");

        }
        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
        mainIntent.putExtra("city", city_final);
        mainIntent.putExtra("metric", metric);

        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();

    }
}