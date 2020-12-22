package com.example.pttesttracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class ScoreEntry {
    public ScoreEntry (){

    }

    public ScoreEntry(long timestamp, double score){
        this.timestamp = timestamp;
        this.score = score;
    }
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "score")
    public double score;

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");

        return sdf.format(new Date(timestamp));
    }

    public String getDebugString(){
            return "UID: " + uid + "TimeStamp: " + getDateString() + "Score: "+ String.valueOf(score);

        }

}
