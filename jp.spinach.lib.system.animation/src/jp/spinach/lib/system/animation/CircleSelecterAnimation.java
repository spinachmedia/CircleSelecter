package jp.spinach.lib.system.animation;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;


public class CircleSelecterAnimation {
	
	AnimationType type;
	boolean handlerIsRunning = false;
	boolean isOpen = false;
	
	int screenWidth,screenHeight;
	
	/**
	 * �T�[�N���Z���N�^�[�̃��C���{�^��
	 */
	public CircleSelecterParent mainCircle;
	
	/**
	 * �T�[�N���Z���N�^�[�̎q�{�^��
	 */
	public List<CircleSelecterChild> circleSelecterList;

	
	/**
	 * �R���X�g���N�^�B
	 * �e�{�^���̕\���ʒu�����߂�B
	 * @param type
	 */
	public CircleSelecterAnimation(AnimationType type) {
		this.type = type;
		circleSelecterList = new ArrayList<CircleSelecterChild>();
	}
	
	/**
	 * �T�[�N���Z���N�^�̃��C���{�^����o�^����B
	 * @param id
	 * @param res
	 */
	public void addMainCircle(int id,Resources res){
		mainCircle = new CircleSelecterParent();
		mainCircle.image = BitmapFactory.decodeResource(res, id);
		mainCircle.drawRect = new Rect(0, 0, mainCircle.image.getWidth(), mainCircle.image.getHeight());
		mainCircle.matrix = new Matrix();
		mainCircle.matrix.postRotate(0);
	}
	
	/**
	 * ��ʃT�C�Y��n���ă{�^���̕`��̈�����肷��B
	 * addMainCircle�̌�Ɏ��s�����K�v������B
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void setMainCircleRect(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		switch(type){
			case LeftBottom:
				mainCircle.viewRect = new Rect(0, screenHeight - mainCircle.image.getHeight(), mainCircle.image.getWidth(), screenHeight);
				mainCircle.matrix.setTranslate(0, screenHeight - mainCircle.image.getHeight());
				break;
			case CenterBottom:
				//TODO �������˂�
				break;
			case RightBottom:
				//TODO �������˂�
				break;
		}
	}
	
	/**
	 * �T�[�N���Z���N�^�̎q�v�f��ǉ�����B
	 * 2~5�𐄏��B
	 * ������50����Ă݂���OutOfMemory.
	 * @param id
	 * @param res
	 */
	public void addChildCircle(int id,Resources res){
		CircleSelecterChild circle = new CircleSelecterChild();
		circle.image = BitmapFactory.decodeResource(res, id);
		circleSelecterList.add(circle);
		settingPlace();
	}
	
