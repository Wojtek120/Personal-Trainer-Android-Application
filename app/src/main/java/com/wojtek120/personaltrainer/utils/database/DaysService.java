package com.wojtek120.personaltrainer.utils.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.plans.ExercisesActivity_;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class DaysService {


    private static final String TAG = "DaysService";

    @RootContext
    Context context;


    private List<Map<String, String>> userDays;
    private ArrayList<String> idOfDays;

    private FirebaseFirestore database;

    private String planId;

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();

    }


    /**
     * Fill ListView with user plans from db
     *
     * @param listView - ListView to fill with plans
     */
    public void setListViewWithDaysOfSelectedPlan(ListView listView, String planId, ProgressBar progressBar) {

        Log.d(TAG, "setting ListView in user plans");

        this.planId = planId;

        idOfDays = new ArrayList<>();
        userDays = new ArrayList<>();

        CollectionReference daysCollectionReference = database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS);

        daysCollectionReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        putPlansDataToArrays(task.getResult());
                        setPlansToListView(listView);

                        progressBar.setVisibility(View.GONE);

                    } else {
                        ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }


    /**
     * Put data (day name, date and id) from documents to arrays
     * Arrays are used to list all days (name and date)
     * and array with id is used to navigate to view with exercises of selected day
     *
     * @param documents - documents from query
     */
    private void putPlansDataToArrays(QuerySnapshot documents) {

        for (QueryDocumentSnapshot document : documents) {
            Log.d(TAG, document.getId() + " => " + document.getData());


            DayModel day = document.toObject(DayModel.class);

            Map<String, String> dayMap = new HashMap<>(2);
            dayMap.put("title", day.getDescription());
            dayMap.put("subtitle", day.getDateString());
            userDays.add(dayMap);

            idOfDays.add(document.getId());

        }

    }


    /**
     * Gets data from array and set it to ListView
     * also set on item click listener
     *
     * @param listView - ListView to which set data
     */
    private void setPlansToListView(ListView listView) {

        SimpleAdapter adapter = new SimpleAdapter(context, userDays,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "subtitle"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> navigateToSelectedDayActivity(position));

    }


    /**
     * Navigate to all exercises list of selected day
     *
     * @param position - position of clicked item on ListView
     */
    private void navigateToSelectedDayActivity(int position) {
        Log.d(TAG, "Selected " + idOfDays.get(position));


        Intent intent = new Intent(context, ExercisesActivity_.class);
        intent.putExtra("planId", planId);
        intent.putExtra("dayId", idOfDays.get(position));
        intent.putExtra("planName", userDays.get(position).get("title"));
        intent.putExtra("date", userDays.get(position).get("subtitle"));
        context.startActivity(intent);
    }


    /**
     * Add new day to database
     *
     * @param day         - day model to add
     * @param progressBar - progress bar
     * @param activity    - activity
     */
    public void addNewDay(DayModel day, ProgressBar progressBar, Activity activity) {

        progressBar.setVisibility(View.VISIBLE);

        String id = database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document().getId();


        database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS).document(id).set(day)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    activity.recreate();
                });

    }
}
