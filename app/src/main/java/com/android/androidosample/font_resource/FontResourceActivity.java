package com.android.androidosample.font_resource;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.androidosample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FontResourceActivity extends AppCompatActivity {
    @BindView(R.id.tv_font_from_code)
    TextView mTVFontFromCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_resource);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_apply_font)
    public void applyFont(View view) {
        Typeface typeface = getResources().getFont(R.font.sample_font);
        mTVFontFromCode.setTypeface(typeface);
    }
}
