package com.example.bob.angrybird;

import java.util.Calendar;


public class RoundRobinTournament extends Tournament {

    public RoundRobinTournament(int _id, String name, String type, String status)
    {
        super(_id,name,type,status);
    }


    public void randomizeListing(int[] teamID, int tournamentID,DatabaseController databaseController){
        //Round  Robin  (League)
        //insert(int tournamentIndex, int teamIndex1, int teamIndex2, int teamScore1, int teamScore2, String time)
        int n =teamID.length;
        Calendar currentTime = Calendar.getInstance();
        //randomTeam(teamID);
        for (int i=0;i<n-1;i++){
            for(int j=i+1;j<=n-1;j++){
                currentTime.set(Calendar.DATE, currentTime.get(Calendar.DATE) + 2);
                String date=String.valueOf(currentTime.get(Calendar.DATE));//把时间换成String类型
                int d=currentTime.get(Calendar.DATE);
                if(d<10){
                    date="0"+String.valueOf(d);
                }

                String time= date+"/"+String.valueOf(currentTime.get(Calendar.MONTH)+1)+"/"+String.valueOf(currentTime.get(Calendar.YEAR));

                databaseController.insert(tournamentID,teamID[i],teamID[j],0,0,time);
            }
        }
    }

}
