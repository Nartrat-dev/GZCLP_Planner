package com.example.gzclpplanner.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gzclpplanner.data.entity.ExerciseEntity;
import com.example.gzclpplanner.data.entity.IterationEntity;

public class ExerciseWithIteration {
    @Embedded
    public ExerciseEntity exercise;

    @Relation(
            parentColumn = "currentIterationId",
            entityColumn = "id"
    )
    public IterationEntity iteration;
}
