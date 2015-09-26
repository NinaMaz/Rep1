package com.masstersoft.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initNavigationView();
    }

    private void initNavigationView() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);

    }

    private void initToolBar() {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(R.string.app_name);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolBar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolBar);
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
        switch (id){
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_layout_test:
                Intent intent=new Intent(MainActivity.this,LayoutTest.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                Toast.makeText(MainActivity.this, "You clicked Help", Toast.LENGTH_SHORT).show();
                return true;

           /* case R.id.action_layout_test1:
                Intent intent1=new Intent(MainActivity.this,LayoutTest.class);
                startActivity(intent1);*/



        }


        return super.onOptionsItemSelected(item);
    }
}
