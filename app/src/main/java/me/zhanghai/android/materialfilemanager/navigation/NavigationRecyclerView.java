/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialfilemanager.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.math.MathUtils;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.WindowInsets;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import me.zhanghai.android.materialfilemanager.R;
import me.zhanghai.android.materialfilemanager.util.ViewUtils;

public class NavigationRecyclerView extends RecyclerView {

    @BindDimen(R.dimen.design_navigation_elevation)
    float mElevation;
    @BindDimen(R.dimen.design_navigation_max_width)
    int mMaxWidth;
    @BindDimen(R.dimen.design_navigation_padding_bottom)
    int mVerticalPadding;
    @BindDrawable(R.color.system_window_scrim)
    Drawable mScrim;
    private int mActionBarSize;

    private int mInsetTop;

    public NavigationRecyclerView(Context context) {
        super(context);

        init();
    }

    public NavigationRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NavigationRecyclerView(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mActionBarSize = ViewUtils.getDimensionPixelSizeFromAttrRes(R.attr.actionBarSize, 0,
                getContext());
        setPadding(getPaddingLeft(), mVerticalPadding, getPaddingRight(), mVerticalPadding);
        setElevation(mElevation);
        setFitsSystemWindows(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int maxWidth = MathUtils.clamp(ViewUtils.getDisplayWidth(getContext()) - mActionBarSize, 0,
                mMaxWidth);
        switch (MeasureSpec.getMode(widthSpec)) {
            case MeasureSpec.AT_MOST:
                maxWidth = Math.min(maxWidth, MeasureSpec.getSize(widthSpec));
                // Fall through!
            case MeasureSpec.UNSPECIFIED:
                widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                break;
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        mInsetTop = insets.getSystemWindowInsetTop();
        setPadding(getPaddingLeft(), mVerticalPadding + mInsetTop, getPaddingRight(),
                mVerticalPadding);
        return insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), 0,
                insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int saveCount = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        mScrim.setBounds(0, 0, getWidth(), mInsetTop);
        mScrim.draw(canvas);
        canvas.restoreToCount(saveCount);
    }
}
