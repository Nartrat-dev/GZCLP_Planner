package com.example.gzclpplanner.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.gzclpplanner.applogic.*;
import com.example.gzclpplanner.data.AppDatabase;
import com.example.gzclpplanner.data.entity.*;
import com.example.gzclpplanner.data.dao.*;
import com.example.gzclpplanner.data.relations.CycleWithIterations;
import com.example.gzclpplanner.data.relations.ExerciseWithIteration;
import com.example.gzclpplanner.data.relations.WorkoutWithExercises;
import com.example.gzclpplanner.data.relations.WorkoutWithExercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Repository {

    private final ExerciseDao exerciseDao;
    private final CycleDao cycleDao;
    private final WorkoutDao workoutDao;
    private final WorkoutPlanDao workoutPlanDao;
    private final IterationDao iterationDao;
    private final GZCLPLogic logic;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        exerciseDao = db.exerciseDao();
        cycleDao = db.cycleDao();
        workoutDao = db.workoutDao();
        workoutPlanDao = db.workoutPlanDao();
        iterationDao = db.iterationDao();
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

    public LiveData<List<WorkoutWithExercises>> getAllWorkoutsWithExercises() {
        return workoutDao.getAllWorkoutsWithExercises();
    }

    public LiveData<List<ExerciseEntity>> getAllExercisesWithSetsReps() {
        return Transformations.map(exerciseDao.getAllExercisesWithIteration(), (List<ExerciseWithIteration> ewIs) -> {
            List<ExerciseEntity> result = new ArrayList<>();
            for (ExerciseWithIteration ewi : ewIs) {
                ExerciseEntity e = ewi.exercise;
                if (ewi.iteration != null) {
                    e.sets = ewi.iteration.numberOfSets;
                    e.reps = ewi.iteration.numberOfReps;
                }
                result.add(e);
            }
            return result;
        });
    }

    public LiveData<List<ExerciseEntity>> getExercisesForWorkout(int workoutId) {
        return Transformations.map(exerciseDao.getExercisesForWorkoutWithIteration(workoutId), (List<ExerciseWithIteration> ewIs) -> {
            List<ExerciseEntity> result = new ArrayList<>();
            for (ExerciseWithIteration ewi : ewIs) {
                ExerciseEntity e = ewi.exercise;
                if (ewi.iteration != null) {
                    e.sets = ewi.iteration.numberOfSets;
                    e.reps = ewi.iteration.numberOfReps;
                }
                result.add(e);
            }
            return result;
        });
    }

    public void completeExercise(int exerciseId, boolean success) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            ExerciseEntity entity = exerciseDao.getExerciseById(exerciseId);
            if (entity == null) return;

            CycleWithIterations cwi = cycleDao.getCycleWithIterations(entity.cycleId);
            Exercise domainExercise = toDomain(entity, cwi);

            logic.do_exercise(success, domainExercise);

            // saveExercise is now synchronous-capable since it's called within this executor block
            saveExercise(domainExercise, entity.workoutId);
        });
    }

    public void initializeDefaultData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (workoutPlanDao.getWorkoutPlan() != null) {
                return; // Data already exists
            }

            WorkoutPlanEntity plan = new WorkoutPlanEntity();
            plan.id = 1;
            plan.currentWorkout = 1;
            workoutPlanDao.insert(plan);

            String[] workoutNames = {"Workout A1", "Workout B1", "Workout A2", "Workout B2"};
            for (int i = 0; i < 4; i++) {
                WorkoutEntity workout = new WorkoutEntity();
                workout.workoutPlanId = 1;
                workout.workoutName = workoutNames[i];
                workout.workoutNumber = i + 1;
                long workoutId = workoutDao.insert(workout);

                // Add 4 exercises per workout
                addDefaultExercisesForWorkout((int) workoutId, i);
            }
        });
    }

    private void addDefaultExercisesForWorkout(int workoutId, int workoutIndex) {
        // T1: 3x5, 3x3, 3x1
        // T2: 3x10, 3x8, 3x6
        // T3: 3x15, 3x12, 3x10

        if (workoutIndex == 0) { // A1
            saveExercise(new Exercise("Squat", Exercise.Tier.T1, Exercise.Type.LOWER, 100, new Cycle(Arrays.asList(new Iteration(3, 5), new Iteration(3, 3), new Iteration(3, 1)))), workoutId);
            saveExercise(new Exercise("Bench Press", Exercise.Tier.T2, Exercise.Type.UPPER, 60, new Cycle(Arrays.asList(new Iteration(3, 10), new Iteration(3, 8), new Iteration(3, 6)))), workoutId);
            saveExercise(new Exercise("Lat Pulldown", Exercise.Tier.T3, Exercise.Type.UPPER, 40, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
            saveExercise(new Exercise("Leg Press", Exercise.Tier.T3, Exercise.Type.LOWER, 80, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
        } else if (workoutIndex == 1) { // B1
            saveExercise(new Exercise("Overhead Press", Exercise.Tier.T1, Exercise.Type.UPPER, 40, new Cycle(Arrays.asList(new Iteration(3, 5), new Iteration(3, 3), new Iteration(3, 1)))), workoutId);
            saveExercise(new Exercise("Deadlift", Exercise.Tier.T2, Exercise.Type.LOWER, 120, new Cycle(Arrays.asList(new Iteration(3, 10), new Iteration(3, 8), new Iteration(3, 6)))), workoutId);
            saveExercise(new Exercise("Dumbbell Row", Exercise.Tier.T3, Exercise.Type.UPPER, 20, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
            saveExercise(new Exercise("Plank", Exercise.Tier.T3, Exercise.Type.UPPER, 0, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
        } else if (workoutIndex == 2) { // A2
            saveExercise(new Exercise("Bench Press", Exercise.Tier.T1, Exercise.Type.UPPER, 70, new Cycle(Arrays.asList(new Iteration(3, 5), new Iteration(3, 3), new Iteration(3, 1)))), workoutId);
            saveExercise(new Exercise("Squat", Exercise.Tier.T2, Exercise.Type.LOWER, 85, new Cycle(Arrays.asList(new Iteration(3, 10), new Iteration(3, 8), new Iteration(3, 6)))), workoutId);
            saveExercise(new Exercise("Face Pulls", Exercise.Tier.T3, Exercise.Type.UPPER, 15, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
            saveExercise(new Exercise("Calf Raises", Exercise.Tier.T3, Exercise.Type.LOWER, 30, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
        } else if (workoutIndex == 3) { // B2
            saveExercise(new Exercise("Deadlift", Exercise.Tier.T1, Exercise.Type.LOWER, 140, new Cycle(Arrays.asList(new Iteration(3, 5), new Iteration(3, 3), new Iteration(3, 1)))), workoutId);
            saveExercise(new Exercise("Overhead Press", Exercise.Tier.T2, Exercise.Type.UPPER, 35, new Cycle(Arrays.asList(new Iteration(3, 10), new Iteration(3, 8), new Iteration(3, 6)))), workoutId);
            saveExercise(new Exercise("Bicep Curls", Exercise.Tier.T3, Exercise.Type.UPPER, 12.5, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
            saveExercise(new Exercise("Tricep Extensions", Exercise.Tier.T3, Exercise.Type.UPPER, 12.5, new Cycle(Arrays.asList(new Iteration(3, 15), new Iteration(3, 12), new Iteration(3, 10)))), workoutId);
        }
    }

    public void saveExercise(Exercise exercise, int workoutId) {
        // Run synchronously if called within a databaseWriteExecutor block, or handle threading carefully
        Cycle cycle = exercise.exercise_cycle;

        if (cycle.id == 0) {
            long cycleId = cycleDao.insertCycle(toCycleEntity(cycle));
            cycle.id = (int) cycleId;

            List<IterationEntity> iterationEntities = toIterationEntities(cycle, cycle.id);
            List<Long> iterationIds = cycleDao.insertIterations(iterationEntities);
            for (int i = 0; i < iterationIds.size(); i++) {
                cycle.get_cycle().get(i).id = iterationIds.get(i).intValue();
            }
        }

        ExerciseEntity entity = toEntity(exercise, workoutId);
        if (entity.id == 0) {
            long newId = exerciseDao.insert(entity);
            exercise.id = (int) newId;
        } else {
            exerciseDao.update(entity);
        }
    }

    /**
     * Asynchronous wrapper for saveExercise to be called from UI thread
     */
    public void saveExerciseAsync(Exercise exercise, int workoutId) {
        AppDatabase.databaseWriteExecutor.execute(() -> saveExercise(exercise, workoutId));
    }

    /**
     * Workout operations
     */
    public void saveWorkout(com.example.gzclpplanner.data.entity.WorkoutEntity workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (workout.id == 0) {
                workoutDao.insert(workout);
            } else {
                workoutDao.update(workout);
            }
        });
    }

    public void deleteWorkout(com.example.gzclpplanner.data.entity.WorkoutEntity workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutDao.delete(workout));
    }

    public void deleteExercise(com.example.gzclpplanner.data.entity.ExerciseEntity exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> exerciseDao.delete(exercise));
    }

    /**
     * Update only the editable fields of an existing exercise (keeps cycle references intact)
     */
    public void updateExerciseFieldsAsync(int exerciseId, String name, double currentWeight, String tier, String type) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            ExerciseEntity entity = exerciseDao.getExerciseById(exerciseId);
            if (entity == null) return;
            entity.exerciseName = name;
            entity.currentWeight = currentWeight;
            entity.tier = tier;
            entity.type = type;
            exerciseDao.update(entity);
        });
    }
}
