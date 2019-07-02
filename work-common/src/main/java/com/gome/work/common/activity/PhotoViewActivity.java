package com.gome.work.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gome.applibrary.activity.BaseActivity;
import com.gome.work.common.R;
import com.gome.work.common.fragment.PhotoViewFragment;
import com.gome.work.common.widget.PhotoViewPager;
import com.gome.work.core.Constants;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by chaergongzi on 2018/7/3.
 */

public class PhotoViewActivity extends BaseGomeWorkActivity {

    public static final String EXTRA_DATA_IMAGES_URL = "extra.image.urls";

    public static final String EXTRA_DATA_IMAGES_INDEX = "extra.image.index";

    public static final String EXTRA_DATA_SELECT_FLAG = "extra.select.flag";

    public static final String TAG = PhotoViewActivity.class.getSimpleName();

    private PhotoViewPager mViewPager;

    private MyFragmentAdapter myFragmentAdapter;

    private ImageView mImgViewPreview;

    private ProgressBar mProgressBar;

    private TextView mTvImageCount;

    private List<String> mImageUrls;

    private int currentPosition;

    private static Bitmap mThumbPic;

    private ViewGroup mSceneRootView;

    private CheckBox mCheckBox;

    private View mViewConfirm;

    private String mEditModel = Constants.MODEL_VIEW;

    private HashSet mSelectFlags = new HashSet();

    public static Bitmap getThumbPic() {
        return mThumbPic;
    }

    public static void setThumbPic(Bitmap thumbPic) {
        PhotoViewActivity.mThumbPic = thumbPic;
    }

    public static void startInstance(Activity activity, View view, ArrayList<String> imgUrls, int currentPosition, Bitmap thumbPic) {
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_URL, imgUrls);
        intent.putExtra(PhotoViewActivity.EXTRA_DATA_IMAGES_INDEX, currentPosition);
        setThumbPic(thumbPic);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (view != null) {
//                view.setTransitionName("image");
//            }
//            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "image");
//            activity.startActivity(intent, optionsCompat.toBundle());
//        } else {
        activity.startActivity(intent);
//        }
    }

    public static void startInstance(Activity activity, View view, List<File> fileList, int currentPosition) {
        ArrayList<String> imgUrls = new ArrayList<>();
        for (File item : fileList) {
            imgUrls.add(item.getAbsolutePath());
        }
        startInstance(activity, view, imgUrls, currentPosition, null);
    }

    public static void startInstance(Activity activity, View view, ArrayList<String> imgUrls, int currentPosition) {
        startInstance(activity, view, imgUrls, currentPosition, null);
    }

    public static void startInstance(Activity activity, View view, ArrayList<String> imgUrls) {
        startInstance(activity, view, imgUrls, 0, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        mSceneRootView = findViewById(R.id.root_view);
        mProgressBar = findViewById(R.id.pb_progress);
        mCheckBox = findViewById(R.id.radio_button);
        mViewConfirm = findViewById(R.id.button_confirm);
        initData();

//        initViewForScene1();
        initViewForScene2();
        mProgressBar.setVisibility(View.GONE);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSelectFlags.add(mViewPager.getCurrentItem());
                } else {
                    mSelectFlags.remove(mViewPager.getCurrentItem());
                }
            }
        });
        mViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(BaseActivity.EXTRA_DATA, mSelectFlags);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        if (!Constants.MODEL_VIEW.equals(mEditModel)) {
            mCheckBox.setVisibility(View.VISIBLE);
            mViewConfirm.setVisibility(ViewPager.VISIBLE);
        } else {
            mCheckBox.setVisibility(View.GONE);
            mViewConfirm.setVisibility(View.GONE);
        }

        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1f)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(false)
                .listener(new SlidrListener() {
                    @Override
                    public void onSlideStateChanged(int state) {

                    }

                    @Override
                    public void onSlideChange(float percent) {

                    }

                    @Override
                    public void onSlideOpened() {

                    }

                    @Override
                    public void onSlideClosed() {

                    }
                })
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                .build();

        Slidr.attach(this, config);
    }

    private void initViewForScene1() {
        mImgViewPreview = findViewById(R.id.iv_preview);
        if (mThumbPic != null) {
            mImgViewPreview.setImageBitmap(mThumbPic);
        } else {
            mImgViewPreview.setImageResource(R.drawable.img_default);
        }

        mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(this).load(mImageUrls.get(currentPosition)).override(512, 512).fitCenter()
                .dontAnimate().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(final GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImgViewPreview.setImageDrawable(resource);
                        mProgressBar.setVisibility(View.GONE);
                        Scene scene2 = Scene.getSceneForLayout(mSceneRootView, R.layout.activity_photo_view_scene2, getBaseContext());
                        TransitionManager.go(scene2, new ChangeBounds());
                        initViewForScene2();
                    }
                });
            }
        });

    }


    private void initViewForScene2() {
        mViewPager = findViewById(R.id.view_pager_photo);
        mTvImageCount = findViewById(R.id.tv_image_count);
        mViewPager.setAdapter(myFragmentAdapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + mImageUrls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + mImageUrls.size());
                if (!Constants.MODEL_VIEW.equals(mEditModel)) {
                    mCheckBox.setChecked(mSelectFlags.contains(position));
                }
            }
        });

        if (mImageUrls.size() == 1) {
            mTvImageCount.setVisibility(View.GONE);
        }
    }


    private void initData() {
        Intent intent = getIntent();
        mImageUrls = (List<String>) intent.getSerializableExtra(EXTRA_DATA_IMAGES_URL);
        String model = intent.getStringExtra(Constants.EXTRA_MODEL);
        if (!TextUtils.isEmpty(model)) {
            mEditModel = model;
        }

        if (mImageUrls == null && mImageUrls.isEmpty()) {
            onBackPressed();
            return;
        }

        currentPosition = intent.getIntExtra(EXTRA_DATA_IMAGES_INDEX, 0);
        if (currentPosition < 0 || currentPosition >= mImageUrls.size()) {
            currentPosition = 0;
        }
        myFragmentAdapter = new MyFragmentAdapter(mImageUrls);

        mSelectFlags = (HashSet) intent.getSerializableExtra(EXTRA_DATA_SELECT_FLAG);
        if (mSelectFlags == null) {
            mSelectFlags = new HashSet<>();
        }

        mCheckBox.setChecked(mSelectFlags.contains(currentPosition));

    }

    @Override
    public void onBackPressed() {
//        mSceneRootView.removeAllViews();
//        mSceneRootView.addView(mImgViewPreview);
        super.onBackPressed();
    }


    public class MyFragmentAdapter extends FragmentStatePagerAdapter {

        private List<String> imageUrls;

        public MyFragmentAdapter(List<String> imageUrls) {
            super(getSupportFragmentManager());
            this.imageUrls = imageUrls;
        }

        @Override
        public Fragment getItem(int i) {
            PhotoViewFragment fragment = new PhotoViewFragment();
            Bundle data = new Bundle();
            data.putString(PhotoViewFragment.EXTRA_DATA, imageUrls.get(i));
            data.putString(Constants.EXTRA_MODEL, mEditModel);
            fragment.setArguments(data);
            return fragment;
        }


        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }


    }
}