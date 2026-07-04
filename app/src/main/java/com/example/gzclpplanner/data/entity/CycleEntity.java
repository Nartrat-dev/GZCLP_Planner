package com.example.gzclpplanner.data.entity;

import androidx.room.*;

@Entity(tableName = "cycle")
public class CycleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name; // optional, e.g. "GZCLP T1"
}
