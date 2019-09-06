package com.gome.work.common.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.gome.work.common.R
import com.gome.work.common.imageloader.ImageLoader
import com.naver.android.helloyako.imagecrop.view.ImageCropView
import kotlinx.android.synthetic.main.activity_image_crop.*

/**
 * Create by liupeiquan on 2018/10/16
 */
class ImageCropActivity : BaseGomeWorkActivity() {

    var imageFilePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageFilePath = intent.getStringExtra(EXTRA_DATA)
        setContentView(R.layout.activity_image_crop)
        getCustomToolbar(include_toolbar).bindActivity(this, "裁剪")
        imageCropView.setGridInnerMode(ImageCropView.GRID_ON);
        imageCropView.setGridOuterMode(ImageCropView.GRID_ON);
        imageCropView.setImageFilePath(imageFilePath)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val item = menu.add("确定")
//        item.setIcon(R.mipmap.ic_title_more)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = outputBitmap(imageCropView.croppedImage)
        var data = Intent()
        data.putExtra(EXTRA_DATA, result)
        setResult(Activity.RESULT_OK, data)
        finish()
        return super.onOptionsItemSelected(item)
    }

}
