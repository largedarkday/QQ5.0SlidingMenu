package com.example.qqslidingmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.qqslidingmenu.R;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWrapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuWidth;
	// dp
	private int mMenuRightPadding = 50;

	private boolean isMenuOpen;

	private long downTime;
	private long upTime;
	int downX;
	int upX;

	public SlidingMenu(Context context) {
		this(context, null);
	}

	/**
	 * 为使用自定义属性时调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 当使用了自定义的属性时，会调用此方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 获取自定义的属性
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		a.recycle();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		mScreenWidth = outMetrics.widthPixels;

		// 吧sp转换为px
		// mMenuRightPadding = (int)
		// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
		// context.getResources().getDisplayMetrics());
	}

	/**
	 * 设置子view的宽和高 设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		mWrapper = (LinearLayout) getChildAt(0);
		mMenu = (ViewGroup) mWrapper.getChildAt(0);
		mContent = (ViewGroup) mWrapper.getChildAt(1);

		mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
				- mMenuRightPadding;
		mContent.getLayoutParams().width = mScreenWidth;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		this.scrollTo(mMenuWidth, 0);
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		// int downX = (int) ev.getX();
		// int upX = (int) ev.getX();
		switch (action) {
		// DOWN事件返回true之后，速滑的时候，scrollview的滑动速度就变慢了
		case MotionEvent.ACTION_DOWN:
			downTime = System.currentTimeMillis();
			downX = (int) ev.getX();
			return true;
		case MotionEvent.ACTION_UP:
			Log.i("kkkkkkkkkkkkkkk", "action_up");
			upTime = System.currentTimeMillis();
			upX = (int) ev.getX();
			if ((upTime - downTime) < 900 && (upX - downX) > 50) {
				openMenu();
			} else if ((upTime - downTime) < 900 && (upX - downX) < -50) {
				closeMenu();
			} else {
				Log.i("kkkkkkkkkkkkkkk", "else");
				int scrollX = this.getScrollX();
				if (scrollX >= mMenuWidth / 2) {
					this.smoothScrollTo(mMenuWidth, 0);
					isMenuOpen = false;
				} else {
					this.smoothScrollTo(0, 0);
					isMenuOpen = true;
				}
			}

			// 不返回true将不会有滑动效果
			return true;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		// 滑动百分比
		float scroll = l * 1.0f / mMenuWidth;
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scroll * 0.7f);

		float rightScale = 0.8f + 0.2f * scroll;
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

		float leftScale = 1.0f - 0.3f * scroll;
		ViewHelper.setPivotX(mMenu, 0);
		ViewHelper.setPivotY(mMenu, mMenu.getHeight() / 2);
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);

		float leftAlpha = 0.1f + 0.9f * (1 - scroll);
		ViewHelper.setAlpha(mMenu, 1 - scroll);
		
		//设置背景由暗变亮
		this.getBackground().setAlpha((int)((1-scroll)*255));
	}

	public void openMenu() {
		Log.i("kkkkkkkkkkkkkkk", "openMenu");
		if (isMenuOpen)
			return;
		this.smoothScrollTo(0, 0);
		isMenuOpen = true;
	}

	public void closeMenu() {
		Log.i("kkkkkkkkkkkkkkk", "closeMenu");
		if (!isMenuOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isMenuOpen = false;
	}

	public boolean isMenuOpen() {
		return isMenuOpen;
	}

	public void toggleMenu() {
		if (isMenuOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

}
