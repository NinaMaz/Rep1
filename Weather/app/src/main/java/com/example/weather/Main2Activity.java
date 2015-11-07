package com.example.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView tView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tView = (TextView)findViewById(R.id.tView);
        Intent intent = getIntent();
        String weather = intent.getStringExtra("Weather");
        int day=intent.getIntExtra("Day",-1);
        tView.setText(String.valueOf(day));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

  /*  public String onJsonParse(String jsontext,String field,int day_num){
        String result="null";

        try{
            switch(field){
                case "description":
                    json_list=json.getJSONArray("list");
                    temp = json_list .getJSONObject(day_num);
                    json_list=temp.getJSONArray("weather");
                    result=json_list.getJSONObject(0).getString("main")+": "+json_list.getJSONObject(0).getString("description");
                    break;

            }
        }
        catch(Exception je){
            return "FIELD DOESNT MATCH";
        }

    }*/
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
}
