package com.gome.work.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author tanxingchun
 */
public class SubThreadManager {
	private static final int THREAD_COUNT = 15;

	private static SubThreadManager instance = null;

	private ExecutorService mService = null;

	private SubThreadManager() {
		mService = Executors.newFixedThreadPool(THREAD_COUNT);
	}

	public static SubThreadManager getInstance() {

		if (instance == null) {
			synchronized (SubThreadManager.class) {
				if (instance == null)
					instance = new SubThreadManager();
			}

		}
		return instance;
	}

	public void fetchData(Runnable request) {
		mService.submit(request);
	}

	public ExecutorService getmService() {
		return mService;
	}
}
