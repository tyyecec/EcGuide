package com.eczom.eczomguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends Activity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_guide)
    Button btnGuide;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.iv_point)
    ImageView ivPoint;

    private ArrayList<ImageView> imageViews;

    //两白点的间距
    private int distance;
    //白点的直径
    private int diameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        //准备数据源
        int[] ids = new int[]{
                R.drawable.welcom3,
                R.drawable.welcom2,
                R.drawable.welcom1
        };

        //使用封装工具类适配，把单位dip换成px
        diameter = DensityUtil.dip2px(this,15);

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            //设置背景
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            //动态创建指示点，白点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_white);
            //单位为像素，需适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(diameter, diameter);
            //除第一个点外，其他点均离左边的点diameter远
            if (i != 0) {
                params.leftMargin = diameter;
            }
            point.setLayoutParams(params);
            //把白点添加到线性布局
            llPointGroup.addView(point);
        }

        //设置viewpager适配器
        viewpager.setAdapter(new GuidePagerAdapter());

        //监听视图树改变状态，得到当前屏幕信息
        ivPoint.getViewTreeObserver().addOnGlobalLayoutListener(new GuideOnGlobalLayoutListener());

        //监听ViewPager滑动，得到当前页面的位置及屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new GuideOnPageChangeListener());

    }

    @OnClick(R.id.btn_guide)
    public void onClick() {
        //保存'进入过引导页面'数据到Sp
        CacheUtils.putBoolean(GuideActivity.this, WelcomeActivity.ISMAIN, true);
        //跳转到MainActivity
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class GuideOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //两白点的间距 = 第1个点距左屏边缘 - 第0个点距左屏边缘
            distance = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
            //会执行多次，得到数据后移除监听优化性能
            ivPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    private class GuideOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面在滑动时回调
         *
         * @param position             当前页面的位置
         * @param positionOffset       当前页面滑动的百分比
         * @param positionOffsetPixels 当前页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //获取针对在父控件中的View参数，黑点
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPoint.getLayoutParams();
            //黑点滑动距离对应的坐标 = 引导页位置*两白点的间距 + 屏幕滑动百分比*两白点的间距
            params.leftMargin = position * distance + (int) (positionOffset * distance);
            //设置黑点移动距离
            ivPoint.setLayoutParams(params);
        }

        /**
         * 当页面滑动结束后回调
         *
         * @param position 当前页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //滑到最后一个引导页时显示btn
                btnGuide.setVisibility(View.VISIBLE);
            } else {
                btnGuide.setVisibility(View.GONE);
            }
        }

        /**
         * 当页面状态发生改变时回调
         *
         * @param state 三种状态（0，1，2）==（什么都没做，正在滑动，滑动完毕）
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class GuidePagerAdapter extends PagerAdapter {

        /**
         * @return 返回视图的总个数
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 将指定position的视图添加到container中并返回
         *
         * @param container 容器ViewPager
         * @param position  即将实例化的视图位置
         * @return 返回该视图
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        /**
         * 判断view和object是否为同一个View
         *
         * @param view   当前视图
         * @param object instantiateItem()返回的结果值，即imageView
         * @return 判断结果
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 将指定position的视图从container中移除
         *
         * @param container 容器ViewPager
         * @param position  即将销毁的视图位置
         * @param object    即将销毁的视图
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}