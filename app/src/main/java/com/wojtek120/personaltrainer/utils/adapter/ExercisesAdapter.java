package com.wojtek120.personaltrainer.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.ExerciseModel;

import java.util.ArrayList;

public class ExercisesAdapter extends ArrayAdapter<ExerciseModel> {

    private static class ViewHolder {
        TextView name;
        TextView reps;
        TextView weight;
        TextView done;
        TextView sets;
    }

    public ExercisesAdapter(Context context, ArrayList<ExerciseModel> exercises) {
        super(context, R.layout.item_exercise, exercises);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExerciseModel exercise = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_exercise, parent, false);

            viewHolder.name = convertView.findViewById(R.id.nameTv);
            viewHolder.reps = convertView.findViewById(R.id.repsNumber);
            viewHolder.weight = convertView.findViewById(R.id.weightNumber);
            viewHolder.done = convertView.findViewById(R.id.doneNumber);
            viewHolder.sets = convertView.findViewById(R.id.setsNumber);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(exercise.getName());
        viewHolder.reps.setText(Integer.toString(exercise.getReps()));
        viewHolder.weight.setText(exercise.getIntensity());
        viewHolder.done.setText(Integer.toString(exercise.getSetsDone()));
        viewHolder.sets.setText(Integer.toString(exercise.getSets()));

        return convertView;
    }

}
