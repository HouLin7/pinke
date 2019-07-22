package com.gome.work.core.utils.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.gome.work.core.Constants;
import com.gome.work.core.SystemFramework;

import java.security.GeneralSecurityException;

/**
 * sharepreference value 加密
 * Created by liuletao on 2016/11/12.
 */
public final class EncryptedPreferencesValue {
	private static final String ENCRIPTSUFFIX		= "liu101245le456tao78";//只包含字母和数字
	private static final String ENCRYPTPASSWORD 	= "liuletao20160811";

	private static EncryptedPreferencesValue encryptedPreferences;
	private final SharedPreferences sharedPreferences;
	private final String cryptoKey;
	private final EncryptedEditor                       encryptedEditor;

	public static EncryptedPreferencesValue getInstance() {
		if (encryptedPreferences == null) {
			synchronized (EncryptedPreferencesValue.class) {
				if (encryptedPreferences == null) {
					encryptedPreferences = new EncryptedPreferencesValue.Builder(
							SystemFramework.getInstance().getGlobalContext())
							.withEncryptionPassword(ENCRYPTPASSWORD)
							.withPreferenceName(Constants.PreferKeys.SHARED_PREFERENCES_NAME)
							.build();
				}
			}
		}
		return encryptedPreferences;
	}

	private EncryptedPreferencesValue(Builder builder) {
		this.sharedPreferences = TextUtils.isEmpty(builder.prefsName) ?
				PreferenceManager.getDefaultSharedPreferences(builder.context) :
				builder.context.getSharedPreferences(builder.prefsName, Context.MODE_PRIVATE);
		if (TextUtils.isEmpty(builder.encryptionPassword)) {
			throw new RuntimeException("Unable to initialize EncryptedPreferences! Did you forget to set a password using Builder.withEncryptionPassword" + "" +
											   "(encryptionKey) ?");
		} else {
			this.cryptoKey = builder.encryptionPassword;
		}
		this.encryptedEditor = new EncryptedEditor(this);
	}