	private void settingPlace(){
		for(int i = 0;i < circleSelecterList.size();i++){
			CircleSelecterChild circle = circleSelecterList.get(i);
			switch(type){
				case LeftBottom:
					
					//�����ʒu�̐ݒ�
					circle.startRect = new Rect(0, screenHeight - circle.image.getHeight(), 0 + circle.image.getWidth(), circle.image.getHeight());
					
					//�`��ʒu�̐ݒ�i�����ʒu�j
					circle.viewRect = new Rect(0, screenHeight - circle.image.getHeight(), 0 + circle.image.getWidth(), screenHeight);
					
					//�摜���̕`��ʒu�̌���
					circle.drawRect = new Rect(0, 0, circle.image.getWidth(), circle.image.getHeight());
					
					//���X�g�̏��Ԃɂ���āA���W��ς���B
					float rot = 90/(circleSelecterList.size() + 1);
					rot = rot * (i+1);
					float rad = (float) (rot * Math.PI / 180);
					double x = Math.sin(rad);
					double y = Math.cos(rad);
					
					//x = 0 �` 1 �̏����B
					//��ʕ��@* x  -  �{�^���̉��� /2 - �e�{�^���̉���  / 2
					int left = (int)((screenWidth*x - circle.image.getWidth()/2) - mainCircle.image.getWidth()/2);
					int top = (int)((-screenWidth*y) + screenHeight - circle.image.getHeight() / 2 + mainCircle.image.getWidth()/2);
					
					//�ŏI�I�Ȉʒu������
					circle.endRect = new Rect(
							left,
							top,
							left + circle.image.getWidth(),
							top + circle.image.getHeight()
							);
					break;
				case CenterBottom:
					//TODO �������˂�
					break;
				case RightBottom:
					//TODO �������˂�
					break;
			}
			
		}
	}
	
	
	
	
	/**
	 * �\���A�j���[�V�����n���h���[�̃L�b�N���\�b�h
	 * ���s���Ƀn���h���[���N���ςŖ������Ƃ�����
	 */
	public void startOpenAnimation() {
		if(handlerIsRunning){
			//��O�ł������邩�E�E�E�H
		}else{
			handlerIsRunning = true;
			for(CircleSelecterChild circle:circleSelecterList){
				circle.rot = 0;
			}
			mainCircle.selfRadious = 0;
			openAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}
	
	/**
	 * �E�B���h�E��������A�j���[�V�����̃n���h���[�L�b�N���\�b�h
	 * ���s���Ƀn���h���[���N���ςŖ������Ƃ�����
	 */
	public void startCloseAnimation() {
		if(handlerIsRunning){
			//��O�ł������邩�E�E�E�H
		}else{
			handlerIsRunning = true;
			for(CircleSelecterChild circle:circleSelecterList){
				circle.rot = 130;
			}
			mainCircle.selfRadious = 90;
			closeAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}

	/**
	 * �T�[�N���Z���N�^�[�A�j���[�V�����n���h���[
	 * 0�x����130�x�܂ł̐����̒l���W���ɂ��ăA�j���[�V�������s���B
	 */
	private final Handler openAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				//�S�A�j���[�V�������I����true�ɂȂ�B
				Boolean handlerIsEnd = false;
				
				//���C���{�^���̉�]�p��ǉ�
				mainCircle.selfRadious++;
				
				//90�x�ɂȂ�܂ŉ�]����������B
				if(mainCircle.selfRadious < 90){
					//�}�g���b�N�X�ɒl��ݒ�
					mainCircle.matrix.postRotate(
						//��]�p��1�x�ǉ�
							1,
						//��]�̒��S��ݒ�B
						//���`�悵�Ă���̈悩��A�摜�̐^�񒆂̈ʒu�܂Œ��S�����炷�B
							mainCircle.viewRect.left + mainCircle.image.getWidth()/2,
							mainCircle.viewRect.top + mainCircle.image.getHeight()/2
						);
				}
				
				
				//�T�[�N���Z���N�^�̎q�v�f��
				for(int i = 0;i < circleSelecterList.size();i++){
					
					CircleSelecterChild circle = circleSelecterList.get(i);
					
					//�ЂƂO�̃{�^���Ƃ�30�x���̍������Ă���A�j���[�V�������X�^�[�g����B
					if(i > 0){
						CircleSelecterChild beforeCircle = circleSelecterList.get(i-1);
						if(circle.rot == 0 
								&& beforeCircle.rot - circle.rot < 30){
							break;
						}
					}
					
					//�{�^���̓����������g��130�x�𒴂������_�œ�����~�߂�B
					//(�A�j���[�V�����I�ɂ͖ړI�n����U�ʂ肷����l�Ɍ�����B)
					if(circle.rot >= 130){
						handlerIsEnd = true;
					}else{
						circle.rot++;
						float rad = (float) (circle.rot * Math.PI / 180);
						
						Rect startRect = circle.startRect;
						Rect endRect = circle.endRect;
						double x = Math.sin(rad);
						int left = startRect.left + (int)((endRect.left - startRect.left) * x);
						int top = startRect.top + (int)((endRect.top - startRect.top) * x);
						circle.viewRect = new Rect(
								left,
								top,
								left + circle.image.getWidth(),
								top + circle.image.getHeight()
								);
						handlerIsEnd = false;
					}
					
				}
				
				//45�x�܂ŏ����B
				if (!handlerIsEnd) {
					sendEmptyMessageDelayed(1, 3);
				}else{
					handlerIsRunning = false;
					isOpen = true;
				}
			} else {
				super.dispatchMessage(msg);
			}
		}
	};
	
	/**
	 * �T�[�N���Z���N�^�[�A�j���[�V�����n���h���[
	 * 130�x����0�x�܂ł̐����̒l���W���ɂ��ăA�j���[�V�������s���B
	 */
	private final Handler closeAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				Boolean handlerIsEnd = false;
				mainCircle.selfRadious--;
				if(mainCircle.selfRadious > 0){
					mainCircle.matrix.postRotate(
						-1,
						mainCircle.viewRect.left + mainCircle.image.getWidth()/2,
						mainCircle.viewRect.top + mainCircle.image.getHeight()/2
						);
				}
				
				for(int i = 0;i < circleSelecterList.size();i++){
					
					CircleSelecterChild circle = circleSelecterList.get(i);
					
					
					//�ЂƂO�̃{�^���Ƃ�30�x�̍������Ă���A�j���[�V�������X�^�[�g����B
					if(i > 0){
						CircleSelecterChild beforeCircle = circleSelecterList.get(i-1);
						if(circle.rot == 130 
								&& circle.rot - beforeCircle.rot < 30){
							break;
						}
					}
					
					//�{�^���̓����������g��0�x�𒴂������_�œ�����~�߂�B
					if(circle.rot <= 0){
						handlerIsEnd = true;
					}else{
						circle.rot = circle.rot - 1;
						float rad = (float) (circle.rot * Math.PI / 180);
						
						Rect startRect = circle.startRect;
						Rect endRect = circle.endRect;
						double x = Math.sin(rad);
						int left = startRect.left + (int)((endRect.left - startRect.left) * x);
						int top = startRect.top + (int)((endRect.top - startRect.top) * x);
						circle.viewRect = new Rect(
								left,
								top,
								left + circle.image.getWidth(),
								top + circle.image.getHeight()
								);
						handlerIsEnd = false;
					}
					
				}
				
				//45�x�܂ŏ����B
				if (!handlerIsEnd) {
					sendEmptyMessageDelayed(1, 3);
				}else{
					handlerIsRunning = false;
					isOpen = false;
				}
			} else {
				super.dispatchMessage(msg);
			}
		}
	};

}
