package jp.spinach.lib.system.animation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;


/**
 * �T�[�N���Z���N�^�[�̂P�{�^���ƂȂ�N���X�B
 * �C���X�^���X�����ė��p����B
 * @author hiroki
 *
 */
public class CircleSelecterChild {
	
	/**
	 * �摜���i�[���邌�B
	 */
	public Bitmap image;
	
	/**
	 * �A�j���[�V�����J�n���̕`��̈�̏��
	 */
	public Rect startRect;
	
	/**
	 * �A�j���[�V�����I�����̕`��̈�̏��
	 */
	public Rect endRect;
	
	/**
	 * ���ۂɕ`�悷��̈�
	 */
	public Rect viewRect;
	
	/**
	 * �摜�f�[�^���̂ǂ̗̈��`��ɗ��p���邩�̏��B
	 */
	public Rect drawRect;
	
	/**
	 * �摜����]�����邽�߂̏����i�[����B
	 */
	public Matrix matrix;
	
	/**
	 * �`��ʒu���v�Z���邽�߂̒l���i�[����B
	 * �O�p�֐��̌v�Z�ɗ��p����B�p�x�B
	 */
	public int rot;
	
}
