package com.example.gzclpplanner.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gzclpplanner.data.entity.CycleEntity;
import com.example.gzclpplanner.data.entity.ExerciseEntity;

import java.util.List;

public class ExerciseWithCycles {
    @Embedded
    public ExerciseEntity exercise;

    @Relation(
            parentColumn = "id",
            entityColumn = "cycleId"
    )
    public List<CycleEntity> cycles;
}
