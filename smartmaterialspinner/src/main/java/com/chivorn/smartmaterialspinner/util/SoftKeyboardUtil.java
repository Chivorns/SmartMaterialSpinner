package com.chivorn.smartmaterialspinner.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtil {
    public static void hideSoftKeyboard(Context mContext) {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}