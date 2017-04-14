package com.example.bob.angrybird;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddNewTournament extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tournament);

        findViewById(R.id.editText3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText tv = (EditText) v;
                    tv.setText("");
                } catch (Exception ex) {
                    ex.toString();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_tournament, menu);
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

    public void addOnClick(View v)
    {
        String type="RoundRobin";
        RadioButton r = (RadioButton)findViewById(R.id.radioButton);
        //选择比赛类型， radiobutton
        if(r.isChecked())
        {
            type = "RoundRobin";
        }
        r = (RadioButton)findViewById(R.id.radioButton2);
        if(r.isChecked())
        {
            type = "Knockout";
        }
        r = (RadioButton)findViewById(R.id.radioButton3);
        if(r.isChecked())
        {
            type = "Double";
        }
        DatabaseController dbc = new DatabaseController(this, "Tournament_db", null,1);
        EditText et = (EditText)findViewById(R.id.editText3);
        dbc.insert(et.getText().toString(), type );           //将信息插入表
        dbc.close();

        try {
            Intent intent = new Intent();
            intent.setClass(AddNewTournament.this, TournamentList.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            AddNewTournament.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }

}
