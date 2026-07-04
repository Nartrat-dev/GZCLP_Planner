package com.example.gzclpplanner.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.gzclpplanner.applogic.*;
import com.example.gzclpplanner.data.AppDatabase;
import com.example.gzclpplanner.data.entity.*;
import com.example.gzclpplanner.data.dao.*;
import com.example.gzclpplanner.data.relations.CycleWithIterations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Repository {

    private ExerciseDao exerciseDao;
    private CycleDao cycleDao;
    private AppDatabase db;
    private GZCLPLogic logic;

    public Repository(Application application) {
        db = AppDatabase.getDatabase(application);
        exerciseDao = db.exerciseDao();
        cycleDao = db.cycleDao();
        logic = new GZCLPLogic();
    }

    /**
     * Cycle -> Entity
     */
    public CycleEntity toCycleEntity(Cycle cycle) {
        CycleEntity entity = new CycleEntity();
        entity.id = cycle.id; // 0 if new
        entity.name = cycle.get_cycle_name();
        return entity;
    }

    public List<IterationEntity> toIterationEntities(Cycle cycle, int cycleId) {
        List<IterationEntity> result = new ArrayList<>();
        List<Iteration> iterations = cycle.get_cycle();
        for (int i = 0; i < iterations.size(); i++) {
            Iteration it = iterations.get(i);
            IterationEntity ie = new IterationEntity();
            ie.id = it.id;
            ie.cycleId = cycleId;
            ie.position = i;
            ie.numberOfSets = it.sets();
            ie.numberOfReps = it.reps();
            result.add(ie);
        }
        return result;
    }

    /**
     * Entity -> Cycle
     */
    public Cycle toDomainCycle(CycleWithIterations cwi, int currentIterationEntityId) {
        List<IterationEntity> sorted = new ArrayList<>(cwi.iterations);
        sorted.sort(Comparator.comparingInt(ie -> ie.position));

        List<Iteration> iterations = new ArrayList<>();
        int currentIndex = 0;
        for (int i = 0; i < sorted.size(); i++) {
            IterationEntity ie = sorted.get(i);
            Iteration iteration = new Iteration(ie.numberOfSets, ie.numberOfReps);
            iteration.id = ie.id;
            iterations.add(iteration);
            if (ie.id == currentIterationEntityId) {
                currentIndex = i;
            }
        }

        Cycle cycle = new Cycle(iterations);
        cycle.id = cwi.cycle.id;
        cycle.set_current_iteration(currentIndex);
        return cycle;
    }

    /**
     * Exercise Mapping
     */
    public ExerciseEntity toEntity(Exercise e, int workoutId) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.id = e.id;
        entity.workoutId = workoutId;
        entity.exerciseName = e.exercise_name;
        entity.currentWeight = e.current_weight_kilograms;
        entity.initialWeight = e.initial_weight_kilograms;
        entity.tier = e.exercise_tier.name();
        entity.type = e.exercise_type.name();
        entity.cycleId = e.exercise_cycle.id;
        entity.currentIterationId = e.exercise_cycle.get_current_iteration().id;
        return entity;
    }

    public Exercise toDomain(ExerciseEntity entity, CycleWithIterations cwi) {
        Cycle cycle = toDomainCycle(cwi, entity.currentIterationId);
        Exercise e = new Exercise(
                entity.exerciseName,
                Exercise.Tier.valueOf(entity.tier),
                Exercise.Type.valueOf(entity.type),
                entity.currentWeight,
                cycle
        );
        e.id = entity.id;
        e.initial_weight_kilograms = entity.initialWeight;
        return e;
    }


    /**
     * Public API for UI
     */

    public LiveData<List<ExerciseEntity>> getAllExercises() {
        return exerciseDao.getAllExercises();
    }

    public void completeExercise(int exerciseId, boolean success) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            ExerciseEntity entity = exerciseDao.getExerciseById(exerciseId);
            if (entity == null) return;

            CycleWithIterations cwi = cycleDao.getCycleWithIterations(entity.cycleId);
            Exercise domainExercise = toDomain(entity, cwi);

            logic.do_exercise(success, domainExercise);

            saveExercise(domainExercise, entity.workoutId);
        });
    }

    /**
     * Saving a shared cycle without duplicating
     */
    public void saveExercise(Exercise exercise, int workoutId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Cycle cycle = exercise.exercise_cycle;

            if (cycle.id == 0) {
                // New cycle: insert it and its iterations, then wire up ids
                long cycleId = cycleDao.insertCycle(toCycleEntity(cycle));
                cycle.id = (int) cycleId;

                List<IterationEntity> iterationEntities = toIterationEntities(cycle, cycle.id);
                List<Long> iterationIds = cycleDao.insertIterations(iterationEntities);
                for (int i = 0; i < iterationIds.size(); i++) {
                    cycle.get_cycle().get(i).id = iterationIds.get(i).intValue();
                }
            }
            // If cycle.id != 0, it's already persisted and possibly shared — don't touch it here.

            ExerciseEntity entity = toEntity(exercise, workoutId);
            if (entity.id == 0) {
                long newId = exerciseDao.insert(entity);
                exercise.id = (int) newId;
            } else {
                exerciseDao.update(entity);
            }
        });
    }
}
