package com.sanjie.zy.widget.indicatior.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;

/**
 * Created by SanJie on 2017/5/16.
 */

public abstract class AbsAnimation<T extends Animator> {

    public static final int DEFAULT_ANIMATION_DURATION = 350;

    long animationDuration = DEFAULT_ANIMATION_DURATION;

    protected ValueAnimation.UpdateListener listener;
    T animator;

    AbsAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createAnimator();
    }

    @NonNull
    public abstract T createAnimator();

    public abstract AbsAnimation progress(float progress);

    public AbsAnimation duration(long duration) {
        animationDuration = duration;

        if (animator instanceof ValueAnimator) {
            animator.setDuration(animationDuration);
        }

        return this;
    }

    public void start() {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null && animator.isStarted()) {
            animator.end();
        }
    }
}
