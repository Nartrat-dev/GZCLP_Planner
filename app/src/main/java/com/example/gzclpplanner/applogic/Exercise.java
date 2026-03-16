package com.example.gzclpplanner.applogic;


public class Exercise {

    public enum Tier {T1, T2, T3}

    public enum Type {
        UPPER(5.0), LOWER(2.5);
        final double increment; // Increment -> amount of weight change for upper or lower body

        Type(double increment) {
            this.increment = increment;
        }
    }

    // Attributes *************************************************
    String exercise_name;

    Tier exercise_tier;
    Type exercise_type;

    double current_weight_kilograms;
    double initial_weight_kilograms;

    Cycle exercise_cycle; // i.e. {4,4}, {5,3}, {6,2}

    // Constructors ************************************************************************
    Exercise() {
        this.exercise_name = "Unnamed Exercise";
    }

    Exercise(String name, Tier tier, Type type, double weight, Cycle cycle) {
        this.exercise_name = name;
        this.current_weight_kilograms = weight;
        this.initial_weight_kilograms = weight;
        this.exercise_tier = tier;
        this.exercise_type = type;
        this.exercise_cycle = cycle;
    }

    // Methods ***************************************************

    /**
     * Functions to set the weight
     */
    void increase_weight(double delta) {
        current_weight_kilograms += delta;
    }
    void decrease_weight(double delta){
        current_weight_kilograms -= delta;
    }
    void set_weight(double amount) {
        current_weight_kilograms = amount;
    }

    /**
     * Rounding function for GZCLP
     */
    double round_down_to_rounding_factor(double value, double rounding_factor) {
        return Math.floor((value / rounding_factor) * value);
    }

    /**
     * Resets Weights, depending on Tier and Type of the exercise
     */
    void reset_weight_gzclp(){
        if (exercise_tier.equals(Tier.T1)) {
            double new_weight = round_down_to_rounding_factor(0.85 * current_weight_kilograms, exercise_type.increment);
            set_weight(new_weight);
        }
        if (exercise_tier.equals(Tier.T2)) {
            set_weight(initial_weight_kilograms + exercise_type.increment);
        }
        if (exercise_tier.equals(Tier.T3)) {
            decrease_weight(exercise_type.increment);
        }
    }

    /**
     * Increeases Weights, depending on Tier and Type of the exercise
     */
    void increase_weight_gzclp() {
        // Increase by 5kg for lower body exercises or by 2.5kg for upper body exercises
        increase_weight(exercise_type.increment);
    }

    /**
     * Updates initial weight to current weight
     */
    void set_initial_weight_to_current_weight() {
        current_weight_kilograms = initial_weight_kilograms;
    }
}

