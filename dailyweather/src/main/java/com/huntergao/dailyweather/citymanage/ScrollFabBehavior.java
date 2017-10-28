package com.huntergao.dailyweather.citymanage;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by HG on 2017/10/26.
 */

public class ScrollFabBehavior extends FloatingActionButton.Behavior {
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                  View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if (dy < 0 && child.getVisibility() != View.VISIBLE){
            child.show();
        }
    }
}
