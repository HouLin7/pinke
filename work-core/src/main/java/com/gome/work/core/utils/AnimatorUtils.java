
package com.gome.work.core.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by szy on 2016/5/10.
 */
public class AnimatorUtils {
    public static Animator getRotationAnimator(View target, float f1, float f2, int duration) {
        Animator animator = ObjectAnimator.ofFloat(target, "rotation", f1, f2);
        animator.setDuration(duration);
        return animator;
    }

    public static Animator getTranslateAnimator(View target, float f1, int duration) {
        Animator animator = ObjectAnimator.ofFloat(target, "translationY", f1);
        animator.setDuration(duration);
        return animator;
    }

    public static TranslateAnimation getTranslateAnimatorX(float fromX, float toX, long duration){
        TranslateAnimation transAnim = new TranslateAnimation(fromX, toX, 0, 0);
        transAnim.setDuration(duration);
        return transAnim;
    }
}
