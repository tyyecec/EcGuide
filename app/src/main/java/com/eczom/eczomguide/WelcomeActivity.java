package com.eczom.eczomguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @BindView(R.id.activity_welcome)
    RelativeLayout activityWelcome;

    public static final String ISMAIN = "isMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //渐变
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setFillAfter(true);

        //缩放
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);

        //添加动画，1500ms
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.setDuration(1500);

        activityWelcome.startAnimation(set);

        set.setAnimationListener(new GuideAnimationListener());
    }

    private class GuideAnimationListener implements Animation.AnimationListener {
        //动画开始播放时回调
        @Override
        public void onAnimationStart(Animation animation) {

        }

        //动画播放结束时回调
        @Override
        public void onAnimationEnd(Animation animation) {
            //判断是否进过主界面
            boolean isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, ISMAIN);
            Intent intent;
            if (isStartMain) {
                //进过---主界面
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                //没进过---引导界面
                intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            }

            startActivity(intent);
            finish();
        }

        //动画重复播放时回调
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
