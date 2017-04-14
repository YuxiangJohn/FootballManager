package com.example.bob.angrybird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CountDownLatch;


public class DatabaseController extends SQLiteOpenHelper {

    public DatabaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    //首次创建数据库时调用，执行建库建表操作
    public void onCreate(SQLiteDatabase db) {
        //String sql_newTournament="CREATE TABLE Tournament(Index INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL,Type TEXT NOT NULL, Status INTEGER DEFAULT '0' NOT NULL)";
       //创建表Tournament，联赛表
        String sql_newTournament="CREATE TABLE Tournament(_id INTEGER NOT NULL PRIMARY KEY, Name TEXT NOT NULL,Type TEXT NOT NULL, Status INTEGER DEFAULT '0' NOT NULL)";
        //创建表Team，队伍表
        String sql_newTeam="CREATE TABLE Team(_id INTEGER NOT NULL ,TournamentIndex INTEGER NOT NULL , Name TEXT NOT NULL, Location TEXT NOT NULL, Icon INTEGER, Score INTEGER NOT NULL DEFAULT '0', PRIMARY KEY(_id,TournamentIndex))";
        //创建表Play,对阵表
        String sql_newPlay="CREATE TABLE Play(_id INTEGER NOT NULL ,TournamentIndex INTEGER NOT NULL , TeamIndex1 INTEGER NOT NULL, TeamIndex2 INTEGER NOT NULL, Time TEXT NOT NULL, TeamScore1 INTEGER, TeamScore2 INTEGER,PRIMARY KEY(_id,TournamentIndex))";
        try {
            db.execSQL(sql_newTournament); //Create new table for tournaments, teams and plays. If there's alredy a database or a table, ignore it.
            db.execSQL(sql_newTeam);
            db.execSQL(sql_newPlay);
        }
        catch(Exception ex)
        {
            String ex_s = ex.toString();
        }
    }

    @Override
    //更数据库版本发生变化时自动执行
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE tb_test";
        db.execSQL(sql);
        onCreate(db);
    }

    /**
     * To insert a record into table 'Tournament'.
     * @param name The name of the Tournament.
     * @param type The type of the Tournament.
     */
    public void insert(String name, String type)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        String sql_count = "SELECT * FROM Tournament";
        Cursor dataCount = db.rawQuery(sql_count,null);
        int index;
        if(dataCount.getCount()!=0) {
            sql_count = "SELECT MAX(_id) FROM Tournament";
            dataCount = db.rawQuery(sql_count,null);
            dataCount.moveToNext();
            index = dataCount.getInt(0) + 1;
        }
        else
        {
            index =1;
        }
        dataCount.close();
        data.put("Name", name);//联赛名称
        data.put("Type", type);//联赛类型
        data.put("Status", 0);//联赛状态
        long i = db.insert("Tournament", null, data);
        db.close();
    }

    /**
     * To insert a record into table 'Team'.
     * @param tournamentIndex The index of the target tournament
     * @param name The name of the team
     * @param location The location of the team
     * @param icon The icon index of the team
     * @param score The total score of the team in this tournament
     */

    public void insert(int tournamentIndex, String name, String location, int icon, int score)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_count = "SELECT * FROM Team WHERE TournamentIndex = '"+tournamentIndex+"'";
        Cursor dataCount = db.rawQuery(sql_count,null);
        int index;
        if(dataCount.getCount()!=0) {
            sql_count = "SELECT MAX(_id) FROM Team WHERE TournamentIndex = '" + tournamentIndex + "'";
            dataCount = db.rawQuery(sql_count,null);
            dataCount.moveToNext();
            index = dataCount.getInt(0) + 1;
        }
        else
        {
             index =1;
        }
        dataCount.close();
        ContentValues data = new ContentValues();
        data.put("_id",index);
        data.put("TournamentIndex", tournamentIndex);
        data.put("Name", name);
        data.put("Location", location);
        data.put("Icon",icon);
        data.put("Score",score);
        db.insert("Team", null, data);
        db.close();
    }

    /**
     * To insert a record into table 'Play'.
     * @param tournamentIndex The index of the target tournament
     * @param teamIndex1 The index of the first team
     * @param teamIndex2 The index of the second team
     * @param teamScore1 the score of the first team
     * @param teamScore2 The score of the second team
     * @param time The time of the play
     */
    public void insert(int tournamentIndex, int teamIndex1, int teamIndex2, int teamScore1, int teamScore2, String time) {
        SQLiteDatabase db = getWritableDatabase();
        String sql_count = "SELECT * FROM Play WHERE TournamentIndex = '"+tournamentIndex+"'";
        Cursor dataCount = db.rawQuery(sql_count,null);
        int index;
        if(dataCount.getCount()!=0) {
            sql_count = "SELECT MAX(_id) FROM Play WHERE TournamentIndex = '" + tournamentIndex + "'";
            dataCount = db.rawQuery(sql_count,null);
            dataCount.moveToNext();
            index = dataCount.getInt(0) + 1;
        }
        else
        {
            index =1;
        }
        dataCount.close();
        ContentValues data = new ContentValues();
        data.put("_id",index);
        data.put("TournamentIndex", tournamentIndex);
        data.put("TeamIndex1",teamIndex1);
        data.put("TeamIndex2",teamIndex2);
        data.put("TeamScore1",teamScore1);
        data.put("TeamScore2",teamScore2);
        data.put("Time",time);
        db.insert("Play", null, data);
        db.close();
    }
