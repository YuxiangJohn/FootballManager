package com.example.bob.angrybird;

/**
 * Created by Jiangyuxiang on 2016/2/27.
 */
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class Rank extends AppCompatActivity{
    int tour_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rank_listing);

        Intent intent = getIntent();
        tour_id = intent.getIntExtra("tour_id", 0);
        clear();
        calculate();
        setFrame();
        Log.i("lll","lll");

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rank_listing, menu);
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
    public void setFrame() {
        try {
            final LinearLayout layout2 = (LinearLayout) findViewById(R.id.layoutrank);
            layout2.setOrientation(LinearLayout.VERTICAL);
            setContentView(layout2);
            final Button btnPlus = new Button(this);
            btnPlus.setText("Return to TournamentInfo");//回到联赛信息界面
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.putExtra("_id", "" + tour_id);
                        intent.setClass(Rank.this, TournamentInfo_Started.class);//从PlayListing跳转到TournamentInfo_Started
                        startActivity(intent);
                        //如果不关闭当前的会出现好多个页面
                        Rank.this.finish();
                    } catch (Exception ex) {
                        String ex_s = ex.toString();
                    }
                }
            });
            layout2.addView(btnPlus);

            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
           //Cursor c = dbc.select("Team", "" + tour_id, "%");
           Cursor c= dbc.query("" + tour_id);
            Cursor c_2;
            String text;
            c.moveToNext();
            //Rank表
            TextView[] view = new TextView[c.getCount()];
            for (int i = 0; i <c.getCount(); i++) {
                view[i] = new TextView(this);
               // c_2=dbc.select("Team", "" + tour_id, "%");
              //  c_2.moveToNext();
                text=String.valueOf(i+1)+"  ";      Log.i(String.valueOf(i+1),String.valueOf(i+1));
                text=text+c.getString(c.getColumnIndex("Name"))+"  Score:  ";
                text=text+String.valueOf(c.getInt(c.getColumnIndex("Score")));
                view[i].setBackgroundColor(0xC0C0C0C0);

                view[i].setId(c.getInt(c.getColumnIndex("_id")));
                view[i].setText(text);
                view[i].setTextSize(20);
                layout2.addView(view[i]);

                c.moveToNext();

            }

        }
        catch(Exception ex)
        {
            ex.toString();
        }


    }
    public void clear() {
        try {
            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
            Cursor c = dbc.select("Play", "" + tour_id, "%");
            Cursor c_2;
            c.moveToNext();
            int sum;
            for (int i = 0; i < c.getCount(); i++)
            {
                    dbc.update(c.getInt(c.getColumnIndex("TeamIndex1")),tour_id,"","",0,-1); //球队1 打平1分
                    dbc.update(c.getInt(c.getColumnIndex("TeamIndex2")),tour_id,"","",0,-1); //球队2 打平1分

                c.moveToNext();
            }
        }
        catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void calculate() {
       try {
           DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
           Cursor c = dbc.select("Play", "" + tour_id, "%");
           Cursor c_2;
           c.moveToNext();
           int sum;
           for (int i = 0; i < c.getCount(); i++)
           {
               if(c.getInt(c.getColumnIndex("TeamScore1")) < c.getInt(c.getColumnIndex("TeamScore2")))
               {
                  dbc.update(c.getInt(c.getColumnIndex("TeamIndex1")), tour_id, "", "", 0, 0); //球队1 输积0分
                   dbc.update(c.getInt(c.getColumnIndex("TeamIndex2")),tour_id,"","",0,3); //球队2 赢积3分
               }
               else if(c.getInt(c.getColumnIndex("TeamScore1")) >  c.getInt(c.getColumnIndex("TeamScore2")))
               {
                   dbc.update(c.getInt(c.getColumnIndex("TeamIndex1")),tour_id,"","",0,3); //球队1 赢积3分
                   dbc.update(c.getInt(c.getColumnIndex("TeamIndex2")),tour_id,"","",0,0); //球队2 赢积0分
               }
               else
               {
                   dbc.update(c.getInt(c.getColumnIndex("TeamIndex1")),tour_id,"","",0,1); //球队1 打平1分
                   dbc.update(c.getInt(c.getColumnIndex("TeamIndex2")),tour_id,"","",0,1); //球队2 打平1分
               }
               c.moveToNext();
           }
       }
       catch (Exception ex)
       {
           ex.toString();
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return;
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, 0, 0, 0);
        layout.removeAllViews();

        setFrame();

    }
}
