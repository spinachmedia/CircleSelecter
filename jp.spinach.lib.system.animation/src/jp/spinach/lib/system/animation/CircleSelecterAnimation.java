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
	 * サークルセレクターのメインボタン
	 */
	public CircleSelecterParent mainCircle;
	
	/**
	 * サークルセレクターの子ボタン
	 */
	public List<CircleSelecterChild> circleSelecterList;

	
	/**
	 * コンストラクタ。
	 * 親ボタンの表示位置を決める。
	 * @param type
	 */
	public CircleSelecterAnimation(AnimationType type) {
		this.type = type;
		circleSelecterList = new ArrayList<CircleSelecterChild>();
	}
	
	/**
	 * サークルセレクタのメインボタンを登録する。
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
	 * 画面サイズを渡してボタンの描画領域を決定する。
	 * addMainCircleの後に実行される必要がある。
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
				//TODO 実装せねば
				break;
			case RightBottom:
				//TODO 実装せねば
				break;
		}
	}
	
	/**
	 * サークルセレクタの子要素を追加する。
	 * 2~5個を推奨。
	 * 試しに50個やってみたらOutOfMemory.
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
					
					//初期位置の設定
					circle.startRect = new Rect(0, screenHeight - circle.image.getHeight(), 0 + circle.image.getWidth(), circle.image.getHeight());
					
					//描画位置の設定（初期位置）
					circle.viewRect = new Rect(0, screenHeight - circle.image.getHeight(), 0 + circle.image.getWidth(), screenHeight);
					
					//画像内の描画位置の決定
					circle.drawRect = new Rect(0, 0, circle.image.getWidth(), circle.image.getHeight());
					
					//リストの順番によって、座標を変える。
					float rot = 90/(circleSelecterList.size() + 1);
					rot = rot * (i+1);
					float rad = (float) (rot * Math.PI / 180);
					double x = Math.sin(rad);
					double y = Math.cos(rad);
					
					//x = 0 ～ 1 の少数。
					//画面幅　* x  -  ボタンの横幅 /2 - 親ボタンの横幅  / 2
					int left = (int)((screenWidth*x - circle.image.getWidth()/2) - mainCircle.image.getWidth()/2);
					int top = (int)((-screenWidth*y) + screenHeight - circle.image.getHeight() / 2 + mainCircle.image.getWidth()/2);
					
					//最終的な位置を決定
					circle.endRect = new Rect(
							left,
							top,
							left + circle.image.getWidth(),
							top + circle.image.getHeight()
							);
					break;
				case CenterBottom:
					//TODO 実装せねば
					break;
				case RightBottom:
					//TODO 実装せねば
					break;
			}
			
		}
	}
	
	
	
	
	/**
	 * 表示アニメーションハンドラーのキックメソッド
	 * 実行時にハンドラーが起動済で無いことが条件
	 */
	public void startOpenAnimation() {
		if(handlerIsRunning){
			//例外でも投げるか・・・？
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
	 * ウィンドウが消えるアニメーションのハンドラーキックメソッド
	 * 実行時にハンドラーが起動済で無いことが条件
	 */
	public void startCloseAnimation() {
		if(handlerIsRunning){
			//例外でも投げるか・・・？
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
	 * サークルセレクターアニメーションハンドラー
	 * 0度から130度までの正弦の値を係数にしてアニメーションを行う。
	 */
	private final Handler openAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				//全アニメーションが終わるとtrueになる。
				Boolean handlerIsEnd = false;
				
				//メインボタンの回転角を追加
				mainCircle.selfRadious++;
				
				//90度になるまで回転させ続ける。
				if(mainCircle.selfRadious < 90){
					//マトリックスに値を設定
					mainCircle.matrix.postRotate(
						//回転角を1度追加
							1,
						//回転の中心を設定。
						//今描画している領域から、画像の真ん中の位置まで中心をずらす。
							mainCircle.viewRect.left + mainCircle.image.getWidth()/2,
							mainCircle.viewRect.top + mainCircle.image.getHeight()/2
						);
				}
				
				
				//サークルセレクタの子要素に
				for(int i = 0;i < circleSelecterList.size();i++){
					
					CircleSelecterChild circle = circleSelecterList.get(i);
					
					//ひとつ前のボタンとの30度分の差がついてからアニメーションをスタートする。
					if(i > 0){
						CircleSelecterChild beforeCircle = circleSelecterList.get(i-1);
						if(circle.rot == 0 
								&& beforeCircle.rot - circle.rot < 30){
							break;
						}
					}
					
					//ボタンの動きが正弦波の130度を超えた時点で動作を止める。
					//(アニメーション的には目的地を一旦通りすぎる様に見える。)
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
				
				//45度まで処理。
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
	 * サークルセレクターアニメーションハンドラー
	 * 130度から0度までの正弦の値を係数にしてアニメーションを行う。
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
					
					
					//ひとつ前のボタンとの30度の差がついてからアニメーションをスタートする。
					if(i > 0){
						CircleSelecterChild beforeCircle = circleSelecterList.get(i-1);
						if(circle.rot == 130 
								&& circle.rot - beforeCircle.rot < 30){
							break;
						}
					}
					
					//ボタンの動きが正弦波の0度を超えた時点で動作を止める。
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
				
				//45度まで処理。
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
