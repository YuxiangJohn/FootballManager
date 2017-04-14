package com.example.bob.angrybird;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TournamentInfo_Started extends AppCompatActivity {

    Tournament tnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info__started);


        try {
            Intent intent = getIntent();
            String extra = intent.getStringExtra("_id");
            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
            Cursor c = dbc.select("Tournament", extra, "%");
            c.moveToNext();
            TextView tvName, tvType;
            tvName = (TextView) findViewById(R.id.tournamentName);
            tvType = (TextView) findViewById(R.id.tournamentType);
            tvName.setText("Name : " + c.getString(c.getColumnIndex("Name")));
            tvType.setText("Type : " + c.getString(c.getColumnIndex("Type")));
            if (c.getString(c.getColumnIndex("Type")).equals("RoundRobin")) {
                tnm = new RoundRobinTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
            } else if (c.getString(c.getColumnIndex("Type")).equals("Knockout")) {
                tnm = new KnockoutTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
            } else {
                tnm = new DoubleTournament(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("Type")), c.getString(c.getColumnIndex("Status")));
            }
            c.close();
            dbc.close();

            setFrame();
        }

        catch(Exception ex)
        {
            ex.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournament_info__started, menu);
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


    public void setFrame()
    {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        layout.setOrientation(LinearLayout.VERTICAL);
        DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);

        Cursor c = dbc.select("Team", ""+tnm._Id, "%");
        c.moveToNext();
        Button[] btn = new Button[c.getCount()];
        for(int i =0;i<c.getCount();i++) {
            btn[i]=new Button(this);
            btn[i].setText(c.getString(c.getColumnIndex("Name")));
            btn[i].setId(c.getInt(c.getColumnIndex("_id")));
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), EditTeam.class); //Application Context and Activity
                        intent.putExtra("tour_id", tnm._Id);
                        intent.putExtra("_id",v.getId());
                        startActivityForResult(intent, 0);
                    } catch (Exception ex) {
                        String ex_s = ex.toString();
                    }
                }
            });
            layout.addView(btn[i]);
            c.moveToNext();
        }
        c.close();
    }

    public void deleteOnClick(View v)
    {
        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        dbc.delete(tnm._Id);
        dbc.close();
        try {
            Intent intent = new Intent();
            intent.setClass(TournamentInfo_Started.this, TournamentList.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            TournamentInfo_Started.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }

    public void playListingOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.putExtra("tour_id",tnm._Id);
            intent.setClass(TournamentInfo_Started.this, PlayListing.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            TournamentInfo_Started.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
    public void RankListingOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.putExtra("tour_id", tnm._Id);
            intent.setClass(TournamentInfo_Started.this, Rank.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            TournamentInfo_Started.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
    public void RetrunToTournmentListOnClick(View v)
    {
        try {
            Intent intent = new Intent();
            intent.setClass(TournamentInfo_Started.this, TournamentList.class);
            startActivity(intent);
            //如果不关闭当前的会出现好多个页面
            TournamentInfo_Started.this.finish();
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }
}
