package com.example.gzclpplanner.applogic;


public class Exercise {

    public enum Tier { T1, T2, T3 }
    public enum Type{ upper, lower }

    // Attributes
    String exercise_name;

    Tier exercise_tier;
    Type exercise_type;

    double current_weight_kilograms;
    double initial_weight_kilograms;

    int number_of_sets;
    int number_of_repetitions;


    // Constructors
    Exercise() {
        this.exercise_name = "Unnamed Exercise";
    }
    Exercise(String name, Tier tier, Type type, double weight, int sets, int reps) {
        this.exercise_name = name;
        this.current_weight_kilograms = weight;
        this.initial_weight_kilograms = weight;
        this.number_of_sets = sets;
        this.number_of_repetitions = reps;
        this.exercise_tier = tier;
        this.exercise_type = type;
    }



}
