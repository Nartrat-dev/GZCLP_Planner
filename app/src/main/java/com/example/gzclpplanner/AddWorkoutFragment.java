package com.example.gzclpplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gzclpplanner.data.entity.WorkoutEntity;
import com.example.gzclpplanner.data.repository.Repository;
import com.example.gzclpplanner.databinding.FragmentAddWorkoutBinding;

public class AddWorkoutFragment extends Fragment {

    private FragmentAddWorkoutBinding binding;
    private Repository repository;
    private int nextNumber = 1;
    private int workoutId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddWorkoutBinding.inflate(inflater, container, false);
        repository = new Repository(requireActivity().getApplication());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository.getAllWorkoutsWithExercises().observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null) {
                // compute the smallest missing positive integer from existing workoutNumber values
                java.util.Set<Integer> numbers = new java.util.HashSet<>();
                int max = 0;
                for (int i = 0; i < workouts.size(); i++) {
                    int n = workouts.get(i).workout.workoutNumber;
                    numbers.add(n);
                    if (n > max) max = n;
                }
                int candidate = 1;
                while (numbers.contains(candidate)) {
                    candidate++;
                }
                // candidate is now the smallest missing positive integer
                nextNumber = candidate;
            }
        });

        // Prefill if editing
        if (getArguments() != null) {
            workoutId = getArguments().getInt("workoutId", -1);
            String name = getArguments().getString("workoutName", "");
            int number = getArguments().getInt("workoutNumber", nextNumber);
            if (workoutId != -1) {
                binding.etWorkoutName.setText(name);
                nextNumber = number; // preserve existing number
            }
        }

        binding.btnSaveWorkout.setOnClickListener(v -> {
            String name = binding.etWorkoutName.getText().toString().trim();
            if (name.isEmpty()) name = "New Workout";
            WorkoutEntity workout = new WorkoutEntity();
            workout.workoutName = name;
            workout.workoutNumber = nextNumber;
            workout.workoutPlanId = 1;
            if (workoutId != -1) workout.id = workoutId;
            repository.saveWorkout(workout);
            NavHostFragment.findNavController(this).navigateUp();
        });

        binding.btnCancelWorkout.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


