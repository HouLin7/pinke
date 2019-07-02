package com.gome.work.common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gome.utils.ToastUtil;
import com.gome.work.common.R;
import com.gome.work.common.databinding.ActivityScanBinding;
import com.gome.work.common.utils.RealPathFromUriUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;


/**
 * Create by liupeiquan on 2018/8/29
 */
public class QRCodeActivity extends BaseGomeWorkActivity implements QRCodeView.Delegate {

    private ActivityScanBinding mBinding;
    private int REQUEST_PICK_IMAGE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan);
        mBinding.zbarview.setDelegate(this);

        mBinding.includeToolbar.myToolBar.bindActivity(this, "扫一扫");

        requestPermission(Manifest.permission.CAMERA, new IPermissionRequestCallback() {
            @Override
            public void onFinished(String permission, boolean isSuccess) {
                if (!isSuccess) {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("相册");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getImage();
        return true;
    }


    @Override
    public void onScanQRCodeSuccess(String scanResult) {
        vibrate();
        if (TextUtils.isEmpty(scanResult)) {
            ToastUtil.showToast(this, "无法识别");
            return;
        }
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_DATA, scanResult));
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        super.onBackPressed();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
        List<BarcodeFormat> formatList = new ArrayList<>();
        formatList.add(BarcodeFormat.QR_CODE);
        formatList.add(BarcodeFormat.UPC_A);
        formatList.add(BarcodeFormat.EAN_13);
        formatList.add(BarcodeFormat.CODE_128);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
        hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
        mBinding.zbarview.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型
        mBinding.zbarview.startCamera();//打开相机
        mBinding.zbarview.showScanRect();//显示扫描框
        mBinding.zbarview.startSpotAndShowRect();
    }


    @Override
    protected void onStop() {
        mBinding.zbarview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mBinding.zbarview.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            if (data != null) {
                String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                getScanCode(realPathFromUri);
            } else {
                Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getScanCode(final String realPathFromUri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinding.zbarview.decodeQRCode(realPathFromUri);
            }
        });
    }

}


