package com.example.gzclpplanner.applogic;
import java.util.ArrayList;
import java.util.List;

/**
 * The cycle for GZCLP, i.e. Tier 1 Cycle
 */

public class Cycle {
    // Attributes
    public int id;
    String cycle_name;
    ArrayList<Iteration> cycle;
    int current_iteration;

    // Constructors
    public Cycle(Iteration first_iteration, Iteration second_iteration, Iteration third_iteration) {
        this.current_iteration = 0; // Cycle starts at first Iteration
        this.cycle.add(first_iteration);
        this.cycle.add(second_iteration);
        this.cycle.add(third_iteration);
    }

    public Cycle(List<Iteration> iterations) {
        cycle.addAll(iterations);
    }


    // Methods
    /**
     * Getters
     */
    public Iteration get_current_iteration() {
        return cycle.get(current_iteration);
    }
    public String get_cycle_name() {
        return cycle_name;
    }
    public ArrayList<Iteration> get_cycle() {
        return cycle;
    }


    /**
     * Setters
     */
    public void set_current_iteration(int current_iteration) {
        this.current_iteration = current_iteration;
    }

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
        if (is_last_iteration()) {
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
        if (is_first_iteration()) {
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
     * Checks, if cycle is at it's beginning
     */
    boolean is_first_iteration () {
        return current_iteration == 0;
    }

    /**
     * Checks, if cycle is at it's end
     */
    boolean is_last_iteration () {
        return current_iteration == cycle.size() - 1;
    }
}
