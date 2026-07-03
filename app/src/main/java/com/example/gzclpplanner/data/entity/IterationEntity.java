package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(
        tableName = "iteration",
        foreignKeys = @ForeignKey(
                entity = CycleEntity.class,
                parentColumns = "id",
                childColumns = "cycleId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(("cycleId"))}
)
public class IterationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Foreign keys
    public int cycleId;

    // Attributes
    public int numberOfSets;
    public int numberOfReps;
}
