package com.example.demophotoselector.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.demophotoselector.R;
import com.example.demophotoselector.util.ShareElementHelper;

public class PhotoPreviewActivity extends AppCompatActivity {

    public static final String IMAGE_RES_ID = "imageResId";
    public static final String IMAGE_ORIGIN_RECT = "originRect";
    public static final String IMAGE_SCALE_TYPE = "scaleType";

    private ShareElementHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        helper = new ShareElementHelper(this,(ImageView) findViewById(R.id.iv_icon),(FrameLayout) findViewById(R.id.activityContanierDetail));
    }

    @Override
    public void onBackPressed() {
        helper.goBack();
    }

}
