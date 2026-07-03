package com.example.gzclpplanner.data.entity;
import androidx.room.*;

@Entity(tableName = "workoutPlan")
public class WorkoutPlanEntity {
    @PrimaryKey
    public int id;

    // Attributes of workoutPlan
    public int currentWorkout;
}
