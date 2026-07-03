package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(
        tableName = "workout",
        foreignKeys = @ForeignKey(
                entity = WorkoutPlanEntity.class,
                parentColumns = "id",
                childColumns = "workout_plan_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("workout_plan_id")}
)
public class WorkoutEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;


    // Foreign keys
    public int workout_plan_id;

    // Attributes of workout
    public String workout_name;
    public int workout_number;

}
