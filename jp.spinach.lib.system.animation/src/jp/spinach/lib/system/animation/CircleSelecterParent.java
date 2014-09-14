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
public class CircleSelecterParent {
	
	/**
	 * �摜���i�[����B
	 */
	public Bitmap image;
	
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
	
	/**
	 * �e�v�f�̂ݗ��p����B
	 */
	public float selfRadious;
	
	/**
	 * �A�j���[�V���������ǂ������i�[����B
	 */
	public boolean handlerFlg = false;

}
