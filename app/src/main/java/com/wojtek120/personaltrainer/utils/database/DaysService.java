package com.wojtek120.personaltrainer.utils.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.LongClickDayDialog_;
import com.wojtek120.personaltrainer.dialog.LongClickPlanDialog;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.plans.ExercisesActivity_;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class DaysService {


    private static final String TAG = "DaysService";

    private Context context;

    private List<Map<String, String>> userDays;
    private ArrayList<DayModel> days;
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
    public void setListViewWithDaysOfSelectedPlan(ListView listView, String planId, ProgressBar progressBar, Context context) {

        Log.d(TAG, "setting ListView in user plans");

        this.context = context;
        this.planId = planId;

        idOfDays = new ArrayList<>();
        userDays = new ArrayList<>();
        days = new ArrayList<>();

        Query query = database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS).orderBy("date");

        query.get()
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
            dayMap.put("subtitle", day.giveDateString());
            userDays.add(dayMap);

            idOfDays.add(document.getId());
            days.add(day);

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
                        android.R.id.text2}){

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                text1.setTextColor(Color.BLACK);
                return view;
            }
        };



        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> navigateToSelectedDayActivity(position));

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
        bundle.putSerializable("day", days.get(position));
        bundle.putString("dayId", idOfDays.get(position));

        LongClickDayDialog_ longClickDayDialog = new LongClickDayDialog_();
        longClickDayDialog.setArguments(bundle);
        longClickDayDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), LongClickPlanDialog.TAG);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    /**
     * Update edited day
     *
     * @param day         - day mode to save
     * @param dayId       - edited day id
     * @param progressBar - progress bar
     * @param activity    - activity
     */
    public void updateDay(DayModel day, String dayId, ProgressBar progressBar, Activity activity) {

        progressBar.setVisibility(View.VISIBLE);

        database.collection(DatabaseCollectionNames.PLANS).document(planId).collection(DatabaseCollectionNames.DAYS).document(dayId).set(day)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    activity.recreate();
                });

    }


    /**
     * Delete day with given id
     *
     * @param dayId      - id of day to delete
     * @param progressBar - progress bar
     * @param activity    - activity
     */
    public void deleteDay(String dayId, ProgressBar progressBar, Activity activity) {

        database.collection(DatabaseCollectionNames.PLANS).document(planId)
                .collection(DatabaseCollectionNames.DAYS).document(dayId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Day deleted");

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
