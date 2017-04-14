package com.example.bob.angrybird;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayListing extends AppCompatActivity {
    int tour_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_listing);

            Intent intent = getIntent();
        tour_id = intent.getIntExtra("tour_id", 0);

        setFrame();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_listing, menu);
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
            final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
            layout.setOrientation(LinearLayout.VERTICAL);

            final Button btnPlus = new Button(this);
            btnPlus.setText("Return to TournamentInfo");//回到联赛信息界面
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.putExtra("_id", "" + tour_id);
                        intent.setClass(PlayListing.this, TournamentInfo_Started.class);//从PlayListing跳转到TournamentInfo_Started
                        startActivity(intent);
                        //如果不关闭当前的会出现好多个页面
                        PlayListing.this.finish();
                    } catch (Exception ex) {
                        String ex_s = ex.toString();
                    }
                }
            });
            layout.addView(btnPlus);
            //这里是添加的东西开始下一轮比赛
          /*  final Button btnPlus1 = new Button(this);
            btnPlus1.setText("Start next Round");//回到联赛信息界面
            btnPlus1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.putExtra("_id", "" + tour_id);
                        intent.setClass(PlayListing.this, AddNextTournament.class);//从PlayListing跳转到TournamentInfo_Started
                        startActivity(intent);
                        //如果不关闭当前的会出现好多个页面
                        PlayListing.this.finish();
                    } catch (Exception ex) {
                        String ex_s = ex.toString();
                    }
                }
            });
            layout.addView(btnPlus1);
            //添加结束
            */

            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
            Cursor c = dbc.select("Play", "" + tour_id, "%");
            Cursor c_2;
            String text;
            c.moveToNext();
            //playing list生成对战表
            Button[] btn = new Button[c.getCount()];
            Log.i(String.valueOf(c.getCount()),"playlist");
            for (int i = 0; i < c.getCount(); i++) {Log.i("22222","22222222");
                btn[i] = new Button(this);
                c_2 = dbc.select("Team", "" + tour_id, "" + c.getInt(c.getColumnIndex("TeamIndex1")));
                c_2.moveToNext();
                text = c_2.getString(c_2.getColumnIndex("Name")) + " VS ";
                c_2 = dbc.select("Team", "" + tour_id, "" + c.getInt(c.getColumnIndex("TeamIndex2")));
                c_2.moveToNext();
                text = text + c_2.getString(c_2.getColumnIndex("Name")) + "    at    " + c.getString(c.getColumnIndex("Time"));
                if(c.getInt(c.getColumnIndex("TeamScore1")) < c.getInt(c.getColumnIndex("TeamScore2")))
                {
                    btn[i].setBackgroundColor(0xC0C0C0C0);
                }
                else if(c.getInt(c.getColumnIndex("TeamScore1")) >  c.getInt(c.getColumnIndex("TeamScore2")))
                {
                    btn[i].setBackgroundColor(0x99ffcc00);
                }
                else
                {
                    btn[i].setBackgroundColor(0x00000000);
                }
                btn[i].setId(c.getInt(c.getColumnIndex("_id")));
                btn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), EditPlay.class); //跳转到EditPlay
                            intent.putExtra("_id",v.getId());
                            intent.putExtra("tour_id",tour_id);
                            startActivityForResult(intent, 0);
                        } catch (Exception ex) {
                            String ex_s = ex.toString();
                        }
                    }
                });
                btn[i].setText(text);
                layout.addView(btn[i]);
                c.moveToNext();
            }
        }
        catch(Exception ex)
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
    public void NextRoundCreatOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.putExtra("_id", "" + tour_id);
            intent.setClass(PlayListing.this, AddNextTournament.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            PlayListing.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
   /* public void RankListOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.putExtra("tour_id",tnm._Id);
            intent.setClass(PlayListing.this, Rank.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            PlayListing.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }*/
}
