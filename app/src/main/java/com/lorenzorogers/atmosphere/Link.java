package com.lorenzorogers.atmosphere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Link {
    public static void makeTextViewLink(final Activity activity, String name) {
        @SuppressLint("DiscouragedApi")
        int resId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
        TextView t = activity.findViewById(resId);

        // Set the movement method to make the TextView clickable
        t.setMovementMethod(LinkMovementMethod.getInstance());

        // Set a click listener to open the link
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = activity.getString(R.string.openmeteoLink); // Retrieve the URL from string resources
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); // Create an Intent to open the URL
                activity.startActivity(intent); // Start the activity
            }
        });
    }
}
