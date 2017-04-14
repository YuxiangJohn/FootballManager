package com.example.bob.angrybird;

import java.util.Calendar;


public class KnockoutTournament extends Tournament {

    public KnockoutTournament(int _id, String name, String type, String status)
    {
        super(_id,name,type,status);
    }

    public void randomizeListing(int[] teamID, int tournamentID,DatabaseController databaseController){
        int n =teamID.length;
        int j;
        Calendar currentTime = Calendar.getInstance();
        for (int i=0;i<Math.floor(n/2);i++){
            currentTime.set(Calendar.DATE, currentTime.get(Calendar.DATE) + 2);
            j=n-i-1;
            String date=String.valueOf(currentTime.get(Calendar.DATE));
            int d=currentTime.get(Calendar.DATE);
            if(d<10){
                date="0"+String.valueOf(d);
            }
            String time= date+"/"+String.valueOf(currentTime.get(Calendar.MONTH)+1)+"/"+String.valueOf(currentTime.get(Calendar.YEAR));
            databaseController.insert(tournamentID,teamID[i],teamID[j],0,0,time);
        }
    }
}
