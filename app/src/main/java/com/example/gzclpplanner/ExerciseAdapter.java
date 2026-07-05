package com.example.gzclpplanner;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gzclpplanner.data.entity.ExerciseEntity;
import com.example.gzclpplanner.databinding.ItemExerciseBinding;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<ExerciseEntity> exercises = new ArrayList<>();
    private final OnExerciseActionListener listener;

    public interface OnExerciseActionListener {
        void onDone(ExerciseEntity exercise);
        void onFailed(ExerciseEntity exercise);
        void onDelete(ExerciseEntity exercise);
        void onEdit(ExerciseEntity exercise);
    }

    public ExerciseAdapter(OnExerciseActionListener listener) {
        this.listener = listener;
    }

    public void setExercises(List<ExerciseEntity> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseBinding binding = ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseEntity exercise = exercises.get(position);
        holder.bind(exercise, position + 1);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ItemExerciseBinding binding;

        ExerciseViewHolder(ItemExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(ExerciseEntity exercise, int number) {
            binding.tvExerciseName.setText(exercise.exerciseName);
            binding.tvExerciseNumber.setText("#" + number);
            binding.tvTier.setText(exercise.tier);
            binding.tvWeight.setText(exercise.currentWeight + " kg");

            binding.tvSets.setText(exercise.sets + " Sets");
            binding.tvReps.setText(exercise.reps + " Reps");

            binding.btnDone.setOnClickListener(v -> listener.onDone(exercise));
            binding.btnFailed.setOnClickListener(v -> listener.onFailed(exercise));
            binding.btnEditExercise.setOnClickListener(v -> listener.onEdit(exercise));
            binding.btnDeleteExercise.setOnClickListener(v -> listener.onDelete(exercise));
        }
    }
}
