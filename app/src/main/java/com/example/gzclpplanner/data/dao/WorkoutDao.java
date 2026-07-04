package com.example.gzclpplanner.data.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.gzclpplanner.data.entity.WorkoutEntity;
import com.example.gzclpplanner.data.relations.*;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(WorkoutEntity workout);

    @Update
    void update(WorkoutEntity workout);

    @Delete
    void delete(WorkoutEntity workout);

    @Transaction
    @Query("SELECT * FROM workout WHERE id = :workoutId")
    WorkoutWithExercises getWorkoutWithExercises(int workoutId);

    @Transaction
    @Query("SELECT * FROM workout ORDER BY workoutNumber ASC")
    LiveData<List<WorkoutWithExercises>> getAllWorkoutsWithExercises();
}
