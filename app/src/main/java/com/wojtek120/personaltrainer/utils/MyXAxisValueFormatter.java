package com.wojtek120.personaltrainer.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyXAxisValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");

            return sdf.format(new Date((long) value));
        } catch (Exception e) {
                    return Float.toString(value);
        }
    }
}
