package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
public class Main2Activity extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    SQLiteDatabase db;
    TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tView = (TextView)findViewById(R.id.tView);
        Intent intent = getIntent();
        int day=intent.getIntExtra("day",-1);
        //tView.setText(String.valueOf(day));
        db= dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if(c.moveToPosition(day)){
            int idDescIndex = c.getColumnIndex("description");
            int idWindIndex = c.getColumnIndex("wind");
            int idIconIndex = c.getColumnIndex("iconurl");
            tView.setText(c.getString(idDescIndex) + "\n"+c.getString(idWindIndex)+"\n");
            ImageView image = (ImageView)findViewById(R.id.iView);
            Glide.with(this)
                    .load(c.getString(idIconIndex))
                    .into(image);
        }
        c.close();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
