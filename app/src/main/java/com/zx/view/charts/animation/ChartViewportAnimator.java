package com.zx.view.charts.animation;

import com.zx.view.charts.model.Viewport;

public interface ChartViewportAnimator
{

    int FAST_ANIMATION_DURATION = 300;

    void startAnimation(Viewport startViewport, Viewport targetViewport);

    void startAnimation(Viewport startViewport, Viewport targetViewport, long duration);

    void cancelAnimation();

    boolean isAnimationStarted();

    void setChartAnimationListener(ChartAnimationListener animationListener);

}
