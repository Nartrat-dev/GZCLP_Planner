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

        // Handle Add Workout button
        binding.btnAddWorkout.setOnClickListener(v -> {
            // TODO: Open add workout dialog or navigate to add workout screen
        });
    }

    @Override
    public void onStartWorkout(WorkoutWithExercises workout) {
        Bundle bundle = new Bundle();
        bundle.putInt("workoutId", workout.workout.id);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_WorkoutsFragment_to_ExercisesFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

