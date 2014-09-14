package jp.spinach.lib.system.animation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;


/**
 * サークルセレクターの１ボタンとなるクラス。
 * インスタンス化して利用する。
 * @author hiroki
 *
 */
public class CircleSelecterChild {
	
	/**
	 * 画像を格納するｌ。
	 */
	public Bitmap image;
	
	/**
	 * アニメーション開始時の描画領域の情報
	 */
	public Rect startRect;
	
	/**
	 * アニメーション終了時の描画領域の情報
	 */
	public Rect endRect;
	
	/**
	 * 実際に描画する領域
	 */
	public Rect viewRect;
	
	/**
	 * 画像データ内のどの領域を描画に利用するかの情報。
	 */
	public Rect drawRect;
	
	/**
	 * 画像を回転させるための情報を格納する。
	 */
	public Matrix matrix;
	
	/**
	 * 描画位置を計算するための値を格納する。
	 * 三角関数の計算に利用する。角度。
	 */
	public int rot;
	
}
