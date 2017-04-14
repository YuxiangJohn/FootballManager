package com.example.bob.angrybird;

import java.util.Calendar;


public class DoubleTournament extends Tournament {
    public DoubleTournament(int _id, String name, String type, String status)
    {
        super(_id,name,type,status);
    }

    public void  randomizeListing(int[] teamID, int tournamentID,DatabaseController databaseController){
        int n =teamID.length;
        int n1=(int)Math.floor(n/2);
        int n2 = n-n1;
        int[] teamID1= new int[n1];
        int[] teamID2 =new int[n2];
        for (int i=0;i<n1;i++){
            teamID1[i]=teamID[i];

        } arrangedPlaysForLeague(teamID1, tournamentID, databaseController);
        for(int i=0;i<n2;i++){
            teamID2[i]=teamID[i+n1];
        }            arrangedPlaysForLeague(teamID2, tournamentID, databaseController);

    }

    public void arrangedPlaysForLeague(int[] teamID, int tournamentID,DatabaseController databaseController){
        //Round  Robin  (League)
        //insert(int tournamentIndex, int teamIndex1, int teamIndex2, int teamScore1, int teamScore2, String time)
        int n =teamID.length;
        Calendar currentTime = Calendar.getInstance();
        //randomTeam(teamID);
        for (int i=0;i<n-1;i++){
            for(int j=i+1;j<=n-1;j++){
                currentTime.set(Calendar.DATE, currentTime.get(Calendar.DATE) + 1);
                String date=String.valueOf(currentTime.get(Calendar.DATE));
                int d=currentTime.get(Calendar.DATE);
                if(d<10){
                    date="0"+String.valueOf(d);
                }

                String time= date+String.valueOf(currentTime.get(Calendar.MONTH))+String.valueOf(currentTime.get(Calendar.YEAR));

                databaseController.insert(tournamentID,teamID[i],teamID[j],0,0,time);
            }
        }
    }
}
