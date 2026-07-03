package com.example.gzclpplanner.data.relations;

import androidx.room.*;

import com.example.gzclpplanner.data.entity.*;

import java.util.List;

public class WorkoutWithExercises {
    @Embedded
    public WorkoutEntity workout;

    @Relation(
            parentColumn = "id",
            entityColumn = "workoutId"
    )
    public List<ExerciseEntity> exercises;
}
