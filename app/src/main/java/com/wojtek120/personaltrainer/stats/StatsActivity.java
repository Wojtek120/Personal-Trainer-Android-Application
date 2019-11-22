package com.wojtek120.personaltrainer.stats;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tiper.MaterialSpinner;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.Exercises;
import com.wojtek120.personaltrainer.utils.MyXAxisValueFormatter;
import com.wojtek120.personaltrainer.utils.database.StatsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity with stats
 *
 * Chart setup taken from:
 * https://medium.com/@leelaprasad4648/creating-linechart-using-mpandroidchart-33632324886d
 */
@EActivity(R.layout.activity_stats)
public class StatsActivity extends AppCompatActivity implements StatsService.OnSetDataToChartListener {
    private final static String TAG = "StatsActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.STATS_ACTIVITY;

    @Bean
    StatsService statsService;
    @Bean
    Exercises exercises;

    @ViewById
    LineChart chart;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    MaterialSpinner spinner;

    @AfterViews
    void setupStats() {

        statsService.setListener(this);

        setUpSpinner();

        setUpChart(new ArrayList<>());

        statsService.setUpChart(chart, getString(R.string.squat_competiton), progressBar);

    }

    /**
     * Set up spinner with exercise names and add on item select listener to it
     */
    private void setUpSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, exercises.getEXERCISES_LIST(this));
        spinner.setAdapter(adapter);

        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                Log.d(TAG, "Selected " + materialSpinner.getSelectedItem().toString());

                statsService.setUpChart(chart, materialSpinner.getSelectedItem().toString(), progressBar);

            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });
    }


    /**
     * Set up chart
     *
     * @param values - list with entries to put in chart
     */
    void setUpChart(List<Entry> values) {

        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        chart.animateY(1000);

        LineDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();

        } else {
            set1 = new LineDataSet(values, getString(R.string.volume));
            set1.setDrawIcons(false);
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormSize(15.f);

            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter());


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);

            Description description = chart.getDescription();
            description.setText(getString(R.string.volume));
//            description.setEnabled(false);

//            Legend legend = chart.getLegend();
//            legend.setEnabled(false);
        }


    }


    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx) {
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, StatsActivity.this, ACTIVITY_NUMBER);
    }

    @Override
    public void setData(List<Entry> entryList) {

        Log.d(TAG, "callback function called " + entryList);

        setUpChart(entryList);

    }
}
