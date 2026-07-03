package com.example.gzclpplanner.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.gzclpplanner.data.entity.WorkoutPlanEntity;

public interface WorkoutPlanDao {
    @Insert
    void insert(WorkoutPlanEntity workoutPlan);

    @Update
    void update(WorkoutPlanEntity workoutPlan);

    @Delete
    void delete(WorkoutPlanEntity workoutPlan);
}
