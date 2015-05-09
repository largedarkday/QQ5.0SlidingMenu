package com.example.qqslidingmenu;

import com.example.qqslidingmenu.view.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
	
	private SlidingMenu slidingMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		
		//去除边缘蓝色发光效果
		//slidingMenu.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	
	public void slidingmenu(View v){
		slidingMenu.toggleMenu();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(slidingMenu.isMenuOpen()){
			slidingMenu.toggleMenu();
			return;
		}
		
		super.onBackPressed();
	}

}
