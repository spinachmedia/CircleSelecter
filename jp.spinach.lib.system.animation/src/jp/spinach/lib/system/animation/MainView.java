package jp.spinach.lib.system.animation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("HandlerLeak")
public class MainView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {

	Activity activity;
	SurfaceHolder holder;
	Thread thread;
	Resources res;
	Canvas canvas;

	Paint paint;

	WindowAnimation windowAnimation;
	CircleSelecterAnimation cercleSelecterAnimation;

	int scWidth;
	int scHeight;

	public MainView(Activity activity) {
		super(activity);
		holder = getHolder();
		holder.addCallback(this);
		this.activity = activity;
		res = getContext().getResources();
		paint = new Paint();

		//サークルセレクターを初期化。
		//サークルセレクターの表示位置を画面の左下に設定。
		//コンストラクタはこのタイミングで動作させる必要がある。
		//(SurafaceView開始後では、CircleSelecterAnimation内のHandlerと競合してしまい、インスタンス化できない。)
		cercleSelecterAnimation = new CircleSelecterAnimation(
				AnimationType.LeftBottom);
	}

	@Override
	public void run() {

		//画面サイズの取得
		scWidth = getWidth();
		scHeight = getHeight();
		
		//メインのボタンを登録する。
		cercleSelecterAnimation.addMainCircle(R.drawable.s_menu_parent, res);
		//メインボタンのサイズを登録する為に、画面サイズを渡す。
		cercleSelecterAnimation.setMainCircleRect(scWidth, scHeight);
		
		//子要素を追加する。画面のスペースと、メモリの許す限りいくらでも追加可。
		//3～5個を推奨。
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_01, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_02, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_03, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_04, res);

		
		while (thread != null) {
			try {
				canvas = holder.lockCanvas();
				canvas.drawColor(Color.WHITE);

				// サークルボタンの子要素の描画
				for(CircleSelecterChild circle:cercleSelecterAnimation.circleSelecterList){
					canvas.drawBitmap(circle.image,
							circle.drawRect,
							circle.viewRect, paint);
				}
				
				// サークルボタンの親ボタン描画
				canvas.drawBitmap(cercleSelecterAnimation.mainCircle.image,
						cercleSelecterAnimation.mainCircle.matrix,
						paint);
				
			} catch (Exception e) {
				e.printStackTrace();
				thread = null;
				activity.finish();
			} finally {
				try {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				} catch (Exception e) {
					e.printStackTrace();
					thread = null;
					activity.finish();
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float x = event.getX();
			float y = event.getY();
			if (cercleSelecterAnimation.isOpen) {
				cercleSelecterAnimation.startCloseAnimation();
			} else {
				cercleSelecterAnimation.startOpenAnimation();
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}
