package com.center.testviewpager;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

public class MainActivity extends Activity
{
    
    private RelativeLayout mRootLayout;
    
    private ViewPager mViewPager;
    
    private int colorBg[];
    
    private ArgbEvaluator mArgbEvaluator;
    
    private int barAlpha = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        mRootLayout = (RelativeLayout)findViewById(R.id.rl_root);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.light_green_500), barAlpha);
        mArgbEvaluator = new ArgbEvaluator();
        colorBg = getResources().getIntArray(R.array.splash_bg);
        final IntroPager introPager = new IntroPager(R.array.splash_icon, R.array.splash_desc);
        mViewPager.setAdapter(introPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int color = (int)mArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],
                    colorBg[(position + 1) % colorBg.length]);
                StatusBarUtil.setColor(MainActivity.this, color, barAlpha);
                mRootLayout.setBackgroundColor(color);
            }
            
            @Override
            public void onPageSelected(int position)
            {
                
            }
            
            @Override
            public void onPageScrollStateChanged(int state)
            {
                
            }
            
        });
        
    }
    
    private class IntroPager extends PagerAdapter
    {
        
        private String[] mDescs;
        
        private TypedArray mIcons;
        
        public IntroPager(int icoImage, int des)
        {
            mDescs = getResources().getStringArray(des);
            mIcons = getResources().obtainTypedArray(icoImage);
        }
        
        @Override
        public int getCount()
        {
            return mIcons.length();
        }
        
        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View itemLayout = getLayoutInflater().inflate(R.layout.layout_app_intro, container, false);
            ImageView mImage = (ImageView)itemLayout.findViewById(R.id.iv_img);
            TextView mTextView = (TextView)itemLayout.findViewById(R.id.tv_desc);
            Button mButton = (Button)itemLayout.findViewById(R.id.btn_launch);
            mImage.setImageResource(mIcons.getResourceId(position, 0));
            mTextView.setText(mDescs[position]);
            if (position == getCount() - 1)
            {
                mButton.setVisibility(View.VISIBLE);
            }
            else
            {
                mButton.setVisibility(View.GONE);
            }
            container.addView(itemLayout);
            return itemLayout;
        }
        
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View)object);
        }
    }
}
