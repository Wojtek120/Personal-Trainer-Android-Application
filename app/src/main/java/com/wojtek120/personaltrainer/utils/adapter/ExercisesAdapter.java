package com.wojtek120.personaltrainer.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.database.DatabaseCollectionNames;

import java.util.ArrayList;

public class ExercisesAdapter extends ArrayAdapter<ExerciseModel> {

    private static final String TAG = "ExercisesAdapter";

    private static class ViewHolder {
        TextView name;
        TextView reps;
        TextView weight;
        TextView done;
        TextView sets;
        ImageView plus;
        ImageView minus;
    }

    private ArrayList<String> idOfExercises;
    private String planId;
    private String dayId;

    public ExercisesAdapter(Context context, ArrayList<ExerciseModel> exercises,String planId, String dayId, ArrayList<String> idOfExercises) {
        super(context, R.layout.item_exercise, exercises);

        this.idOfExercises = idOfExercises;
        this.planId = planId;
        this.dayId = dayId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExerciseModel exercise = getItem(position);
        String id = idOfExercises.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_exercise, parent, false);

            findCorrespondingFieldsToViewHolder(viewHolder, convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setFieldsWithTextFromModel(viewHolder, exercise);

        setOnClickListeners(viewHolder, id, exercise);


        return convertView;
    }

    private void setOnClickListeners(ViewHolder viewHolder, String exerciseId, ExerciseModel exercise) {

        viewHolder.plus.setOnClickListener(v -> {
            Log.d(TAG, "Clicked increment of " + exerciseId);

            if(exercise.getSetsDone() < exercise.getSets()) {
                incrementSetsDone(exercise, exerciseId, viewHolder.done, 1);
            }
        });

        viewHolder.minus.setOnClickListener(v -> {
            Log.d(TAG, "Clicked decrement of " + exerciseId);

            if(exercise.getSetsDone() > 0) {
                incrementSetsDone(exercise, exerciseId, viewHolder.done, -1);
            }
        });
    }

    private void incrementSetsDone(ExerciseModel exercise, String exerciseId, TextView setsDoneTextView, int numberToIncrement) {

        Log.d(TAG, "Incrementing " + exerciseId );

        DocumentReference document = FirebaseFirestore.getInstance().collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES).document(exerciseId);

        document.update("setsDone", FieldValue.increment(numberToIncrement))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Incremented");

                    exercise.setSetsDone(exercise.getSetsDone() + numberToIncrement);
                    setsDoneTextView.setText(Integer.toString(exercise.getSetsDone()));
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error incrementing", e);
                    Log.d(TAG, getContext().getString(R.string.something_went_wrong));
                });

    }

    private void setFieldsWithTextFromModel(ViewHolder viewHolder, ExerciseModel exercise) {
        viewHolder.name.setText(exercise.getName());
        viewHolder.reps.setText(Integer.toString(exercise.getReps()));
        viewHolder.weight.setText(exercise.getIntensity());
        viewHolder.done.setText(Integer.toString(exercise.getSetsDone()));
        viewHolder.sets.setText(Integer.toString(exercise.getSets()));
    }

    private void findCorrespondingFieldsToViewHolder(ViewHolder viewHolder, View convertView) {
        viewHolder.name = convertView.findViewById(R.id.nameTv);
        viewHolder.reps = convertView.findViewById(R.id.repsNumber);
        viewHolder.weight = convertView.findViewById(R.id.weightNumber);
        viewHolder.done = convertView.findViewById(R.id.doneNumber);
        viewHolder.sets = convertView.findViewById(R.id.setsNumber);
        viewHolder.plus = convertView.findViewById(R.id.incrementDoneSet);
        viewHolder.minus = convertView.findViewById(R.id.decrementDoneSet);
    }

}
