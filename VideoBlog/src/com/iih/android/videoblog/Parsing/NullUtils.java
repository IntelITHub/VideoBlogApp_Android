package com.iih.android.videoblog.Parsing;

import java.util.LinkedHashMap;

import android.widget.EditText;
import android.widget.TextView;

public class NullUtils {

	public static boolean isNull(String object) {
		try {
			if (object != null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static void isNullObject(TextView text,
			LinkedHashMap<String, Object> object, String Key) {
		try {
			String str = (String) object.get(Key);
			if (str == null) {
				text.setText("NA");
			} else if (str.equalsIgnoreCase("")) {
				text.setText("NA");
			} else {
				text.setText(str);
			}
		} catch (Exception e) {
			text.setText("NA");
		}
	}

	public static <T> void isNullLabelWithNoValue(T text,
			LinkedHashMap<String, Object> object, String Key) {
		try {
			String str = (String) object.get(Key);
			if (str == null) {
				((TextView) text).setText("");
			} else if (str.equalsIgnoreCase("")) {
				((TextView) text).setText("");
			} else {
				((TextView) text).setText(str);
			}
		} catch (Exception e) {
			((TextView) text).setText("");
		}
	}

	public static <T> void isNullInputBoxWithNoValue(T text,
			LinkedHashMap<String, Object> object, String Key) {
		try {
			String str = (String) object.get(Key);
			if (str == null) {
				((EditText) text).setText("");
			} else if (str.equalsIgnoreCase("")) {
				((EditText) text).setText("");
			} else {
				((EditText) text).setText(str);
			}
		} catch (Exception e) {
			((EditText) text).setText("");
		}
	}
}