	private String encryptString(String message) {
		try {
			String encString = AESCrypt.encrypt(cryptoKey, message);
			return encodeCharset(encString);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String decryptString(String message) {
		try {
			String decString = removeEncoding(message);
			return AESCrypt.decrypt(cryptoKey, decString);
		} catch (GeneralSecurityException e) {
			return null;
		}
	}

	private String removeEncoding(String value) {
		String encodedString = value;
		encodedString = encodedString.replaceFirst(ENCRIPTSUFFIX, "").replaceAll("x0P1Xx", "\\+").replaceAll("x0P2Xx", "/").replaceAll("x0P3Xx", "=");
		return encodedString;
	}

	private String encodeCharset(String value) {
		String encodedString = ENCRIPTSUFFIX + value;
		encodedString = encodedString.replaceAll("\\+", "x0P1Xx").replaceAll("/", "x0P2Xx").replaceAll("=", "x0P3Xx");
		return encodedString;
	}

	private boolean containsEncryptedKey(String encryptedKey) {
		return sharedPreferences.contains(encryptedKey);
	}

	private <T> Object decryptType(String key, Object type, T defaultType) {
		if (TextUtils.isEmpty(key) || !containsEncryptedKey(key)) {
			return defaultType;
		}
		String value;
		try {
			value = sharedPreferences.getString(key, null);
		}catch (ClassCastException e){
			T translateValue = (T)dataTransformatOne(key, type, defaultType);
			encryptDatas(key, String.valueOf(translateValue));
			return translateValue;
		}
		if (TextUtils.isEmpty(value)) {
			return defaultType;
		}
		if (!value.startsWith(ENCRIPTSUFFIX)){
			getEdit().putValue(key, value);
			getEdit().commit();
			return value;
		}
		String orgValue = decryptString(value);
		return dataTransformat(type, defaultType, orgValue);
	}

	private <T> Object dataTransformat(Object type, T defaultType, String orgValue){
		if (TextUtils.isEmpty(orgValue)) {
			return defaultType;
		}
		if (type instanceof String) {
			return orgValue;
		} else if (type instanceof Integer) {
			try {
				return Integer.parseInt(orgValue);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Long) {
			try {
				return Long.parseLong(orgValue);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Float) {
			try {
				return Float.parseFloat(orgValue);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Boolean) {
			return Boolean.parseBoolean(orgValue);
		} else {
			return defaultType;
		}
	}

	private <T> Object dataTransformatOne(String key, Object type, T defaultType){
		if (type instanceof String) {
			return sharedPreferences.getString(key, (String) defaultType);
		} else if (type instanceof Integer) {
			try {
				return sharedPreferences.getInt(key, (Integer) defaultType);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Long) {
			try {
				return sharedPreferences.getLong(key, (Long) defaultType);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Float) {
			try {
				return sharedPreferences.getFloat(key, (Float) defaultType);
			} catch (NumberFormatException e) {
				return defaultType;
			}
		} else if (type instanceof Boolean) {
			return sharedPreferences.getBoolean(key, (Boolean) defaultType);
		} else {
			return defaultType;
		}
	}

	private String encryptDatas(String key, String value){
		if (!value.startsWith(ENCRIPTSUFFIX)){
			getEdit().putValue(key, value);
			getEdit().commit();
			return value;
		}
		return value;
	}

	public int getInt(String key, int defaultValue) {
		return (Integer) decryptType(key, 0, defaultValue);
	}

	public long getLong(String key, long defaultValue) {
		return (Long) decryptType(key, 0L, defaultValue);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return (Boolean) decryptType(key, defaultValue, defaultValue);
	}

	public float getFloat(String key, float defaultValue) {
		return (Float) decryptType(key, 0f, defaultValue);
	}

	public String getString(String key, String defaultValue) {
		return (String) decryptType(key, "", defaultValue);
	}

	public EncryptedEditor getEdit() {
		return encryptedEditor;
	}

	public final class EncryptedEditor {

		private final EncryptedPreferencesValue encryptedPreferences;
		private final SharedPreferences.Editor editor;

		private EncryptedEditor(EncryptedPreferencesValue encryptedPreferences) {
			this.encryptedPreferences = encryptedPreferences;
			this.editor = encryptedPreferences.sharedPreferences.edit();
		}
		private SharedPreferences.Editor editor() {
			return editor;
		}

		private String encryptValue(String value) {
			String encryptedString = encryptedPreferences.encryptString(value);
			return encryptedString;
		}

		private void putValue(String key, String value) {
			editor().putString(key, TextUtils.isEmpty(value) ? value : encryptValue(value));
		}

		public EncryptedEditor putString(String key, String value) {
			putValue(key, value);
			return this;
		}

		public EncryptedEditor putInt(String key, int value) {
			putValue(key, String.valueOf(value));
			return this;
		}

		public EncryptedEditor putLong(String key, long value) {
			putValue(key, String.valueOf(value));
			return this;
		}

		public EncryptedEditor putFloat(String key, float value) {
			putValue(key, String.valueOf(value));
			return this;
		}

		public EncryptedEditor putBoolean(String key, boolean value) {
			putValue(key, String.valueOf(value));
			return this;
		}

		public EncryptedEditor remove(String key) {
			editor().remove(key);
			return this;
		}

		public EncryptedEditor clear() {
			editor().clear();
			return this;
		}
		public void apply() {
			editor().apply();
		}

		public boolean commit() {
			return editor().commit();
		}

	}

	public static final class Builder {

		private final Context context;
		private String encryptionPassword;
		private String prefsName;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder withEncryptionPassword(String encryptionPassword) {
			this.encryptionPassword = encryptionPassword;
			return this;
		}

		public Builder withPreferenceName(String preferenceName) {
			this.prefsName = preferenceName;
			return this;
		}

		public EncryptedPreferencesValue build() {
			return new EncryptedPreferencesValue(this);
		}

	}

}
