package com.example.gzclpplanner.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gzclpplanner.data.entity.CycleEntity;
import com.example.gzclpplanner.data.entity.IterationEntity;
import com.example.gzclpplanner.data.relations.CycleWithIterations;

import java.util.List;

public interface CycleDao {
    @Insert
    long insertCycle(CycleEntity cycle);

    @Update
    void updateCycle(CycleEntity cycle);

    @Delete
    void deleteCycle(CycleEntity cycle);

    @Insert
    List<Long> insertIterations(List<IterationEntity> iterations);

    @Transaction
    @Query("SELECT * FROM cycle WHERE id = :cycleId")
    CycleWithIterations getCycleWithIterations(int cycleId);
}
