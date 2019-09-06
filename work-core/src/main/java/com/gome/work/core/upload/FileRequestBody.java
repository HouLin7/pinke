package com.gome.work.core.upload;

import com.liulishuo.okdownload.SpeedCalculator;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class FileRequestBody extends RequestBody {
    /**
     * 实体请求体
     */
    private RequestBody requestBody;

    /**
     * 上传回调接口
     */
    private IProgressChangeListener progressChangeListener;

    private BufferedSink bufferedSink;

    private SpeedCalculator mSpeedCalculator = new SpeedCalculator();

    public FileRequestBody(RequestBody requestBody) {
        this(requestBody, null);
    }


    public FileRequestBody(RequestBody requestBody, IProgressChangeListener callback) {
        super();
        this.requestBody = requestBody;
        this.progressChangeListener = callback;
    }


    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }


    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            requestBody.writeTo(sink);
            return;
        }
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(wrapperSink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }


    /**
     * 写入，回调进度接口
     *
     * @param sink
     * @return
     */
    private Sink wrapperSink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                    if (progressChangeListener != null) {
                        progressChangeListener.onStart();
                    }
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                mSpeedCalculator.downloading(byteCount);
                if (progressChangeListener != null) {
                    progressChangeListener.onProcess(contentLength, bytesWritten, mSpeedCalculator);
                    if (contentLength == bytesWritten) {
                        progressChangeListener.onEnd();
                    }
                }
            }
        };
    }
}
