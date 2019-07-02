package com.gome.work.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by wangchao on 17/7/13.
 */

public class AnimatorRelativeLayout extends RelativeLayout {


    private boolean end = false;

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    private EndListener mEndListener = null;


    public AnimatorRelativeLayout(Context context) {
        super(context);
    }

    public AnimatorRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatorRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initAndStart(AnimatorParam beanArrayList) {
        ArrayList<AnimatorParam> list = new ArrayList<AnimatorParam>();
        list.add(beanArrayList);
        initAndStart(list);
    }

    public void initAndStart(ArrayList<AnimatorParam> beanArrayList) {
        if (beanArrayList == null || beanArrayList.size() <= 0) {
            return;
        }
        this.end = false;

        this.removeAllViews();

        TreeMap<Integer, ArrayList<AnimatorSet>> map = new TreeMap();

        for (int i = 0; i < beanArrayList.size(); i++) {

            AnimatorParam fashAnimatorBean = beanArrayList.get(i);

            //初始化view
            View v = addView(fashAnimatorBean);

            //设置对应view动画
            AnimatorSet anim = setAnimatorSet(v, fashAnimatorBean.getAniType(), fashAnimatorBean.duration);

            //按step将动画分组
            if (anim != null) {
                int step = fashAnimatorBean.getStep();
                ArrayList<AnimatorSet> animatorSetList = map.get(step);
                if (animatorSetList != null && animatorSetList.size() > 0) {
                    animatorSetList.add(anim);
                } else {
                    animatorSetList = new ArrayList();
                    animatorSetList.add(anim);
                    map.put(step, animatorSetList);
                }
            } else {
                throw new RuntimeException("animator format is error!");
            }
        }


        //开始依次分组执行动画
        Iterator iterator = map.keySet().iterator();
        nextStep(iterator, map);
    }

    private View addView(AnimatorParam animatorParam) {
        if (0 == animatorParam.getSourceId()) {
            throw new RuntimeException("resource id must be not null!");
        }
        RelativeLayout.LayoutParams param = animatorParam.getParams();
        ImageView im = new ImageView(this.getContext());
        im.setImageResource(animatorParam.getSourceId());
        this.addView(im, param);
        if (animatorParam.initAlphaIsZero) {
            AnimatorSet anim = setAnimatorSet(im, 4, animatorParam.duration);
            if (anim != null) {
                anim.setDuration(0).start();
            }
        }
        im.setOnClickListener(animatorParam.getListener());
        return im;
    }

    private void nextStep(final Iterator iterator, final TreeMap<Integer, ArrayList<AnimatorSet>> map) {
        if (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            AnimatorSet animSet = new AnimatorSet();
            ArrayList<AnimatorSet> value = map.get(key);
            if (value != null && value.size() > 0) {
                if (value.size() == 1) {
                    animSet.play(value.get(0));
                } else {
                    for (int i = 0; i < value.size() - 1; i++) {
                        animSet.play(value.get(i)).with(value.get(i + 1));
                    }
                }
                animSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        nextStep(iterator, map);
                    }

                });
                animSet.start();
            }
        } else {
            end = true;
            if (mEndListener != null) {
                mEndListener.onEnd();
            }
        }
    }


    private AnimatorSet setAnimatorSet(View v, int type, int duration) {
        if (v != null && type >= 0) {
            AnimatorSet set = new AnimatorSet();
            switch (type) {
                case 0://迅速显示
                    set.setDuration(duration).play(ObjectAnimator.ofFloat(v, "alpha",
                            1f, 1f));
                    break;
                case 1://缓慢显示
                    set.setDuration(duration).play(ObjectAnimator.ofFloat(v, "alpha",
                            0.2f, 1f, 1f));
                    break;
                case 2://快速显示
                    set.setDuration(duration).play(ObjectAnimator.ofFloat(v, "alpha", 0f, 1f));
                    break;
                case 3://左右旋转抖动
                    set.setDuration(duration).setDuration(0).play(ObjectAnimator.ofFloat(v, "alpha",
                            1f, 1f));
                    set.setDuration(1000).playTogether(ObjectAnimator.ofFloat(v, "rotation",
                            15f, -15f, 4f, -4f, 4f, -4f, 4f, -4f, 0f));
                    break;
                case 4://立即消失
                    set.play(ObjectAnimator.ofFloat(v, "alpha",
                            0f, 0f));
                    break;

            }
            return set;
        } else {
            return null;
        }
    }

    public void setEndListener(EndListener endListener) {
        this.mEndListener = endListener;
    }


    public static class AnimatorParam {
        public static final int LEFT = 1001;
        public static final int TOP = 1002;
        public static final int RIGHT = 1003;
        public static final int BOTTOM = 1004;
        public static final int CENTER_HORIZONTAL = 10001;
        public static final int CENTER_VERTICAL = 10002;

        private boolean initAlphaIsZero = true;

        private int sourceId;


        private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        private int aniType = 0;

        private View.OnClickListener listener = null;

        private int step = 0;


        private int duration = 1000;

        public boolean isInitAlphaIsZero() {
            return initAlphaIsZero;
        }

        public void setInitAlphaIsZero(boolean initAlphaIsZero) {
            this.initAlphaIsZero = initAlphaIsZero;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public View.OnClickListener getListener() {
            return listener;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        public int getSourceId() {

            return sourceId;
        }

        public void setSourceId(int sourceId) {
            this.sourceId = sourceId;
        }

        public RelativeLayout.LayoutParams getParams() {
            return params;
        }

        public void setParams(RelativeLayout.LayoutParams params) {
            this.params = params;
        }

        public int getAniType() {
            return aniType;
        }

        public void setAniType(int aniType) {
            this.aniType = aniType;
        }

        public AnimatorParam sourceId(int sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public AnimatorParam params(int leftOrRight, int topOrBottom,
                                    int left, int top, int right, int bottom) {

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            switch (leftOrRight) {
                case LEFT:
                    param.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    break;
                case RIGHT:
                    param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    break;
                case CENTER_HORIZONTAL:
                    param.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    break;
            }

            switch (topOrBottom) {
                case TOP:
                    param.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    break;
                case BOTTOM:
                    param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    break;
                case CENTER_VERTICAL:
                    param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    break;
            }

            if (leftOrRight == CENTER_HORIZONTAL && topOrBottom == CENTER_VERTICAL) {
                param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            }

            param.setMargins(left, top, right, bottom);
            this.params = param;
            return this;
        }

        public AnimatorParam aniType(int aniType) {
            this.aniType = aniType;
            return this;
        }

        public AnimatorParam step(int step) {
            this.step = step;
            return this;
        }

        public AnimatorParam duration(int duration) {
            this.duration = duration;
            return this;
        }

        public AnimatorParam listener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public AnimatorParam aniType(boolean initAlphaIsZero) {
            this.initAlphaIsZero = initAlphaIsZero;
            return this;
        }
    }


    public interface EndListener {
        void onEnd();
    }

}
