package com.example.gzclpplanner.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.gzclpplanner.data.entity.IterationEntity;

public interface IterationDao {
    @Insert
    void insert(IterationEntity iteration);

    @Update
    void update(IterationEntity iteration);

    @Delete
    void delete(IterationEntity exercise);
}
