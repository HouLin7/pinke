package com.gome.work.core.upload;

import com.liulishuo.okdownload.SpeedCalculator;

public interface IProgressChangeListener {
    /**
     * @param totalBytes    总字节数
     * @param transferBytes 已传输的字节
     */
    public void onProcess(long totalBytes, long transferBytes, SpeedCalculator speed);

    public void onStart();

    public void onEnd();

}
