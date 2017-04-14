package com.example.bob.angrybird;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class TournamentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_list);
        getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournament_list, menu);
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

    public void getList()
    {
        try {
            final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(0, 0, 0, 0);
            //setContentView(layout);
            final Button btnPlus = new Button(this);
            btnPlus.setText("+ (Add a New Tournament)");
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(TournamentList.this, AddNewTournament.class);
                        startActivity(intent);
                        TournamentList.this.finish();
                    } catch (Exception ex) {
                        String ex_s = ex.toString();
                    }
                }
            });
            layout.addView(btnPlus);


            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
            Cursor c = dbc.select("Tournament", "%", "%");
            int count = c.getCount();//总数据项数
            final Button[] btn;
            btn = new Button[count];
            c.moveToNext();
            for (int i = 0; i < count; i++) {
                btn[i] = new Button(this);
                if(c.getString(c.getColumnIndex("Status")).equals("0"))
                {
                    btn[i].setBackgroundColor(0x99ffcc00); //黄色
                }
                else
                {
                    btn[i].setBackgroundColor(0xC0C0C0C0);//灰色
                }
                btn[i].setText(c.getString(c.getColumnIndex("Name"))+"("+c.getString(c.getColumnIndex("Type"))+")");
                btn[i].setId(c.getInt(c.getColumnIndex("_id")));
                btn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Button btn = (Button) v;
                            DatabaseController dbc = new DatabaseController(v.getContext(), "Tournament_db", null, 1);
                            Cursor c = dbc.select("Tournament", "" + (v.getId()), "%");
                            c.moveToNext();
                            //if (c.getString(c.getColumnIndex("Type")).equals("RoundRobin")) {
                            //   RoundRobinTournament tnm = new RoundRobinTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
                            //} else if (c.getString(c.getColumnIndex("Type")).equals("Knockout")) {
                            //    KnockoutTournament tnm = new KnockoutTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
                            //} else {
                            //    DoubleTournament tnm = new DoubleTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
                            //}
                            try {
                                Intent intent = new Intent();
                                intent.putExtra("_id", "" + v.getId());
                                if (c.getString(c.getColumnIndex("Status")).equals("0")) {
                                    intent.setClass(TournamentList.this, TournamentInfo.class);
                                } else {
                                    intent.setClass(TournamentList.this, TournamentInfo_Started.class);
                                }
                                startActivity(intent);
                                TournamentList.this.finish();
                            } catch (Exception ex) {
                                String ex_s = ex.toString();
                            }
                        } catch (Exception ex) {
                            String i = ex.toString();
                        }
                    }
                });
                layout.addView(btn[i]);
                c.moveToNext();
            }
            c.close();
            dbc.close();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
}
