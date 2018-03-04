package com.chivorn.smartmaterialspinner.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Hum Vorn on 3/2/2018.
 */

public class SoftKeyboardUtil {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
