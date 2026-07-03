package com.example.gzclpplanner.data.dao;
import androidx.room.*;

import com.example.gzclpplanner.data.entity.WorkoutEntity;
import com.example.gzclpplanner.data.relations.*;

@Dao
public interface WorkoutDao {
    @Insert
    void insert(WorkoutEntity workout);

    @Update
    void update(WorkoutEntity workout);

    @Delete
    void delete(WorkoutEntity workout);

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :workoutId")
    WorkoutWithExercises getWorkoutWithExercises(int workoutId);
}
