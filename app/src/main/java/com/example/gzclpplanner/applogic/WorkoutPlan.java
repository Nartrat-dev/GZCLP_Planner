package com.example.gzclpplanner.applogic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the logic for the list of all the workouts in a plan
 * (i.e. for GZCLP: Day 1, Day 2, Day 3, Day 4)
 */
public class WorkoutPlan {
    private final List<Workout> workout_list = new ArrayList<>();

    public void add_workout(Workout workout) {
        workout_list.add(workout);
    }

    public void remove_workout(Workout workout) {
        workout_list.remove(workout);
    }

    public void swap_workouts(int index_a, int index_b) {
        Collections.swap(workout_list, index_a, index_b);
    }

    public Workout get_next_workout(int currentIndex) {
        return workout_list.get((currentIndex + 1) % workout_list.size());
    }

}

