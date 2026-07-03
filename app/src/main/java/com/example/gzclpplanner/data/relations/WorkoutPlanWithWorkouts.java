package com.example.gzclpplanner.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gzclpplanner.data.entity.WorkoutPlanEntity;
import com.example.gzclpplanner.data.entity.WorkoutEntity;

import java.util.List;

public class WorkoutPlanWithWorkouts {
    @Embedded
    public WorkoutPlanEntity workoutPlan;

    @Relation(
            parentColumn = "id",
            entityColumn = "workoutPlanId"
    )
    public List<WorkoutEntity> workouts;
}
