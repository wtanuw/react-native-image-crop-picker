package com.yalantis.ucrop.callback;

import android.graphics.RectF;

/**
 * Created by Oleksii Shliama.
 */
public interface OverlayViewChangeListener {

    void onCropRectUpdated(RectF cropRect);
    boolean onCropRectShouldUpdated(RectF cropRect);

}