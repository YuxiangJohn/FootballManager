package com.example.bob.angrybird;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddNewTeam extends AppCompatActivity {

    Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_team);

        Intent intent = getIntent();
        int tour_id = intent.getIntExtra("tour_id",0);
        team = new Team(0,tour_id,"","",0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_team, menu);
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

    public void OnSetAvatarButton(View view) {
        Intent intent = new Intent(getApplicationContext(), ChooseIcon.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return; //Getting the Avatar Image we show to our users
        ImageView avatarImage = (ImageView) findViewById(R.id.imageView);
//Figuring out the correct image
        String drawableName = "ic_logo_00";
        switch (data.getIntExtra("imageID",R.id.teamid00)) {
            case R.id.teamid00: drawableName = "ic_logo_00"; team.Icon=0; break;
            case R.id.teamid01: drawableName = "ic_logo_01"; team.Icon=1; break;
            case R.id.teamid02: drawableName = "ic_logo_02"; team.Icon=2; break;
            case R.id.teamid03: drawableName = "ic_logo_03"; team.Icon=3; break;
            case R.id.teamid04: drawableName = "ic_logo_04"; team.Icon=4; break;
            case R.id.teamid05: drawableName = "ic_logo_05"; team.Icon=5; break;
            default:
                drawableName = "ic_logo_00"; break;
        }
        int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName()); avatarImage.setImageResource(resID);
    }
//下面是添加google map的部分

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

    public void addOnClick(View v)
    {
        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        EditText name,location;
        name = (EditText)findViewById(R.id.nameText);
        location = (EditText)findViewById(R.id.locationText);
        dbc.insert(team.TournamentIndex,name.getText().toString(),location.getText().toString(),team.Icon,team.Score);
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
