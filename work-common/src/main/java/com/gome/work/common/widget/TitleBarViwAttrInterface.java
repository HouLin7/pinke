package com.gome.work.common.widget;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liupeiquan on 2018/8/14.
 */
public interface TitleBarViwAttrInterface {

    void setLeftTextSize(int dimensSize);

    void setMidTextSize(int dimensSize);

    void setRightTextSize(int dimensSize);

    void setLeftViewText(CharSequence text);

    void setMidViewText(CharSequence text);

    void setRightViewText(CharSequence text);

    void setLeftTextColor(int color);

    void setMidTextColor(int color);

    void setRightTextColor(int color);

    TextView getLeftView();

    TextView getMidView();

    TextView getRightView();
    TextView getDividerView();
    CircleImageView getCircleImageView();
    ImageView getRightImageView();

    void setLeftCircleIcon(int drawableId);
    void setLeftCircleIcon(Drawable drawableId);
}
