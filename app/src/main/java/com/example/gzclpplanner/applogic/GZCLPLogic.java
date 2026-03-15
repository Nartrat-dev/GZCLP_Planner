package com.example.gzclpplanner.applogic;

public class GZCLPLogic {
    void do_exercise(boolean success, Exercise exercise) {

        // Situation 1: all reps in every set successfully done
        if (success) {

            exercise.increase_weight_gzclp(); // Increase weight by 5 (lower body) or 2.5 (upper body)

            // If it is the first Iteration of the cycle -> save weight
            if (exercise.exercise_cycle.is_last_iteration()) {
                exercise.set_initial_weight_to_current_weight();
            }

        }

        // Situation 2: one or more reps of one or more sets unsuccessfull
        else {
            exercise.exercise_cycle.next_iteration(); // go to next Iteration

            // Lower weight, if the cycle is reset
            if (exercise.exercise_cycle.current_iteration == 0) {
                exercise.reset_weight_gzclp(); // Resets Weight depending on tier and type of Exercise
            }
        }
    }
}

