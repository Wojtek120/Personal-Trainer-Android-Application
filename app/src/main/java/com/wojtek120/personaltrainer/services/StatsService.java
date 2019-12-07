package com.wojtek120.personaltrainer.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class StatsService {

    private static final String TAG = "StatsService";
    private static final String USER_ID_FIELD = "userId";

    private Context context;
    private FirebaseFirestore database;

    private LineChart chart;
    private String exerciseName;
    private ProgressBar progressBar;

    private class ExercisesWithData {
        Date date;
        ExerciseModel exerciseModel;
    }

    private class DayWithId {
        String id;
        DayModel dayModel;
    }

    private List<ExercisesWithData> exerciseModels;
    private List<DayWithId> dayList;
    private List<Entry> entryList;

    private OnSetDataToChartListener onSetDataToChartListener;

    public interface OnSetDataToChartListener {
        void setData(List<Entry> entryList);
    }

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Set listener to callback function when data collecting is finished
     *
     * @param activityInstance - activity
     */
    public void setListener(OnSetDataToChartListener activityInstance) {
        onSetDataToChartListener = activityInstance;
    }


    /**
     * Fill chart with data
     *
     * @param chart        - chart view
     * @param exerciseName - exercise name to look for in database
     * @param progressBar  - progress bar
     */
    public void setUpChart(LineChart chart, String exerciseName, ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);

        this.chart = chart;
        this.progressBar = progressBar;
        this.exerciseName = exerciseName;

        exerciseModels = Collections.synchronizedList(new ArrayList<>());
        dayList = Collections.synchronizedList(new ArrayList<>());
        entryList = Collections.synchronizedList(new ArrayList<>());


        getUserPlans();

    }

    /**
     * Get plans and iterate through it to get days and finally exercises which user want to put on chart
     */
    private void getUserPlans() {

        Query query = database.collection(DatabaseCollectionNames.PLANS)
                .whereEqualTo(USER_ID_FIELD, AuthenticationFacade.getIdOfCurrentUser())
                .orderBy("created", Query.Direction.DESCENDING);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "success getting plans");

                    iterateThroughPlans(queryDocumentSnapshots.getDocuments());

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error " + e.toString());
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });
    }

    /**
     * Iterate through plans and create task with queries about days
     *
     * @param documents - documents
     */
    private void iterateThroughPlans(List<DocumentSnapshot> documents) {

        List<Task<QuerySnapshot>> taskList = new ArrayList<>();

        for (DocumentSnapshot document : documents) {

            Log.d(TAG, "Got plan " + document.getId() + document.getData());

            String planId = document.getId();

            Task<QuerySnapshot> task = database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS).orderBy("date").get();
            taskList.add(task);

        }


        waitForDayQueries(taskList);


    }

    /**
     * Wait when threads get all days and iterate through it to get exercises
     *
     * @param taskList - task list with days queries
     */
    private void waitForDayQueries(List<Task<QuerySnapshot>> taskList) {

        Task<List<QuerySnapshot>> allTasksWithDays = Tasks.whenAllSuccess(taskList);
        allTasksWithDays.addOnSuccessListener(querySnapshots -> {

            Log.d(TAG, "::allTasksWithDays finished");

            iterateThroughDays(querySnapshots);

        });
    }


    /**
     * Iterate through days and create task with queries about exercises,
     * also add days to array - used when finding date of exercise
     *
     * @param querySnapshots - querySnapshots
     */
    private void iterateThroughDays(List<QuerySnapshot> querySnapshots) {

        List<Task<QuerySnapshot>> taskList = new ArrayList<>();

        for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                Log.d(TAG, "::allTasksWithDays: addOnSuccessListener:: got " + documentSnapshot.getId() + " :: " + documentSnapshot.getData());

                DayWithId dayWithId = new DayWithId();
                dayWithId.id = documentSnapshot.getId();
                dayWithId.dayModel = documentSnapshot.toObject(DayModel.class);

                dayList.add(dayWithId);

                taskList.add(documentSnapshot.getReference().collection(DatabaseCollectionNames.EXERCISES).whereEqualTo("name", exerciseName).get());
            }
        }

        waitForExerciseQueries(taskList);

    }

    /**
     * Wait to get exercises that match string with its name
     *
     * @param taskList - tasks with queries about exercises
     */
    private void waitForExerciseQueries(List<Task<QuerySnapshot>> taskList) {

        Task<List<QuerySnapshot>> allTasksWithDays = Tasks.whenAllSuccess(taskList);
        allTasksWithDays.addOnSuccessListener(querySnapshots -> {

            Log.d(TAG, "::allTasksWithDays finished");

            iterateThroughExercises(querySnapshots);

        });

    }

    /**
     * Iterate through exercises and put it in arras,
     * also create entry list
     *
     * @param querySnapshots - querySnapshots
     */
    private void iterateThroughExercises(List<QuerySnapshot> querySnapshots) {

        for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                DocumentReference plan = getDocumentReferenceToPlanOfExercise(documentSnapshot);


                Log.d(TAG, "::allTasksWithExercises: got " + documentSnapshot.getId()
                        + " :: " + documentSnapshot.getData() + " parent " + plan.getId());

                ExerciseModel exerciseModel = documentSnapshot.toObject(ExerciseModel.class);

                putExerciseToList(exerciseModel, plan.getId());

            }
        }


        sortDataArray();
        createEntryList();

        setUpChartAndPutDataToIt();
    }


    /**
     * Get plan document reference from exercise document snapshot (in which this exercise is)
     *
     * @param documentSnapshot - documentSnapshot
     * @return - plan document reference
     */
    private DocumentReference getDocumentReferenceToPlanOfExercise(QueryDocumentSnapshot documentSnapshot) {
        return documentSnapshot.getReference().getParent().getParent();
    }

    /**
     * Sort array with exercises by date
     */
    private void sortDataArray() {

        Collections.sort(exerciseModels, (o1, o2) -> {
            if (o1.date == null || o2.date == null)
                return 0;
            return o1.date.compareTo(o2.date);
        });

    }

    /**
     * Put exercise to list with data
     * Checks in which plan exercise is and puts it with data
     *
     * @param exerciseModel - exercise model
     * @param planId        - plan id in which exercise is
     */
    private void putExerciseToList(ExerciseModel exerciseModel, String planId) {

        for (DayWithId dayWithId : dayList) {

            if (dayWithId.id.equals(planId)) {

                Log.d(TAG, "Adding x: " + dayWithId.dayModel.getDate() + " y: " + exerciseModel.getWeight());

                ExercisesWithData exercisesWithData = new ExercisesWithData();

                exercisesWithData.date = dayWithId.dayModel.getDate();
                exercisesWithData.exerciseModel = exerciseModel;

                exerciseModels.add(exercisesWithData);
            }
        }


    }


    /**
     * Create list with entries
     */
    private void createEntryList() {

        for (ExercisesWithData exercisesWithData : exerciseModels) {

            float volume = (float) exercisesWithData.exerciseModel.getWeight() * exercisesWithData.exerciseModel.getSets() * exercisesWithData.exerciseModel.getReps();

            entryList.add(new Entry(exercisesWithData.date.getTime(), volume));

        }

        Log.d(TAG, ":: created entry list " + entryList);

    }


    /**
     * Run callback function to fill chart with data
     */
    private void setUpChartAndPutDataToIt() {

        progressBar.setVisibility(View.GONE);

        Log.d(TAG, "Setting " + entryList);

        if (onSetDataToChartListener != null) {
            onSetDataToChartListener.setData(entryList);
        }

    }

}
