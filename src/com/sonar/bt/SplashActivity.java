package com.sonar.bt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        
        ImageView splashImage = (ImageView) findViewById(R.id.imageView1);
        splashImage.setOnClickListener(splashListener);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.custom);
        splashImage.setAnimation(fade);
        
        fade.setAnimationListener(animListener);
        
    }
    
    private OnClickListener splashListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent(SplashActivity.this,Menu.class);
			startActivity(intent);
		}
	};
    
    private AnimationListener animListener =  new AnimationListener() {
		
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		public void onAnimationEnd(Animation animation) {
			Intent intent = new Intent(SplashActivity.this,Menu.class);
			startActivity(intent);
		}
	};
}