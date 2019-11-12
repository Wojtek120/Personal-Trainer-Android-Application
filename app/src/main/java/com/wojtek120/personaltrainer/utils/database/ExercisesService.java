package com.wojtek120.personaltrainer.utils.database;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.adapter.ExercisesAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean(scope = EBean.Scope.Singleton)
public class ExercisesService {

    private static final String TAG = "ExercisesService";
    private static final String USER_ID_FIELD = "userId";

    @RootContext
    Context context;

    private ArrayList<ExerciseModel> exercises;

    private String planId;
    private String dayId;

    private FirebaseFirestore database;
    private Query query;

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
        query = database.collection(DatabaseCollectionNames.PLANS).whereEqualTo(USER_ID_FIELD, AuthenticationFacade.getIdOfCurrentUser());
    }


    public void setListViewWithExercises(ListView listView, String planId, String dayId, ProgressBar progressBar) {

        Log.d(TAG, "setting ListView in user plans");

        this.planId = planId;

        CollectionReference exercisesCollectionReference = database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .collection(DatabaseCollectionNames.EXERCISES);
        Query query = exercisesCollectionReference.orderBy("order");

        exercises = new ArrayList<>();

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

            Log.d(TAG, document.getId() + " => " + document.getData());

            ExerciseModel exercise = document.toObject(ExerciseModel.class);
            exercise.setExerciseId(document.getId());
            exercises.add(exercise);

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

    }


    /**
     * Navigate to exercise details
     *
     * @param position - position of clicked item on ListView
     */
    private void navigateToSelectedExerciseDetails(int position) {

        ExerciseModel exercise = exercises.get(position);

        Log.d(TAG, "Selected " + exercise.getExerciseId());


    }
}
