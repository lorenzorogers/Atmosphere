package com.lorenzorogers.atmosphere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Link {
    public static void makeTextViewLink(Activity activity, String name) {
        @SuppressLint("DiscouragedApi") int resId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
        TextView t = activity.findViewById(resId);
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }
}