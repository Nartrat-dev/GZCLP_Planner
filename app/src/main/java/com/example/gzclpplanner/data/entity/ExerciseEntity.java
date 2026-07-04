package com.example.gzclpplanner.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(
        tableName = "exercise",
        foreignKeys = {
        @ForeignKey(
                entity = WorkoutEntity.class,
                parentColumns = "id",
                childColumns = "workoutId",
                onDelete = ForeignKey.CASCADE
        ),

        @ForeignKey(
            entity = CycleEntity.class,
            parentColumns = "id",
            childColumns = "cycleId",
            onDelete = ForeignKey.CASCADE
        ),

        @ForeignKey(
             entity = IterationEntity.class,
             parentColumns = "id",
             childColumns = "currentIterationId",
             onDelete = ForeignKey.CASCADE
        )},

        indices = {@Index("workoutId"), @Index("cycleId"), @Index("currentIterationId")}
)
public class ExerciseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;


    // Foreign keys
    public int workoutId;
    public int cycleId;
    public int currentIterationId;

    // Attributes of exercise
    public String exerciseName;
    public String tier;
    public String type;
    public double currentWeight;
    public double initialWeight;

    @Ignore
    public int sets;
    @Ignore
    public int reps;
}
