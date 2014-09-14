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

		//�T�[�N���Z���N�^�[���������B
		//�T�[�N���Z���N�^�[�̕\���ʒu����ʂ̍����ɐݒ�B
		//�R���X�g���N�^�͂��̃^�C�~���O�œ��삳����K�v������B
		//(SurafaceView�J�n��ł́ACircleSelecterAnimation����Handler�Ƌ������Ă��܂��A�C���X�^���X���ł��Ȃ��B)
		cercleSelecterAnimation = new CircleSelecterAnimation(
				AnimationType.LeftBottom);
	}

	@Override
	public void run() {

		//��ʃT�C�Y�̎擾
		scWidth = getWidth();
		scHeight = getHeight();
		
		//���C���̃{�^����o�^����B
		cercleSelecterAnimation.addMainCircle(R.drawable.s_menu_parent, res);
		//���C���{�^���̃T�C�Y��o�^����ׂɁA��ʃT�C�Y��n���B
		cercleSelecterAnimation.setMainCircleRect(scWidth, scHeight);
		
		//�q�v�f��ǉ�����B��ʂ̃X�y�[�X�ƁA�������̋������肢����ł��ǉ��B
		//3�`5�𐄏��B
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_01, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_02, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_03, res);
		cercleSelecterAnimation.addChildCircle(R.drawable.s_menu_child_04, res);

		
		while (thread != null) {
			try {
				canvas = holder.lockCanvas();
				canvas.drawColor(Color.WHITE);

				// �T�[�N���{�^���̎q�v�f�̕`��
				for(CircleSelecterChild circle:cercleSelecterAnimation.circleSelecterList){
					canvas.drawBitmap(circle.image,
							circle.drawRect,
							circle.viewRect, paint);
				}
				
				// �T�[�N���{�^���̐e�{�^���`��
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
