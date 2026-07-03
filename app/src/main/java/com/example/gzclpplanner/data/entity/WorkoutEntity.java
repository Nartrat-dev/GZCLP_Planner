package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(
        tableName = "workout",
        foreignKeys = @ForeignKey(
                entity = WorkoutPlanEntity.class,
                parentColumns = "id",
                childColumns = "workoutPlanId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("workoutPlanId")}
)
public class WorkoutEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;


    // Foreign keys
    public int workoutPlanId;

    // Attributes of workout
    public String workoutName;
    public int workoutNumber;

}
