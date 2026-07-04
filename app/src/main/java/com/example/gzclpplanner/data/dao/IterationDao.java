package com.example.gzclpplanner.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.gzclpplanner.data.entity.IterationEntity;

@Dao
public interface IterationDao {
    @Insert
    long insert(IterationEntity iteration);

    @Update
    void update(IterationEntity iteration);

    @Delete
    void delete(IterationEntity exercise);
}
