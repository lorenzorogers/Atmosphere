package com.lorenzorogers.atmosphere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Link {

    public static void makeTextViewLink(final Activity activity, String name) {
        @SuppressLint("DiscouragedApi")
        int resId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());

        Log.d("Link", "TextView ID: " + resId);

        TextView t = activity.findViewById(resId);

        if (t == null) {
            Log.e("Link", "TextView not found!");
            return;
        }

        t.setMovementMethod(LinkMovementMethod.getInstance());

        t.setOnClickListener(v -> {
            String url = activity.getString(R.string.openmeteoLink);

            Log.d("Link", "Opening URL: " + url);

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
            } catch (Exception e) {
                Log.e("Link", "Error opening URL", e);
            }
        });
    }
}
