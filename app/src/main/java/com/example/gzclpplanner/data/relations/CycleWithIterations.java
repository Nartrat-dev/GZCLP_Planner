package com.example.gzclpplanner.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gzclpplanner.data.entity.*;

import java.util.List;

public class CycleWithIterations {
    @Embedded
    public CycleEntity cycle;

    @Relation(
            parentColumn = "id",
            entityColumn = "iterationId"
    )
    public List<IterationEntity> iterations;
}
