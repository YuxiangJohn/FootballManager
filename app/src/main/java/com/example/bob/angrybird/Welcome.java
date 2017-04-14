package com.example.bob.angrybird;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);//跳转到layout welcome
        DatabaseController dbc= new DatabaseController(this,"Tournament_db",null,1);//初始化
        try {

        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

    public void startOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.setClass(Welcome.this, TournamentList.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            Welcome.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
}
