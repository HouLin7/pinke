
package com.gome.work.core.model.appmarket;

import java.io.Serializable;


public class AppInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1695402319104927349L;
	private String appName;
	private String packageName;
	private boolean isSystemApp;
	private long codesize;
	public long getCodesize() {
		return codesize;
	}
	

	public void setCodesize(long codesize) {
		this.codesize = codesize;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}


	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", isSystemApp=" + isSystemApp + ", codesize=" + codesize
				+ "]";
	}
	
}
