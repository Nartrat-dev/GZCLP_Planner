package com.example.gzclpplanner.data.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.gzclpplanner.data.entity.ExerciseEntity;
import com.example.gzclpplanner.data.entity.WorkoutEntity;
import com.example.gzclpplanner.data.relations.*;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(WorkoutEntity workout);

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :workoutId")
    WorkoutWithExercises getWorkoutWithExercises(int workoutId);
}
