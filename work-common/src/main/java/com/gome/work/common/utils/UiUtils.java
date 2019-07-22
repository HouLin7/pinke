
package com.gome.work.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.work.common.R;
import com.gome.work.common.widget.LoadingDialog;
import com.gome.work.core.Constants;
import com.gome.work.core.SystemFramework;
import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.BaseChooseParamInfo;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.event.model.PhotoChooseParamInfo;
import com.gome.work.core.model.im.ConversationInfo;

import java.lang.reflect.Field;

public class UiUtils {

    private static Toast sToast;

    public static void showToast(String text) {
        //这个方法在外层切换线程时有时可能会不能切换到主线程，直接返回时简易处理，
        //再调用这个方法时如果没有切换主线程的代码会吞了这个异常
        if (Looper.myLooper() != Looper.getMainLooper()) return;
        if (sToast == null) {
            sToast = Toast.makeText(SystemFramework.getInstance().getGlobalContext(), text, Toast.LENGTH_LONG);
        } else {
            sToast.setText(text);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void showToast(int id) {
        //这个方法在外层切换线程时有时可能会不能切换到主线程，直接返回时简易处理，
        //再调用这个方法时如果没有切换主线程的代码会吞了这个异常
        if (Looper.myLooper() != Looper.getMainLooper()) return;
        if (sToast == null) {
            sToast = Toast.makeText(SystemFramework.getInstance().getGlobalContext(), SystemFramework.getInstance().getGlobalContext().getString(id), Toast.LENGTH_LONG);
        } else {
            sToast.setText(SystemFramework.getInstance().getGlobalContext().getString(id));
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void showShortToast(String text) {
        if (sToast == null) {
            sToast = Toast.makeText(SystemFramework.getInstance().getGlobalContext(), text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    public static void closeSoftKeyBoard(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isSoftKeyBoardActive(Activity activity) {
        if (activity == null) {
            return false;
        }
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputmanger.isActive(view);
        }
        return false;
    }

    /**
     * 开启软键盘
     *
     * @param activity
     */
    public static void openSoftKeyBoard(Activity activity, View view) {
        if (activity == null) {
            return;
        }
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 软键盘是否显示
     *
     * @param activity
     */
    public static boolean isSoftKeyBoardShowing(Activity activity, View view) {
        if (activity == null || view == null) {
            return false;
        }
        InputMethodManager inputmanger = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputmanger.isActive(view);
    }

    /**
     * 显示缩放动画
     */
    public static void showScaleAnimation(final View view) {
        final AnimationSet set = new AnimationSet(true);
        final ScaleAnimation anim = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final ScaleAnimation anim2 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anim2.setFillAfter(false);
                anim2.setDuration(100);
                anim2.setInterpolator(new LinearInterpolator());
                view.startAnimation(anim2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        anim.setFillAfter(true);
        anim.setDuration(100);
        anim.setInterpolator(new LinearInterpolator());
        view.startAnimation(anim);
    }

    /**
     * 显示透明度
     *
     * @param v
     * @param duration
     * @param visibility
     */
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        v.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    /**
     * 显示缩放动画
     */
    public static void showScaleAnimation(View view, float fromX, float toX, float fromY,
                                          float toY, int duration, boolean fillAfter, Animation.AnimationListener listener) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(fillAfter);
        anim.setDuration(duration);
        // anim.setRepeatCount(1);
        // anim.setRepeatMode(Animation.REVERSE);
        if (null != listener)
            anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    public static Dialog showLoading(Context context, Dialog loadingDialog) {
        if (context != null && context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                if (loadingDialog == null) {
                    loadingDialog = UiUtils.showLoading(context);
                } else if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
            }
        }
        return loadingDialog;
    }

    /**
     * 无提示文本
     */
    public static Dialog showLoading(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, R.style.loading_dialog);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 默认文本正在加载
     */
    public static Dialog showLoadingText(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, R.style.loading_dialog, "正在加载");
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 自定义提示文本
     */
    public static Dialog showLoadingText(Context context, String tip) {
        LoadingDialog loadingDialog = new LoadingDialog(context, R.style.loading_dialog, tip);
        loadingDialog.show();
        return loadingDialog;
    }

    public static void hideDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @TargetApi(21)
    public static void setStatusBar(Activity activity, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
        }
    }

    public static int getVoiceWidth(Context context, int voiceTime) {
        int screenWidth = getScreenWidth(context);
        int minWidth = dip2px(context, 75);
        int voiceMaxWidth = screenWidth - dip2px(context, 60) * 2 - dip2px(context, 13) * 2;
        int voiceWidth = 0;
        voiceWidth = ((voiceMaxWidth - minWidth) / (60 - 2)) * voiceTime + minWidth;

        if (voiceTime < 3) {
            voiceWidth = minWidth;
        }

        if (voiceTime >= 59) {
            voiceWidth = voiceMaxWidth;
        }

        return voiceWidth;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(SystemFramework.getInstance().getGlobalContext(), colorId);
    }

    public static String getResString(int resId) {
        if (SystemFramework.getInstance().getGlobalContext() == null || resId <= 0) return "";
        return SystemFramework.getInstance().getGlobalContext().getResources().getString(resId);
    }

    public static Drawable getDrawable(int resId) {
        if (SystemFramework.getInstance().getGlobalContext() == null || resId <= 0) return null;
        return ContextCompat.getDrawable(SystemFramework.getInstance().getGlobalContext(), resId);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = SystemFramework.getInstance().getGlobalContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            return sbar;
        }
    }

    public static boolean isSupportSettingStatusBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    }

    public static void hideStatusNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }


    /**
     * 根据百分比改变颜色透明度
     */
    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 调用图片选择器
     *
     * @param context
     */
    public static void getSelectPicList(Activity context, int requestCode) {
        PhotoChooseParamInfo info = new PhotoChooseParamInfo(context);
        info.chooseModel = Constants.MODEL_PICK_MULTI;
        info.maxCount = 10;
        info.typeScope = 0;
        info.requestCode = requestCode;
        EventDispatcher.postEvent(EventInfo.FLAG_PHOTO_CHOOSE, info);
    }

    public static void getSelectPicList(Activity context, int typeScope, int requestCode) {
        PhotoChooseParamInfo info = new PhotoChooseParamInfo(context);
        info.chooseModel = Constants.MODEL_PICK_MULTI;
        info.maxCount = 10;
        info.typeScope = typeScope;
        info.requestCode = requestCode;
        EventDispatcher.postEvent(EventInfo.FLAG_PHOTO_CHOOSE, info);
    }

    public static int getResColorValue(Context context, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorResId);
        } else {
            return context.getResources().getColor(colorResId);
        }
    }
}
