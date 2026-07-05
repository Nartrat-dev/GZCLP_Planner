package com.example.gzclpplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gzclpplanner.applogic.Cycle;
import com.example.gzclpplanner.applogic.Exercise;
import com.example.gzclpplanner.applogic.Iteration;
import com.example.gzclpplanner.data.repository.Repository;
import com.example.gzclpplanner.databinding.FragmentAddExerciseBinding;

public class AddExerciseFragment extends Fragment {

    private FragmentAddExerciseBinding binding;
    private Repository repository;
    private int workoutId = 1;
    private int exerciseId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false);
        repository = new Repository(requireActivity().getApplication());

        if (getArguments() != null) {
            workoutId = getArguments().getInt("workoutId", 1);
            exerciseId = getArguments().getInt("exerciseId", -1);
            String name = getArguments().getString("exerciseName", "");
            float startingWeight = getArguments().getFloat("startingWeight", 0f);
            String tier = getArguments().getString("tier", "");
            String type = getArguments().getString("type", "");

            // Prefill UI if editing
            if (exerciseId != -1) {
                binding.etExerciseName.setText(name);
                if (startingWeight != 0f) binding.etStartingWeight.setText(String.valueOf(startingWeight));
                if (tier != null) {
                    if (tier.equals("T1")) binding.rgTier.check(R.id.rbT1);
                    else if (tier.equals("T2")) binding.rgTier.check(R.id.rbT2);
                    else binding.rgTier.check(R.id.rbT3);
                }
                if (type != null) {
                    binding.switchType.setChecked(type.equals("UPPER"));
                }
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSaveExercise.setOnClickListener(v -> {
            String name = binding.etExerciseName.getText().toString().trim();
            if (name.isEmpty()) name = "New Exercise";
            double weight = 0;
            try { weight = Double.parseDouble(binding.etStartingWeight.getText().toString()); } catch (Exception ignored) {}

            String tierStr = "T3";
            if (binding.rgTier.getCheckedRadioButtonId() == R.id.rbT1) tierStr = "T1";
            else if (binding.rgTier.getCheckedRadioButtonId() == R.id.rbT2) tierStr = "T2";

            String typeStr = binding.switchType.isChecked() ? "UPPER" : "LOWER";

            if (exerciseId != -1) {
                // Update existing exercise fields (keeps cycle intact)
                repository.updateExerciseFieldsAsync(exerciseId, name, weight, tierStr, typeStr);
            } else {
                Exercise.Tier tier = Exercise.Tier.valueOf(tierStr);
                Exercise.Type type = binding.switchType.isChecked() ? Exercise.Type.UPPER : Exercise.Type.LOWER;
                Cycle cycle = new Cycle(new Iteration(3,15), new Iteration(3,12), new Iteration(3,10));
                Exercise ex = new Exercise(name, tier, type, weight, cycle);
                repository.saveExerciseAsync(ex, workoutId);
            }

            NavHostFragment.findNavController(this).navigateUp();
        });

        binding.btnCancelExercise.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

