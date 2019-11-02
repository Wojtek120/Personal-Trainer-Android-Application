package com.wojtek120.personaltrainer.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to EditTexts to put maxes in correct form
 * accepts to decimal places and 3 numbers fore dot
 */
public class DecimalInputFilter implements InputFilter {

    private Pattern pattern;

    public DecimalInputFilter() {
        pattern = Pattern.compile("^[1-9]?\\d{0,2}(\\.\\d{0,2})?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length()).toString();

        Matcher matcher = pattern.matcher(newVal);

        if (matcher.matches())
            return null;

        if (TextUtils.isEmpty(source))
            return dest.subSequence(dstart, dend);
        else
            return "";
    }
}
