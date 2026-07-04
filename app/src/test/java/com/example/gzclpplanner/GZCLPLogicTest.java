package com.example.gzclpplanner;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.gzclpplanner.applogic.*;
import java.util.Arrays;

public class GZCLPLogicTest {

    @Test
    public void testT1ExerciseSuccess() {
        Iteration it1 = new Iteration(3, 5);
        Iteration it2 = new Iteration(3, 3);
        Iteration it3 = new Iteration(3, 1);
        Cycle cycle = new Cycle(Arrays.asList(it1, it2, it3));
        
        Exercise exercise = new Exercise("Squat", Exercise.Tier.T1, Exercise.Type.LOWER, 100.0, cycle);
        GZCLPLogic logic = new GZCLPLogic();

        // Success should increase weight and stay on same iteration if it's the first one
        logic.do_exercise(true, exercise);
        
        assertEquals(105.0, exercise.current_weight_kilograms, 0.01);
        assertEquals(105.0, exercise.initial_weight_kilograms, 0.01);
        assertEquals(0, cycle.getCurrentIterationIndex()); 
    }

    @Test
    public void testT1ExerciseFailureTransitions() {
        Iteration it1 = new Iteration(3, 5);
        Iteration it2 = new Iteration(3, 3);
        Iteration it3 = new Iteration(3, 1);
        Cycle cycle = new Cycle(Arrays.asList(it1, it2, it3));
        
        Exercise exercise = new Exercise("Squat", Exercise.Tier.T1, Exercise.Type.LOWER, 100.0, cycle);
        GZCLPLogic logic = new GZCLPLogic();

        // Failure 1: 3x5 -> 3x3
        logic.do_exercise(false, exercise);
        assertEquals(100.0, exercise.current_weight_kilograms, 0.01);
        assertEquals(1, cycle.getCurrentIterationIndex());

        // Failure 2: 3x3 -> 3x1
        logic.do_exercise(false, exercise);
        assertEquals(2, cycle.getCurrentIterationIndex());

        // Failure 3: 3x1 -> Reset (Weight should decrease by 15% and round down to nearest 5)
        // 100 * 0.85 = 85.
        logic.do_exercise(false, exercise);
        assertEquals(0, cycle.getCurrentIterationIndex());
        assertEquals(85.0, exercise.current_weight_kilograms, 0.01);
    }
    
    // Helper to access private field in Cycle for testing if needed, 
    // or we can add a getter to Cycle.
}
