package com.lorenzorogers.atmosphere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class CustomCardView extends CardView {

    private ImageView icon;
    private TextView title;
    private TextView value;

    public CustomCardView(Context context) {
        super(context);
        init(context);
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyAttributes(context, attrs);
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        applyAttributes(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_card, this, true);
        icon = findViewById(R.id.customCardIcon);
        title = findViewById(R.id.customCardTitle);
        value = findViewById(R.id.customCardValue);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCardView);

        if (a.hasValue(R.styleable.CustomCardView_cardIcon)) {
            int iconResId = a.getResourceId(R.styleable.CustomCardView_cardIcon, -1);
            if (iconResId != -1) {
                icon.setImageResource(iconResId);
            }
        }

        String titleText = a.getString(R.styleable.CustomCardView_cardTitle);
        if (titleText != null) {
            title.setText(titleText);
        }

        String valueText = a.getString(R.styleable.CustomCardView_cardValue);
        if (valueText != null) {
            value.setText(valueText);
        }

        if (a.hasValue(R.styleable.CustomCardView_cardBgColor)) {
            int bgColor = a.getColor(R.styleable.CustomCardView_cardBgColor, Color.WHITE);
            setCardBackgroundColor(bgColor);
        }
        a.recycle();
    }

    public void setCardData(int iconResId, String titleText, String valueText, int bgColorResId) {
        icon.setImageResource(iconResId);
        title.setText(titleText);
        value.setText(valueText);
        setCardBackgroundColor(ContextCompat.getColor(getContext(), bgColorResId));
    }
}
