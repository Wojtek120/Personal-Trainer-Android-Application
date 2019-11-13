package com.wojtek120.personaltrainer.utils;

import android.content.Context;

import com.wojtek120.personaltrainer.R;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Arrays;

@EBean
public class Exercises {

    private ArrayList<String> EXERCISES_LIST;


    public ArrayList<String> getEXERCISES_LIST(Context context) {

        EXERCISES_LIST = new ArrayList<>(Arrays.asList(
                context.getString(R.string.squat_competiton),
                context.getString(R.string.bench_competition),
                context.getString(R.string.deadlift_conventional),
                context.getString(R.string.deadlift_sumo),

                context.getString(R.string.low_bar_paused),
                context.getString(R.string.high_bar),
                context.getString(R.string.high_bar_paused),
                context.getString(R.string.front_squat),
                context.getString(R.string.front_squat_paused),
                context.getString(R.string.pin_squat),
                context.getString(R.string.goblet_squat),

                context.getString(R.string.bench_paused),
                context.getString(R.string.bench_biacromical),
                context.getString(R.string.bench_touch_and_go),
                context.getString(R.string.bench_incline),
                context.getString(R.string.bench_decline),

                context.getString(R.string.deadlift_deficyt),
                context.getString(R.string.deadlift_rack),
                context.getString(R.string.deadlift_romanian),

                context.getString(R.string.rows),
                context.getString(R.string.db_rows),
                context.getString(R.string.pull_up),
                context.getString(R.string.chin_up),
                context.getString(R.string.neutral_grip_pull_up),

                context.getString(R.string.bb_biceps_curl),
                context.getString(R.string.db_biceps_curl),
                context.getString(R.string.db_hammer_biceps_curl),
                context.getString(R.string.hercules),
                context.getString(R.string.rope_curl),

                context.getString(R.string.skull_crushers),
                context.getString(R.string.dips),
                context.getString(R.string.rope_extension)

        ));

        return EXERCISES_LIST;
    }
}