//删除队
    public void delete(int tournamentIndex, int teamIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_delete = "DELETE FROM Team WHERE TournamentIndex = ' " + tournamentIndex + " ' AND _id = ' " + teamIndex + " '";
        db.execSQL(sql_delete);
        db.close();
    }
//删除联赛
    public void delete(int tournamentIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_deleteTeam = "DELETE FROM Team WHERE TournamentIndex = ' " + tournamentIndex + " ' ";//删除联赛中的队
        String sql_deletePlay = "DELETE FROM Play WHERE TournamentIndex = ' " + tournamentIndex + " ' ";//删除play
        String sql_deleteTournament = "DELETE FROM Tournament WHERE _id = ' " + tournamentIndex + " ' ";//删除联赛
        db.execSQL(sql_deleteTeam);
        db.execSQL(sql_deletePlay);
        db.execSQL(sql_deleteTournament);
        db.close();
    }
//更新队
    public void update(int _id,int tournamentIndex, String name, String location, int icon, int score)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_select = "SELECT * FROM Team WHERE _id = '"+_id+"' AND TournamentIndex = '"+tournamentIndex+"'";
        Cursor originalData = db.rawQuery(sql_select,null);
        originalData.moveToNext();
        if(name.equals(""))
        {
            name = originalData.getString(originalData.getColumnIndex("Name"));
        }
        if(location.equals(""))
        {
            location = originalData.getString(originalData.getColumnIndex("Location"));
        }
        if(icon == 0)
        {
            icon = originalData.getInt(originalData.getColumnIndex("Icon"));
        }
        if(score == 0) {

            score = originalData.getInt(originalData.getColumnIndex("Score"));
        }
        else if(score ==-1){
            score=0;
        }
        else {
            score=score+originalData.getInt(originalData.getColumnIndex("Score"));
        }
        String sql_update = "UPDATE Team Set Name = '" + name + "', Location = '"+location+"', Icon = '"+icon+"', Score = '"+score+"' WHERE _id = '"+_id+"' AND TournamentIndex = '"+tournamentIndex+"' ";
        db.execSQL(sql_update);
        db.close();
    }
//更新play
    public void update(int _id,int tournamentIndex, int teamScore1, int teamScore2)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_update = "UPDATE Play Set TeamScore1 = '" + teamScore1 + "', TeamScore2 = '"+teamScore2+"' WHERE _id = '"+_id+"' AND TournamentIndex = '"+tournamentIndex+"' ";
        db.execSQL(sql_update);
        db.close();
    }
//更新联赛状态
    public void update(int _id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql_update = "UPDATE Tournament Set Status = '1' WHERE _id = '"+_id+"'";
        db.execSQL(sql_update);
        db.close();
    }

    public Cursor select(String tableName, String tournamentIndex, String teamOrPlayIndex)
    {
        SQLiteDatabase db = getWritableDatabase();
                String selection = null;
                Cursor c;
                if(tableName.equals("Tournament")) {
                    if(!tournamentIndex.equals("%"))
                    {
                selection = "_id = " + tournamentIndex;
            }
            c = db.query("Tournament", new String[]{"_id", "Name", "Type", "Status"}, selection, null, null, null, "_id desc");//查询表Tournament
        }
        else if (tableName.equals("Team"))
        {
            if(!tournamentIndex.equals("%"))
            {
                selection = "TournamentIndex = " + tournamentIndex;
            }
            if(!teamOrPlayIndex.equals("%"))
            {
                selection += " AND _id = " + teamOrPlayIndex;
            }
            c= db.query("Team", new String[]{"_id", "TournamentIndex", "Name", "Location", "Icon", "Score"}, selection, null, null, null, "_id desc");//查询表Team
        }
        else {
            if(!tournamentIndex.equals("%"))
            {
                selection = "TournamentIndex = " + tournamentIndex;
            }
            if(!teamOrPlayIndex.equals("%"))
            {
                selection += " AND _id = " + teamOrPlayIndex;
            }
            c= db.query("Play", new String[]{"_id", "TournamentIndex", "TeamIndex1", "TeamIndex2", "Time", "TeamScore1", "TeamScore2"}, selection , null, null, null, "_id desc");//查询表Play
        }
        return  c;
    }


    public Cursor query( String tournamentIndex)
    {

        SQLiteDatabase db = getWritableDatabase();
        String selection =null;
        Cursor c;
        if(!tournamentIndex.equals("%"))
        {
            selection = "TournamentIndex = " + tournamentIndex;
        }
        c= db.query("Team", new String[]{"_id", "TournamentIndex", "Name", "Location", "Icon", "Score"}, selection, null, null, null, "Score desc");//查询表Team
        Log.i("xunwen","xunwen");
        return c;
    }



}
