package com.example.bob.angrybird;

import android.content.Context;
import android.view.View;
public abstract class Tournament {

    public int _Id;
    public String Name;
    public String Type;
    public String Status;
    public Tournament(int _id, String name, String type, String status)
    {
        _Id = _id;
        Name = name;
        Type = type;
        Status = status;
    }

    public void addTeam(Context ct,String teamName, String location, int icon)
    {
        if(Status.equals("0")) {
            DatabaseController dbc = new DatabaseController(ct, "Tournament_db", null, 1);
            dbc.insert(_Id, teamName, location, icon, 0);
            dbc.close();
        }
    }

    public void start(Context ct)
    {
        if(Status.equals("0"))
        {
            DatabaseController dbc = new DatabaseController(ct, "Tournament_db", null, 1);
            dbc.update(_Id);
            dbc.close();
            Status = "1";
        }
    }

    public abstract void randomizeListing(int[] teamID, int tournamentID,DatabaseController databaseController);

}
