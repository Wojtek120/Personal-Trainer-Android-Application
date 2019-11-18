package com.wojtek120.personaltrainer.utils.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.model.PlanModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.Date;

@EBean(scope = EBean.Scope.Singleton)
public class PredefinedPlansService {
    private static final String TAG = "PredefinedPlansService";

    private Context context;

    private FirebaseFirestore database;

    private PlanModel planModel;
    private String userPlanId;
    private String planId;


    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }


    /**
     * Copy plan with @param id to user plans
     *
     * @param documentId  - document id to copy
     * @param progressBar - progress bar
     */
    public void copyPlan(String documentId, ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);

        planId = documentId;


        DocumentReference document = database.collection(DatabaseCollectionNames.PREDEFINED_PLANS).document(documentId);
        document.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "success getting plans");

                    savePlans(queryDocumentSnapshots, progressBar);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });
    }


    /**
     * Save selected plan, than get all days from plan
     *
     * @param document    - document from query
     * @param progressBar - progress bar
     */
    private void savePlans(DocumentSnapshot document, ProgressBar progressBar) {

        Log.d(TAG, "Got plan " + document.getId() + document.getData());

        planModel = document.toObject(PlanModel.class);
        planModel.setUserId(AuthenticationFacade.getIdOfCurrentUser());
        planModel.setCreated(new Date());

        userPlanId = database.collection(DatabaseCollectionNames.PLANS).document().getId();

        database.collection(DatabaseCollectionNames.PLANS).document(userPlanId).set(planModel)
                .addOnSuccessListener(aVoid -> {


                    getDaysFromDatabase(progressBar);


                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });

    }


    /**
     * Get days from predefined plan, than save it to user plans
     *
     * @param progressBar - progress bar
     */
    private void getDaysFromDatabase(ProgressBar progressBar) {

        Query query = database.collection(DatabaseCollectionNames.PREDEFINED_PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).orderBy("date");

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "success getting days");
                    saveDays(queryDocumentSnapshots, progressBar);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });

    }


    /**
     * Iterate through all days and save it
     *
     * @param queryDocumentSnapshots - document from query
     * @param progressBar            - progress bar
     */
    private void saveDays(QuerySnapshot queryDocumentSnapshots, ProgressBar progressBar) {

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

            Log.d(TAG, "got day " + document.getId() + document.getData());

            DayModel dayModel = document.toObject(DayModel.class);
            String dayIdInPredefined = document.getId();
            saveOneDay(dayModel, dayIdInPredefined, progressBar);

        }

    }

    /**
     * Save day from predefined plan to user plans
     *
     * @param dayModel          - model to save
     * @param dayIdInPredefined - id of day in predefined plan
     * @param progressBar       - progress bar
     */
    private void saveOneDay(DayModel dayModel, String dayIdInPredefined, ProgressBar progressBar) {

        String dayId = database.collection(DatabaseCollectionNames.PLANS).document(userPlanId).collection(DatabaseCollectionNames.DAYS).document().getId();

        database.collection(DatabaseCollectionNames.PLANS).document(userPlanId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .set(dayModel)
                .addOnSuccessListener(aVoid -> {

                    getExercisesFromDatabase(dayIdInPredefined, dayId, progressBar);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });

    }


    /**
     * Get all exercises form predefined plan and save it to user database
     *
     * @param dayIdInPredefined - id of day in predefined plan
     * @param userDayId         - id of day in user database
     * @param progressBar       - progress bar
     */
    private void getExercisesFromDatabase(String dayIdInPredefined, String userDayId, ProgressBar progressBar) {

        Query query = database.collection(DatabaseCollectionNames.PREDEFINED_PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayIdInPredefined)
                .collection(DatabaseCollectionNames.EXERCISES).orderBy("order");

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "success getting exercises");
                    saveExercises(queryDocumentSnapshots, userDayId, progressBar);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });


    }


    /**
     * Iterate through all exercises and save it
     *
     * @param queryDocumentSnapshots - document from query
     * @param userDayId              - id of day in user database
     * @param progressBar            - progress bar
     */
    private void saveExercises(QuerySnapshot queryDocumentSnapshots, String userDayId, ProgressBar progressBar) {

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

            Log.d(TAG, "got exercise " + document.getId() + document.getData());

            ExerciseModel exerciseModel = document.toObject(ExerciseModel.class);
            saveOneExercise(exerciseModel, userDayId, progressBar);

        }

        progressBar.setVisibility(View.GONE);

    }

    /**
     * Save exercise to user database
     *
     * @param exerciseModel - model of exercise to save
     * @param userDayId     - id of day in user database
     * @param progressBar   - progrss bar
     */
    private void saveOneExercise(ExerciseModel exerciseModel, String userDayId, ProgressBar progressBar) {

        String exerciseId = database.collection(DatabaseCollectionNames.PLANS).document(userPlanId)
                .collection(DatabaseCollectionNames.DAYS).document(userDayId)
                .collection(DatabaseCollectionNames.EXERCISES).document()
                .getId();

        Log.d(TAG, "saving to plan " + userPlanId + " to day " + userDayId + " exercise " + exerciseModel);

        database.collection(DatabaseCollectionNames.PLANS).document(userPlanId)
                .collection(DatabaseCollectionNames.DAYS).document(userDayId)
                .collection(DatabaseCollectionNames.EXERCISES).document(exerciseId)
                .set(exerciseModel)

                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });

    }


}
