package com.example.pttesttracker;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pttesttracker.ScoreEntry;

@Dao
public interface ScoreEntryDao {
    @Query("Select * from ScoreEntry")
    List<ScoreEntry> getAll();

    @Insert
    void insertAll(ScoreEntry ... entries);

    @Delete
    void delete(ScoreEntry entries);

    @Query("Delete FROM ScoreEntry")
    void deleteAll();

}
