package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(tableName = "cycle")
public class CycleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String current_iteration;
}
