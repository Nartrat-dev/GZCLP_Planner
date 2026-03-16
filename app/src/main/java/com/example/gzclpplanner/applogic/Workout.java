package com.example.gzclpplanner.applogic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all the exercises of one workout (aka. Day)
 */
public class Workout {

    // Attributes
    private final String workout_name;
    private final ArrayList<Exercise> exercise_list;


    // Constructors
    public Workout() {
        this.workout_name = "Unnamed Workout";
        this.exercise_list = new ArrayList<>();
    }
    Workout(String name) {
        this.workout_name = name;
        this.exercise_list = new ArrayList<>();
    }

    // Methods
    public void add_exercise(Exercise new_exercise) {
        exercise_list.add(new_exercise);
    }
    public void remove_exercise(int exercise_number) {
        exercise_list.remove(exercise_number);
    }

    public void swap_exercises(int index_a, int index_b) {
        Collections.swap(exercise_list, index_a, index_b);
    }

    // Getter Methods
    public List<Exercise> get_exercise_list() {
        return List.copyOf(exercise_list);
    }
    public String get_workout_name(){
        return workout_name;
    }
}
