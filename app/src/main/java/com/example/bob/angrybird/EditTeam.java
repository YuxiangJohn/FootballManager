package com.example.bob.angrybird;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditTeam extends AppCompatActivity {

    Team team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        Intent intent = getIntent();
        int tour_id = intent.getIntExtra("tour_id", 0);
        int _id = intent.getIntExtra("_id", 0);
        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        Cursor c = dbc.select("Team",""+tour_id,""+_id);
        c.moveToNext();
        team = new Team(_id,tour_id,c.getString(c.getColumnIndex("Name")),c.getString(c.getColumnIndex("Location")),0,c.getInt(c.getColumnIndex("Icon")));
        EditText nameText,locationText;
        nameText = (EditText)findViewById(R.id.nameText);
        locationText = (EditText)findViewById(R.id.locationText);
        nameText.setText(team.Name);
        String drawableName = "ic_logo_00";
        locationText.setText(team.Location);
        ImageView img = (ImageView)findViewById(R.id.imageView);
        switch (team.Icon) {
            case 0: drawableName = "ic_logo_00"; team.Icon=0; break;
            case 1: drawableName = "ic_logo_01"; team.Icon=1; break;
            case 2: drawableName = "ic_logo_02"; team.Icon=2; break;
            case 3: drawableName = "ic_logo_03"; team.Icon=3; break;
            case 4: drawableName = "ic_logo_04"; team.Icon=4; break;
            case 5: drawableName = "ic_logo_05"; team.Icon=5; break;
            default:
                drawableName = "ic_logo_00"; break;
        }
        int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        img.setImageResource(resID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_team, menu);
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

    public void editOnClick(View v)
    {
        EditText nameText,locationText;
        nameText = (EditText)findViewById(R.id.nameText);
        locationText = (EditText)findViewById(R.id.locationText);
        team.Name = nameText.getText().toString();
        team.Location = locationText.getText().toString();

        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        dbc.update(team._Id,team.TournamentIndex,team.Name,team.Location,team.Icon,team.Score);
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

    public void deleteOnClick(View v)
    {
        DatabaseController dbc = new DatabaseController(this,"Tournament_db",null,1);
        dbc.delete(team.TournamentIndex, team._Id);
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
