package com.example.bob.angrybird;

public class Team {
    public int _Id;
    public int TournamentIndex;
    public String Name;
    public String Location;
    public int Score;
    public int Icon;
    public Team(int _id, int tournamentIndex, String name, String location, int score, int icon)
    {
        _Id = _id;
        TournamentIndex = tournamentIndex;
        Name = name;
        Location = location;
        Score = score;
        Icon = icon;
    }


}
