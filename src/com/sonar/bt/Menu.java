package com.sonar.bt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {
private Button NAVIGATE, GRAB;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.menu);
        
        NAVIGATE = (Button) findViewById(R.id.navigate);
        NAVIGATE.setOnClickListener(navigateListener);
        
        GRAB = (Button) findViewById(R.id.grab);
        GRAB.setOnClickListener(grabListener);
	}	
	private OnClickListener navigateListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent(Menu.this,NavigateActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener grabListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent(Menu.this,GrabActivity.class);
			startActivity(intent);
		}
	};
}


