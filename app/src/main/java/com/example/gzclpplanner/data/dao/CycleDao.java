package com.example.gzclpplanner.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.gzclpplanner.data.entity.CycleEntity;

public interface CycleDao {
    @Insert
    void insert(CycleEntity cycle);

    @Update
    void update(CycleEntity cycle);

    @Delete
    void delete(CycleEntity cycle);
}
