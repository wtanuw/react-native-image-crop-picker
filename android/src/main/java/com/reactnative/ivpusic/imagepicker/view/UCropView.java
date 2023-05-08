package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

import androidx.annotation.NonNull;
import android.util.Log;
import androidx.annotation.IntRange;

public class UCropView extends FrameLayout {
    private float mCropExpandWidth = 0;
    private float mCropExtraPadding = 0;

    private GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    private final RectF mTempRect = new RectF();

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.ucrop_view, this, true);
        mGestureCropImageView = findViewById(R.id.image_view_crop);
        mViewOverlay = findViewById(R.id.view_overlay);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();

        setListenersToViews();
    }

    private void setListenersToViews() {
        mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            @Override
            public void onCropRectUpdated(RectF cropRect) {
            //   Log.d("myTag", "onCropRectUpdated"+cropRect);
              mTempRect.set(cropRect);
                mGestureCropImageView.setCropRect(mTempRect);
            }
            @Override
            public boolean onCropRectShouldUpdated(RectF cropRect) {
            //   Log.d("myTag", "onCropRectShouldUpdated"+cropRect);
              mTempRect.set(cropRect);
                return mGestureCropImageView.isValidCropRect(mTempRect);
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

    /**
     * Method for reset state for UCropImageView such as rotation, scale, translation.
     * Be careful: this method recreate UCropImageView instance and reattach it to layout.
     */
    public void resetCropImageView() {
        removeView(mGestureCropImageView);
        mGestureCropImageView = new GestureCropImageView(getContext());
        setListenersToViews();
        mGestureCropImageView.setCropRect(getOverlayView().getCropViewRect());
        addView(mGestureCropImageView, 0);
    }
    
    public void setCropExpandWidthPadding(@IntRange(from = 0) int width, @IntRange(from = 0) int padding) {
        float unit = getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_rect_corner_touch_unit);
        mCropExpandWidth = width*unit;

        int totalpadding = (int)(padding*unit)+getResources().getDimensionPixelSize(R.dimen.ucrop_padding_crop_frame);
        int horizontal = totalpadding+(int)mCropExpandWidth;
        int vertical = padding;
        mViewOverlay.setPadding(horizontal,vertical,horizontal,vertical);
        mGestureCropImageView.setPadding(horizontal,vertical,horizontal,vertical);
    }
}