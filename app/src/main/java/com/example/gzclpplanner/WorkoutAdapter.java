package com.example.gzclpplanner;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gzclpplanner.data.entity.ExerciseEntity;
import com.example.gzclpplanner.data.relations.WorkoutWithExercises;
import com.example.gzclpplanner.databinding.ItemWorkoutBinding;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<WorkoutWithExercises> workouts = new ArrayList<>();
    private final OnWorkoutActionListener listener;

    public interface OnWorkoutActionListener {
        void onStartWorkout(WorkoutWithExercises workout);
        void onDeleteWorkout(WorkoutWithExercises workout);
        void onEditWorkout(WorkoutWithExercises workout);
    }

    public WorkoutAdapter(OnWorkoutActionListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWorkouts(List<WorkoutWithExercises> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkoutBinding binding = ItemWorkoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(workouts.get(position));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkoutBinding binding;

        WorkoutViewHolder(ItemWorkoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(WorkoutWithExercises workoutWithExercises) {
            binding.tvWorkoutNumber.setText("Day " + workoutWithExercises.workout.workoutNumber);
            binding.tvWorkoutName.setText(workoutWithExercises.workout.workoutName);

            // Clear previous dynamic exercises
            binding.dynamicExerciseContainer.removeAllViews();

            // Add exercises to the container
            for (ExerciseEntity exercise : workoutWithExercises.exercises) {
                TextView tv = new TextView(binding.getRoot().getContext());
                tv.setText(exercise.exerciseName + ": " + exercise.currentWeight + "kg");
                tv.setPadding(0, 4, 0, 4);
                binding.dynamicExerciseContainer.addView(tv);
            }

            binding.btnGoToWorkout.setOnClickListener(v -> listener.onStartWorkout(workoutWithExercises));
            binding.btnEditWorkout.setOnClickListener(v -> listener.onEditWorkout(workoutWithExercises));
            binding.btnDeleteWorkout.setOnClickListener(v -> listener.onDeleteWorkout(workoutWithExercises));
        }
    }
}
