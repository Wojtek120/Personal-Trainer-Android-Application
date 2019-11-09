package com.wojtek120.personaltrainer.utils.database;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.PlanModel;
import com.wojtek120.personaltrainer.plans.DaysActivity_;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class PlansService {

    private static final String TAG = "PlansService";
    private static final String USER_ID_FIELD = "userId";

    @RootContext
    Context context;

    private ArrayList<String> idOfPlans;
    private ArrayList<String> userPlans;

    private FirebaseFirestore database;
    private Query query;

    @AfterInject
    void setUserPlansService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
        query = database.collection(DatabaseCollectionNames.PLANS).whereEqualTo(USER_ID_FIELD, AuthenticationFacade.getIdOfCurrentUser());
    }


    /**
     * Fill ListView with user plans from db
     *
     * @param listView - ListView to fill with plans
     */
    public void setListViewWithUserPlans(ListView listView, ProgressBar progressBar) {

        Log.d(TAG, "setting ListView in user plans");

        idOfPlans = new ArrayList<>();
        userPlans = new ArrayList<>();

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "success getting plans");

                    putPlansDataToArrays(queryDocumentSnapshots.getDocuments());
                    setPlansToListView(listView);

                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                });

    }




    /**
     * Put data (plan name and id) from documents to arrays
     * Arrays are used to list all plans
     * and array with id is used to navigate to view with days of plan
     *
     * @param documents - documents from query
     */
    private void putPlansDataToArrays(List<DocumentSnapshot> documents) {

        for (DocumentSnapshot document : documents) {

            Log.d(TAG, "Got plan " + document.getId() + document.getData());
            PlanModel plan = document.toObject(PlanModel.class);

            userPlans.add(plan.getName());
            idOfPlans.add(document.getId());

        }

    }


    /**
     * Gets data from array and set it to ListView
     * also set on item click listener
     *
     * @param listView - ListView to which set data
     */
    private void setPlansToListView(ListView listView) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_expandable_list_item_1,
                userPlans);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> navigateToDaysOfSelectedPlan(position));

    }


    /**
     * Navigate to all days list of selected plan
     *
     * @param position - position of clicked item on ListView
     */
    private void navigateToDaysOfSelectedPlan(int position) {
        Log.d(TAG, "Selected " + idOfPlans.get(position));

        Intent intent = new Intent(context, DaysActivity_.class);
        intent.putExtra("planId", idOfPlans.get(position));
        intent.putExtra("planName", userPlans.get(position));
        context.startActivity(intent);

    }
}
