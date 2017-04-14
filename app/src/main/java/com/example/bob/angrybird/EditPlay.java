package com.example.bob.angrybird;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditPlay extends AppCompatActivity {

    int _id;
    int tour_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_play);
        try {
            Intent intent = getIntent();
            _id = intent.getIntExtra("_id", 0);
            tour_id = intent.getIntExtra("tour_id", 0);
            TextView tv = (TextView) findViewById(R.id.vstext);
            DatabaseController dbc = new DatabaseController(this, "Tournament_db", null, 1);
            String text;
            Cursor c = dbc.select("Play", "" + tour_id, _id + "");
            c.moveToNext();
            //////////////////////////////////////////////////////////////////
            EditText s1,s2;
            s1 = (EditText)findViewById(R.id.score1);
            s2 = (EditText)findViewById(R.id.score2);
            s1.setText(String.valueOf(c.getInt(c.getColumnIndex("TeamScore1"))));
            s2.setText(String.valueOf(c.getInt(c.getColumnIndex("TeamScore2"))));
            //////////////////////////////////////////////////////////////////
            Cursor c_2 = dbc.select("Team", "" + tour_id, "" + c.getInt(c.getColumnIndex("TeamIndex1")));
            c_2.moveToNext();
            text = c_2.getString(c_2.getColumnIndex("Name")) + " VS ";
            c_2 = dbc.select("Team", "" + tour_id, "" + c.getInt(c.getColumnIndex("TeamIndex2")));
            c_2.moveToNext();
            text += c_2.getString(c_2.getColumnIndex("Name"));
            tv.setText(text);
            c_2.close();
            c.close();
            dbc.close();
        }
        catch (Exception ex) {
            ex.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_play, menu);
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
    public void OnOpenInGoogleMaps (View view) {
        EditText teamAddres = (EditText) findViewById(R.id.locationText);
// Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q=" + teamAddres.getText());
// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }
    public void editOnClick(View v)
    {
        EditText s1,s2;
        s1 = (EditText)findViewById(R.id.score1);
        s2 = (EditText)findViewById(R.id.score2);
        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        dbc.update(_id, tour_id, Integer.parseInt(s1.getText().toString()), Integer.parseInt(s2.getText().toString()));
        dbc.close();

        try {
            //Creating a Return intent to pass to the Main Activity
            Intent returnIntent = new Intent();
//Figuring out which image was clicked
            setResult(RESULT_OK, returnIntent);
//Finishing Activity and return to main screen!
            finish();
        } catch (Exception ex) {
            String ex_s = ex.toString();
        }
    }


}
