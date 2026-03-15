package com.example.gzclpplanner.applogic;

/**
 * One Iteration of a cycle that determines rep and set amount of an exercise
 */

public final class Iteration {

    private final int number_of_sets;
    private final int number_of_reps;

    public Iteration(int sets, int reps) {
        if (sets <= 0 || reps <= 0) {
            throw new IllegalArgumentException("Sets and Reps cannot be smaller than 0");
        }
        this.number_of_sets = sets;
        this.number_of_reps = reps;
    }

    public int sets() {
        return number_of_sets;
    }

    public int reps() {
        return number_of_reps;
    }

}