package com.example.gzclpplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gzclpplanner.data.relations.WorkoutWithExercises;
import com.example.gzclpplanner.data.entity.WorkoutEntity;
import androidx.appcompat.app.AlertDialog;
import com.example.gzclpplanner.data.repository.Repository;
import com.example.gzclpplanner.databinding.FragmentWorkoutsBinding;

import java.util.List;

public class WorkoutsFragment extends Fragment implements WorkoutAdapter.OnWorkoutActionListener {

    private FragmentWorkoutsBinding binding;
    private Repository repository;
    private WorkoutAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        repository = new Repository(requireActivity().getApplication());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutAdapter(this);
        binding.rvExercises.setAdapter(adapter);

        // Observe data
        repository.getAllWorkoutsWithExercises().observe(getViewLifecycleOwner(), workouts -> {
            adapter.setWorkouts(workouts);
        });

        // Handle Add Workout button -> navigate to AddWorkoutFragment
        binding.btnAddWorkout.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_WorkoutsFragment_to_AddWorkoutFragment)
        );
    }

    @Override
    public void onStartWorkout(WorkoutWithExercises workout) {
        Bundle bundle = new Bundle();
        bundle.putInt("workoutId", workout.workout.id);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_WorkoutsFragment_to_ExercisesFragment, bundle);
    }

    @Override
    public void onDeleteWorkout(WorkoutWithExercises workout) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete workout")
                .setMessage("Are you sure you want to delete workout '" + workout.workout.workoutName + "'? This will remove all exercises in it.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteWorkout(workout.workout);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditWorkout(WorkoutWithExercises workout) {
        Bundle args = new Bundle();
        args.putInt("workoutId", workout.workout.id);
        args.putString("workoutName", workout.workout.workoutName);
        args.putInt("workoutNumber", workout.workout.workoutNumber);
        NavHostFragment.findNavController(this).navigate(R.id.action_WorkoutsFragment_to_AddWorkoutFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

