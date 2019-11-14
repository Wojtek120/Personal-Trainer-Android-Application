package com.wojtek120.personaltrainer.utils.database;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.LongClickExerciseDialog;
import com.wojtek120.personaltrainer.dialog.LongClickExerciseDialog_;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.adapter.ExercisesAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

@EBean(scope = EBean.Scope.Singleton)
public class ExercisesService {

    private static final String TAG = "ExercisesService";
    private static final String USER_ID_FIELD = "userId";

    private Context context;

    private ArrayList<ExerciseModel> exercises;
    private ArrayList<String> exercisesId;

    private String planId;
    private String dayId;

    private FirebaseFirestore database;

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }


    public void setListViewWithExercises(ListView listView, String planId, String dayId, ProgressBar progressBar, Context context) {

        Log.d(TAG, "setting ListView in user plans");

        this.context = context;
        this.planId = planId;
        this.dayId = dayId;

        CollectionReference exercisesCollectionReference = database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES);
        Query query = exercisesCollectionReference.orderBy("order");

        exercises = new ArrayList<>();
        exercisesId = new ArrayList<>();

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        putExercisesDataToArrays(task.getResult());
                        setExercisesToListView(listView);

                        progressBar.setVisibility(View.GONE);

                    } else {
                        ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }


    /**
     * Put exercises data from documents to arrays
     * Arrays are used to list all exercises
     * and array with id is used to navigate to exercise details
     *
     * @param documents - documents from query
     */
    private void putExercisesDataToArrays(QuerySnapshot documents) {

        for (QueryDocumentSnapshot document : documents) {

            ExerciseModel exercise = document.toObject(ExerciseModel.class);

            Log.d(TAG, "Found exercise" + document.getId() + " => " + exercise.toString());

            exercises.add(exercise);
            exercisesId.add(document.getId());

        }

    }


    /**
     * Gets data from array and set it to ListView
     * also set on item click listener
     *
     * @param listView - ListView to which set data
     */
    private void setExercisesToListView(ListView listView) {

        ExercisesAdapter exercisesAdapter = new ExercisesAdapter(context, exercises);
        listView.setAdapter(exercisesAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> navigateToSelectedExerciseDetails(position));

        listView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            onItemLongClicked(position);
            return true;
        });
    }


    /**
     * Runs dialog when on long item clicked
     *
     * @param position - position of clicked item
     */
    private void onItemLongClicked(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("exercise", exercises.get(position));
        bundle.putString("exerciseId", exercisesId.get(position));

        LongClickExerciseDialog_ longClickExerciseDialog = new LongClickExerciseDialog_();
        longClickExerciseDialog.setArguments(bundle);
        longClickExerciseDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), LongClickExerciseDialog.TAG);
    }


    /**
     * Navigate to exercise details
     *
     * @param position - position of clicked item on ListView
     */
    private void navigateToSelectedExerciseDetails(int position) {

        ExerciseModel exercise = exercises.get(position);

        Log.d(TAG, "Selected " + exercisesId.get(position));


    }

    /**
     * Add new exercise
     *
     * @param exercise - exercise model to add
     */
    public void addNewExercise(ExerciseModel exercise, ProgressBar progressBar, Activity activity) {

        progressBar.setVisibility(View.VISIBLE);

        CollectionReference exercisesCollectionReference = database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES);
        Query query = exercisesCollectionReference.orderBy("order", Query.Direction.DESCENDING).limit(1);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        int newExerciseOrder = getOrderNumber(task.getResult());

                        Log.d(TAG, "New exercise order: " + newExerciseOrder);

                        exercise.setOrder(newExerciseOrder);

                        putNewExerciseToDatabase(exercise, progressBar, activity);


                    } else {
                        ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

    /**
     * Put new exercise to database
     *
     * @param exercise    - exercise model
     * @param progressBar - progress bar to hide
     * @param activity    - activity
     */
    private void putNewExerciseToDatabase(ExerciseModel exercise, ProgressBar progressBar, Activity activity) {

        String id = database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES).document().getId();

        database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES).document(id).set(exercise)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    activity.recreate();
                });


    }


    /**
     * If there is any exercise from query returns its order + 1
     * it's new exercise order
     *
     * @param documents - document from query
     * @return new exercise order
     */
    private int getOrderNumber(QuerySnapshot documents) {

        Log.d(TAG, "Looking for last order number");

        if (documents.isEmpty()) {
            return 1;
        }

        ExerciseModel exercise = new ExerciseModel();

        for (QueryDocumentSnapshot document : documents) {

            Log.d(TAG, document.getId() + " => " + document.getData());

            exercise = document.toObject(ExerciseModel.class);
        }

        return exercise.getOrder() + 1;

    }

    /**
     * Update edited exercise
     *
     * @param exercise    - model with new data
     * @param exerciseId  - edited exercise id
     * @param progressBar - progress bar
     * @param activity    - activity
     */
    public void updateExercise(ExerciseModel exercise, String exerciseId, ProgressBar progressBar, Activity activity) {

        progressBar.setVisibility(View.VISIBLE);

        database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES).document(exerciseId)
                .set(exercise)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    activity.recreate();
                });

    }

    /**
     * Delete exercise with given id
     *
     * @param exerciseId  - id of exercise to delete
     * @param progressBar - progress bar
     * @param activity    - activity
     */
    public void deleteExercise(String exerciseId, ProgressBar progressBar, Activity activity) {

        database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES).document(exerciseId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Exercise deleted");

                    ToastMessage.showMessage(activity, activity.getString(R.string.deleted));
                    progressBar.setVisibility(View.GONE);
                    activity.recreate();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting document", e);

                    ToastMessage.showMessage(activity, activity.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });
    }
}
