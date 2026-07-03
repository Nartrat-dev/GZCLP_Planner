package com.example.gzclpplanner.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(
        tableName = "exercise",
        foreignKeys = {
        @ForeignKey(
                entity = WorkoutEntity.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = ForeignKey.CASCADE
        ),

        @ForeignKey(
            entity = CycleEntity.class,
            parentColumns = "id",
            childColumns = "cycle_id",
            onDelete = ForeignKey.CASCADE
        )},

        indices = {@Index("workout_id"), @Index("cycle_id")}
)
public class ExerciseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;


    // Foreign keys
    public int workout_id;
    public int cycle_id;

    // Attributes of exercise
    public String exercise_name;
    public String tier;
    public String type;
    public double current_weight;
    public double initial_weight;
}