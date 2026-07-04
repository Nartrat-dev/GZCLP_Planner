package com.example.gzclpplanner.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.gzclpplanner.data.entity.ExerciseEntity;

import java.util.List;

public interface ExerciseDao {
    @Insert
    long insert(ExerciseEntity exercise);

    @Update
    void update(ExerciseEntity exercise);

    @Delete
    void delete(ExerciseEntity exercise);

    @Query("SELECT * FROM exercise")
    LiveData<List<ExerciseEntity>> getAllExercises();

    @Query("SELECT * FROM exercise WHERE id = :id")
    ExerciseEntity getExerciseById(int id);

    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId")
    List<ExerciseEntity> getExercisesForWorkout(int workoutId);
}
