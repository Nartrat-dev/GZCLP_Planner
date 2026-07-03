package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(
        tableName = "iteration",
        foreignKeys = @ForeignKey(
                entity = CycleEntity.class,
                parentColumns = "id",
                childColumns = "cycle_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(("cycle_id"))}
)
public class IterationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Foreign keys
    public int cycle_id;

    // Attributes
    public int number_of_sets;
    public int getNumber_of_reps;
}
