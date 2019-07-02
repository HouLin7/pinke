
package com.gome.work.common.utils;

import android.content.res.ColorStateList;
import android.text.TextUtils;

import com.gome.work.common.R;
import com.gome.work.core.SystemFramework;

/**
 * ch 2016/6/6
 */
public class ColorUtils {
    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    private static int defaultColor = R.color.bg_user_color7;
    private static int[] colors = {
            R.color.bg_user_color1, R.color.bg_user_color2
            , R.color.bg_user_color3, R.color.bg_user_color4
            , R.color.bg_user_color5, R.color.bg_user_color6
            , R.color.bg_user_color7, R.color.bg_user_color8
            , R.color.bg_user_color9
    };

    public static int genGroupHeadColor(String groupId) {
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(groupId.trim())) {
            return defaultColor;
        }
        return genUserHeadColor(Math.abs(groupId.hashCode()));
    }

    public static int genUserHeadColor(long id) {
        id = id < 0 ? 0 : id;
        int i = ((int) id) % 9;
        return SystemFramework.getInstance().getGlobalContext().getResources().getColor(colors[i]);
    }

    public static int genUserHeadColor(String id) {
        return genUserHeadColor(Integer.parseInt(id));
    }

    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {
                        -ENABLE_ATTR, CHECKED_ATTR
                },
                {
                        -ENABLE_ATTR
                },
                {
                        PRESSED_ATTR, -CHECKED_ATTR
                },
                {
                        PRESSED_ATTR, CHECKED_ATTR
                },
                {
                        CHECKED_ATTR
                },
                {
                        -CHECKED_ATTR
                }
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {
                        -ENABLE_ATTR, CHECKED_ATTR
                },
                {
                        -ENABLE_ATTR
                },
                {
                        CHECKED_ATTR, PRESSED_ATTR
                },
                {
                        -CHECKED_ATTR, PRESSED_ATTR
                },
                {
                        CHECKED_ATTR
                },
                {
                        -CHECKED_ATTR
                }
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }
}
