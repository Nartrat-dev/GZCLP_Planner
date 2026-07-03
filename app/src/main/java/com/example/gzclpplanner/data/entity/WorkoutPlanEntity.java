package com.example.gzclpplanner.data.entity;
import androidx.room.*;

@Entity(tableName = "workout_plan")
public class WorkoutPlanEntity {
    @PrimaryKey
    public int id;

    // Attributes of workout_plan
    public int current_workout;
}
