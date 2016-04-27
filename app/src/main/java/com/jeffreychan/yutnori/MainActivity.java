package com.jeffreychan.yutnori;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	ImageView penguinJumpImageView, sealJumpImageView;
	AnimationDrawable penguinJumpAnimation, sealJumpAnimation;
	Button startButton, helpButton, quitButton, twoPlayerButton, onePlayerButton, backButton;
	int width, height;
	RelativeLayout rl;
	TextView loading;
	Context context = this;
	TranslateAnimation leftToRight, rightToLeft;
	boolean isLeft = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.titlescreen);

		rl = (RelativeLayout) findViewById(R.id.rl);

		penguinJumpImageView = (ImageView) findViewById(R.id.penguinjumpimageview);
		penguinJumpAnimation = (AnimationDrawable) penguinJumpImageView.getBackground();
		penguinJumpAnimation.start();

		sealJumpImageView = (ImageView) findViewById(R.id.sealmoveimageview);
		sealJumpAnimation = (AnimationDrawable) sealJumpImageView.getBackground();
		sealJumpAnimation.start();

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		startButton = new Button(this);
		startButton.setBackgroundResource(R.drawable.startbutton);
		startButton.setId(View.generateViewId());
		startButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		startButton.setOnClickListener(this);
		startButton.setX(width / 2 - startButton.getLayoutParams().width / 2);
		startButton.setY(height * 6 / 10);
		rl.addView(startButton);

		helpButton = new Button(this);
		helpButton.setBackgroundResource(R.drawable.howtoplay);
		helpButton.setId(View.generateViewId());
		helpButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		helpButton.setOnClickListener(this);
		helpButton.setX(width / 2 - helpButton.getLayoutParams().width / 2);
		helpButton.setY(height * 7 / 10);
		rl.addView(helpButton);

		quitButton = new Button(this);
		quitButton.setBackgroundResource(R.drawable.quit);
		quitButton.setId(View.generateViewId());
		quitButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		quitButton.setOnClickListener(this);
		quitButton.setX(width / 2 - quitButton.getLayoutParams().width / 2);
		quitButton.setY(height * 8 / 10);
		rl.addView(quitButton);

		onePlayerButton = new Button(this);
		onePlayerButton.setBackgroundResource(R.drawable.oneplayer);
		onePlayerButton.setId(View.generateViewId());
		onePlayerButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		onePlayerButton.setOnClickListener(this);
		onePlayerButton.setX(width + startButton.getX());
		onePlayerButton.setY(startButton.getY());
		rl.addView(onePlayerButton);

		twoPlayerButton = new Button(this);
		twoPlayerButton.setBackgroundResource(R.drawable.twoplayer);
		twoPlayerButton.setId(View.generateViewId());
		twoPlayerButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		twoPlayerButton.setOnClickListener(this);
		twoPlayerButton.setX(width + helpButton.getX());
		twoPlayerButton.setY(helpButton.getY());
		rl.addView(twoPlayerButton);

		backButton = new Button(this);
		backButton.setBackgroundResource(R.drawable.back);
		backButton.setId(View.generateViewId());
		backButton.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		backButton.setOnClickListener(this);
		backButton.setX(width + quitButton.getX());
		backButton.setY(quitButton.getY());
		rl.addView(backButton);

		loading = new TextView(this);
		loading.setId(View.generateViewId());
		loading.setLayoutParams(new RelativeLayout.LayoutParams(width / 2, height / 10));
		loading.setOnClickListener(this);
		loading.setGravity(Gravity.CENTER);
		loading.setTextColor(Color.BLACK);
		loading.setTextSize(20f);
		loading.setX(helpButton.getX());
		loading.setY(helpButton.getY());
		loading.setText(R.string.loading);
		loading.setVisibility(View.INVISIBLE);
		rl.addView(loading);

		int ANIMATION_DURATION = 600;
		rightToLeft = new TranslateAnimation(0, -width, 0, 0);
		rightToLeft.setDuration(ANIMATION_DURATION);
		rightToLeft.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				setInitialButtonClickable(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				shiftButtonsLeft();
				setModeButtonClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing
			}
		});

		leftToRight = new TranslateAnimation(0, width, 0, 0);
		leftToRight.setDuration(ANIMATION_DURATION);
		leftToRight.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				setModeButtonClickable(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				shiftButtonsRight();
				setInitialButtonClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing
			}
		});
	}

	/*
	 * Set the starting buttons to be un-clickable to prevent multiple clicks
	 */
	private void setInitialButtonClickable(boolean b){
		startButton.setClickable(b);
		helpButton.setClickable(b);
		quitButton.setClickable(b);
	}

	/*
	 * Set the mode buttons to be un-clickable to prevent multiple clicks
	 */
	private void setModeButtonClickable(boolean b){
		onePlayerButton.setClickable(b);
		twoPlayerButton.setClickable(b);
		backButton.setClickable(b);
	}

	/*
	 * Sets the new locations of the buttons after the right to left animation ends.
	 * Sets the mode buttons clickable
	 */
	private void shiftButtonsLeft(){
		if (!isLeft) {
			isLeft = true;
			onePlayerButton.setX(width / 2 - startButton.getLayoutParams().width / 2);
			twoPlayerButton.setX(width / 2 - startButton.getLayoutParams().width / 2);
			backButton.setX(width / 2 - startButton.getLayoutParams().width / 2);

			onePlayerButton.setClickable(true);
			twoPlayerButton.setClickable(true);
			backButton.setClickable(true);

			startButton.setX(width / 2 - startButton.getLayoutParams().width / 2 - width);
			helpButton.setX(width / 2 - startButton.getLayoutParams().width / 2 - width);
			quitButton.setX(width / 2 - startButton.getLayoutParams().width / 2 - width);
		}
	}

	/*
	 * Sets the new locations of the buttons after the left to right animation ends.
	 * Sets the starting buttons clickable
	 */
	private void shiftButtonsRight(){
		if (isLeft){
			isLeft = false;
			startButton.setX(width/2 - startButton.getLayoutParams().width / 2);
			helpButton.setX(width/2 - startButton.getLayoutParams().width / 2);
			quitButton.setX(width/2 - startButton.getLayoutParams().width / 2);

			startButton.setClickable(true);
			helpButton.setClickable(true);
			quitButton.setClickable(true);

			onePlayerButton.setX(width + width/2 - startButton.getLayoutParams().width / 2);
			twoPlayerButton.setX(width + width/2 - startButton.getLayoutParams().width / 2);
			backButton.setX(width + width/2 - startButton.getLayoutParams().width / 2);
		}
	}

	@Override
	public void onBackPressed() {
		if (isLeft) showInitialButtons();
		else finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == onePlayerButton.getId() || v.getId() == twoPlayerButton.getId()) {
			showLoading();

			final boolean isOnePlayer = (v.getId() == onePlayerButton.getId());

			Handler handler = new Handler();
			handler.post(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(context, BoardActivity.class);
					intent.putExtra("Computer", isOnePlayer);
					startActivity(intent);
					finish();
				}
			});
		}
		else if (v.getId() == startButton.getId()){
			showModeButtons();
		}
		else if (v.getId() == backButton.getId()) {
			showInitialButtons();
		}
		else if (v.getId() == helpButton.getId()){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			ScrollView sv = new ScrollView(this);
			TextView tv = new TextView(this);
			tv.setPadding(0, 40, 0, 40);
			tv.setText(R.string.guide);
			tv.setTextSize(20f);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			sv.addView(tv);
			adb.setView(sv);
			adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			});
			adb.show();
		}
		else if (v.getId() == quitButton.getId()){
			finish();
		}
	}

	private void showInitialButtons(){
		startButton.startAnimation(leftToRight);
		helpButton.startAnimation(leftToRight);
		quitButton.startAnimation(leftToRight);

		twoPlayerButton.startAnimation(leftToRight);
		onePlayerButton.startAnimation(leftToRight);
		backButton.startAnimation(leftToRight);
	}

	private void showModeButtons(){
		startButton.startAnimation(rightToLeft);
		helpButton.startAnimation(rightToLeft);
		quitButton.startAnimation(rightToLeft);

		twoPlayerButton.startAnimation(rightToLeft);
		onePlayerButton.startAnimation(rightToLeft);
		backButton.startAnimation(rightToLeft);
	}

	private void showLoading(){
		startButton.setVisibility(View.INVISIBLE);
		helpButton.setVisibility(View.INVISIBLE);
		quitButton.setVisibility(View.INVISIBLE);
		twoPlayerButton.setVisibility(View.INVISIBLE);
		onePlayerButton.setVisibility(View.INVISIBLE);
		backButton.setVisibility(View.INVISIBLE);

		loading.setVisibility(View.VISIBLE);
	}
}