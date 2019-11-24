package com.wojtek120.personaltrainer.utils.database;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.LongClickExerciseDialog;
import com.wojtek120.personaltrainer.dialog.LongClickExerciseDialog_;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.adapter.ExercisesAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class HomeService {
    private static final String TAG = "HomeService";
    private static final String USER_ID_FIELD = "userId";

    private FirebaseFirestore database;

    private Context context;
    private Date date;
    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<ExerciseModel> exercises;
    private ArrayList<String> exercisesId;

    private String planId;
    private String dayId;

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Set list wiew
     *
     * @param listView - ListView in which exercises are put
     * @param dayDateToFind - date in which exercises will be put in ListView
     * @param progressBar - Progress bar
     * @param context - context
     */
    public void setListViewWithExercisesByGivenDate(ListView listView, Date dayDateToFind, ProgressBar progressBar, Context context) {

        this.listView = listView;
        this.context = context;
        this.date = dayDateToFind;
        this.progressBar = progressBar;

        exercises = new ArrayList<>();
        exercisesId = new ArrayList<>();

        Query query = database.collection(DatabaseCollectionNames.PLANS).whereEqualTo(USER_ID_FIELD, AuthenticationFacade.getIdOfCurrentUser());

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
     * Iterate through plans and query for days
     *
     * @param documents - plans documents
     */
    private void iterateThroughPlans(List<DocumentSnapshot> documents) {

        List<Task<QuerySnapshot>> taskList = new ArrayList<>();

        for (DocumentSnapshot document : documents) {

            Log.d(TAG, "Got plan " + document.getId() + document.getData());

            String planId = document.getId();

            Task<QuerySnapshot> task = database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS).get();
            taskList.add(task);

        }

        if (!taskList.isEmpty()) {
            waitForDayQueries(taskList);
        } else {
            progressBar.setVisibility(View.GONE);
        }

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
     * Iterate through days, look for this with given date
     * and create task with queries about exercises,
     *
     * @param querySnapshots - querySnapshots
     */
    private void iterateThroughDays(List<QuerySnapshot> querySnapshots) {

        List<Task<QuerySnapshot>> taskList = new ArrayList<>();

        loop:
        for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                Log.d(TAG, "::allTasksWithDays: addOnSuccessListener:: got " + documentSnapshot.getId() + " :: " + documentSnapshot.getData());

                DayModel dayModel = documentSnapshot.toObject(DayModel.class);

                if (DateUtils.isSameDay(date, dayModel.getDate())) {
                    taskList.add(documentSnapshot.getReference().collection(DatabaseCollectionNames.EXERCISES).orderBy("order").get());

                    planId = documentSnapshot.getReference().getParent().getParent().getId();
                    dayId = documentSnapshot.getId();

                    break loop;
                }
            }
        }

        if (!taskList.isEmpty()) {
            waitForExerciseQueries(taskList);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }


    /**
     * Wait when threads get all exercises and iterate through it to put it in ListView
     *
     * @param taskList - task list
     */
    private void waitForExerciseQueries(List<Task<QuerySnapshot>> taskList) {

        Task<List<QuerySnapshot>> allTasksWithDays = Tasks.whenAllSuccess(taskList);
        allTasksWithDays.addOnSuccessListener(querySnapshots -> {

            Log.d(TAG, "::allTasksWithDays finished");

            iterateThroughExercises(querySnapshots);

        });

    }

    /**
     * Add exercises to list to put it in ListViev
     *
     * @param querySnapshots - querySnapshots
     */
    private void iterateThroughExercises(List<QuerySnapshot> querySnapshots) {

        for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                ExerciseModel exerciseModel = documentSnapshot.toObject(ExerciseModel.class);
                exercises.add(exerciseModel);
                exercisesId.add(documentSnapshot.getId());
            }
        }

        setExercisesToListView();

        progressBar.setVisibility(View.GONE);
    }


    /**
     * Set up ListView with exercises
     */
    private void setExercisesToListView() {

        ExercisesAdapter exercisesAdapter = new ExercisesAdapter(context, exercises, planId, dayId, exercisesId);
        listView.setAdapter(exercisesAdapter);

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

}
