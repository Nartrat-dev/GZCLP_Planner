package com.example.gzclpplanner.applogic;
import java.util.ArrayList;

public class Workout {

    // Attributes
    String workout_name;
    int workout_number; // Number of Workout to determine sequence
    ArrayList<Exercise> exercise_list;


    // To keep track of the amount of workouts and to make a sequence
    static int total_workouts;
    static int next_workout_number() {
        return ++total_workouts;
    }

    // Constructors
    Workout() {
        this.workout_name = "Unnamed Workout";
        this.workout_number = next_workout_number();
    }
    Workout(String name) {
        this.workout_name = name;
        this.workout_number = next_workout_number();
    }

    // Methods
    void add_exercise(Exercise new_exercise) {
        exercise_list.add(new_exercise);
    }

    void remove_exercise(int exercise_number) {
        exercise_list.remove(exercise_number);
    }

    void swap_workout_numbers(Workout a, Workout b) {
        int helper = a.workout_number;
        a.workout_number = b.workout_number;
        b.workout_number = helper;
    }

}
