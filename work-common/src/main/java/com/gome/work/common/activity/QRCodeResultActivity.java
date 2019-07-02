package com.gome.work.common.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.gome.work.common.R;
import com.gome.work.common.widget.MyToolbarView;

/**
 * Create by liupeiquan on 2018/9/19
 */
public class QRCodeResultActivity extends BaseGomeWorkActivity {
    private TextView tvResult;
    private String strResult;
    private MyToolbarView mToolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_result);
        mToolbarView = findViewById(R.id.my_tool_bar);
        tvResult = findViewById(R.id.qr_code_result);
        strResult = getIntent().getStringExtra(EXTRA_DATA);
        tvResult.setText(strResult);
        mToolbarView.bindActivity(this, "扫一扫");
    }
}
