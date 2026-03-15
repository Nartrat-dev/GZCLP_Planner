package com.example.gzclpplanner.applogic;
import java.util.ArrayList;

/**
 * The cycle for GZCLP, i.e. Tier 1 Cycle
 */

public class Cycle {
    // Attributes
    ArrayList<Iteration> cycle;
    int current_iteration;

    // Constructors
    Cycle (Iteration first_iteration, Iteration second_iteration, Iteration third_iteration) {
        this.current_iteration = 0; // Cycle starts at first Iteration
        this.cycle.add(first_iteration);
        this.cycle.add(second_iteration);
        this.cycle.add(third_iteration);
    }


    // Methods
    /**
     * Add or remove iteration from cycle
     */
    void add_iteration(Iteration iteration) {
        cycle.add(iteration);
    }
    void remove_iteration(Iteration iteration) {
        cycle.remove(iteration);
    }

    /**
     * Goes to next Iteration of the cycle
     */
    void next_iteration () {
        // When the cycle is at it's end, reset it
        if (current_iteration == cycle.size() - 1) {
            current_iteration = 0; // resets to first Iteration
        }
        // If it is not at it's end, go to the next Iteration
        else {
            current_iteration++; // go to next Iteration
        }
    }

    /**
     * Goes to previous Iteration of the cycle
     */
    void previous_iteration() {
        // When the cycle is at it's beginning, go to the last element
        if (current_iteration == 0) {
            current_iteration = cycle.size() - 1; // resets to first Iteration
        }
        // If it is not at it's beginning, go to the previous Iteration
        else {
            current_iteration--; // go to next Iteration
        }
    }

    /**
     * Resets the cycle
     */
    void reset_cycle() {
        current_iteration = 0;
    }

    /**
     * Checks, if cycle is at it's end
     */
    boolean is_last_iteration () {
        if (current_iteration == cycle.size() - 1) {
            return true;
        }
        else {
            return false;
        }
    }
}
