package com.example.gzclpplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gzclpplanner.data.entity.ExerciseEntity;
import com.example.gzclpplanner.data.repository.Repository;
import com.example.gzclpplanner.databinding.FragmentExercisesBinding;

import java.util.List;

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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExerciseAdapter(this);
        binding.rvExercises.setAdapter(adapter);

        // Observe data
        repository.getAllExercises().observe(getViewLifecycleOwner(), exercises -> {
            adapter.setExercises(exercises);
        });

        // Handle Add Exercise button
        binding.btnAddExercise.setOnClickListener(v -> {
            // TODO: Open add exercise dialog or navigate to add exercise screen
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

