package com.bhavadeep.googleclustering.ui.animation;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * Created by ${Bhavadeep} on 1/2/2018.
 */

public class CustomConstraintLayout extends ConstraintLayout {
    public CustomConstraintLayout(Context context) {
        super(context);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getCustomY() {
        final int height = getHeight();
        if (height != 0) return getY() / getHeight();
        else return getY();
    }

    public void setCustomY(float yFraction) {
        final int height = getMeasuredHeight();
        float newHeight = (height > 0) ? (yFraction * height) : -9999;
        setY(newHeight);
    }
}
