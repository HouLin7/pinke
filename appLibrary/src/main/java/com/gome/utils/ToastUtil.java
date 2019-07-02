package com.gome.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class ToastUtil {
	
	private  static void showToastImpl(Context context, CharSequence text, int duration) {
		if (TextUtils.isEmpty(text)) {
			text = "";
		}
//		Toast toast = Toast.makeText(context, text, duration);
//		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflate.inflate(R.layout.toast, null);
//		TextView txtView = (TextView) v.findViewById(R.id.txtViewContent);
//		toast.setView(v);
//		txtView.setText(text);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		
		Toast.makeText(context, text, duration).show();;
	}
	
	
	public static void showToast(Context context, CharSequence text) {		
		showToastImpl(context, text, Toast.LENGTH_SHORT);
	}
	
	public static void showToast(Context context, int resId) {
		showToastImpl(context, context.getString(resId), Toast.LENGTH_SHORT);
	}
}

