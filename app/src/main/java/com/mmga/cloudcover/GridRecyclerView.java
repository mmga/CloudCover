package com.mmga.cloudcover;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

public class GridRecyclerView extends RecyclerView {

    public GridRecyclerView(Context context) {
        super(context);
    }

    public GridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new ClassCastException("not a GridLayout Manager");
        }
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutAnimationController.AnimationParameters animationParameters =
                    (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;
            if (animationParameters == null) {
                animationParameters = new GridLayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animationParameters;
            }

            int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

            animationParameters.count = count;
            animationParameters.index = index;
            animationParameters.columnsCount = columns;
            animationParameters.rowsCount = count / columns;

            int invertedIndex = count - 1 - index;
            animationParameters.column = columns - 1 - (invertedIndex % columns);
            animationParameters.row = animationParameters.rowsCount - 1 - invertedIndex / columns;

        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }
}
