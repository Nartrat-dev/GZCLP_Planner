package com.example.gzclpplanner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gzclpplanner.data.entity.ExerciseEntity;

import androidx.appcompat.app.AlertDialog;

import com.example.gzclpplanner.data.repository.Repository;
import com.example.gzclpplanner.databinding.FragmentExercisesBinding;

import java.util.concurrent.atomic.AtomicInteger;


public class ExercisesFragment extends Fragment implements ExerciseAdapter.OnExerciseActionListener {

    private FragmentExercisesBinding binding;
    private Repository repository;
    private ExerciseAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        repository = new Repository(requireActivity().getApplication());
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExerciseAdapter(this);
        binding.rvExercises.setAdapter(adapter);

        // Check if we are filtering for a specific workout
        AtomicInteger workoutId = new AtomicInteger(-1);
        if (getArguments() != null) {
            workoutId.set(getArguments().getInt("workoutId", -1));
        }

        if (workoutId.get() != -1) {
            // Observe filtered data
            repository.getExercisesForWorkout(workoutId.get()).observe(getViewLifecycleOwner(), exercises -> {
                adapter.setExercises(exercises);
            });
        } else {
            // Observe all data
            repository.getAllExercisesWithSetsReps().observe(getViewLifecycleOwner(), exercises -> {
                adapter.setExercises(exercises);
            });
        }

        // Handle Add Exercise button -> navigate to AddExerciseFragment with optional workoutId
        binding.btnAddExercise.setOnClickListener(v -> {
            Bundle args = new Bundle();
            if (getArguments() != null) {
                workoutId.set(getArguments().getInt("workoutId", -1));
                if (workoutId.get() != -1) args.putInt("workoutId", workoutId.get());
            }
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_ExercisesFragment_to_AddExerciseFragment, args);
        });
    }

    @Override
    public void onDone(ExerciseEntity exercise) {
        repository.completeExercise(exercise.id, true);
    }

    @Override
    public void onFailed(ExerciseEntity exercise) {
        repository.completeExercise(exercise.id, false);
    }

    @Override
    public void onDelete(ExerciseEntity exercise) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete exercise")
                .setMessage("Are you sure you want to delete exercise '" + exercise.exerciseName + "'?")
                .setPositiveButton("Delete", (dialog, which) -> repository.deleteExercise(exercise))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEdit(ExerciseEntity exercise) {
        Bundle args = new Bundle();
        args.putInt("workoutId", exercise.workoutId);
        args.putInt("exerciseId", exercise.id);
        args.putString("exerciseName", exercise.exerciseName);
        args.putFloat("startingWeight", (float) exercise.currentWeight);
        args.putString("tier", exercise.tier != null ? exercise.tier : "");
        args.putString("type", exercise.type != null ? exercise.type : "");
        NavHostFragment.findNavController(this).navigate(R.id.action_ExercisesFragment_to_AddExerciseFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

