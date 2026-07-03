package com.example.gzclpplanner.data.repository;

import android.app.Application;

import com.example.gzclpplanner.applogic.*;
import com.example.gzclpplanner.data.AppDatabase;
import com.example.gzclpplanner.data.dao.*;
import com.example.gzclpplanner.data.entity.*;

public class WorkoutRepository {

    private final ExerciseDao exerciseDao;
    private final AppDatabase db;

    public WorkoutRepository(Application application) {
        db = AppDatabase.getDatabase(application);
        exerciseDao = db.exerciseDao();
    }

    public void doExercise(boolean success, Exercise exercise, int exerciseEntityId, int workoutId, int cycleId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // domain logic is being executed
            new GZCLPLogic().do_exercise(success, exercise);

            // Convert the now-mutated object to an entity and persist
            ExerciseEntity updated = toEntity(exercise, workoutId, exerciseEntityId, cycleId);
            exerciseDao.update(updated);
        });

    }


    // Mapping to logic
    // todo: make current cycle into exercise table
    private ExerciseEntity toEntity(Exercise e, int workoutId, int existingId, int cycleId) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.id = existingId; // 0 if new, Room autogenerates
        entity.workoutId = workoutId;
        entity.cycleId = cycleId;
        // entity.cycleIteration = e.exercise_cycle.current_iteration; todo: IMPLEMENT!
        entity.exerciseName = e.exercise_name;
        entity.currentWeight = e.current_weight_kilograms;
        entity.initialWeight = e.initial_weight_kilograms;
        entity.tier = e.exercise_tier.name();
        entity.type = e.exercise_type.name();
        return entity;
    }

    /* todo: implement
    private Exercise toDomain(ExerciseEntity entity) {
        Cycle cycle = new Cycle(entity.cycleId);
        return new Exercise(
                entity.exerciseName,
                Exercise.Tier.valueOf(entity.tier),
                Exercise.Type.valueOf(entity.type),
                entity.currentWeight,
                cycle
        );*/
}