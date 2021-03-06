package com.arjinmc.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

/**
 * ScaleProgressBar
 * @description 
 * @author Eminem Lu
 * @email arjinmc@hotmail.com
 * @date 2015/6/17
 */
public class ScaleProgressBar extends View{
	
	/**current progress*/
	private int progress = 0;
	/**max value of progress */
	public final int MAX_PROGRESS = 100;
	
	/**progress path color*/
	private final int COLOR_PROGETSS = 0xffffffff;
	/**the small circle color*/
	private final int COLOR_S_CIRCLE = 0xffffffff;
	private final int COLOR_TRANSLUCENT = 0xb0000000;
	/**the small circle radius*/
	private final int RADIUS_PROGRESS = 20;
	/**the big circle radius*/
	private final int RADIUS_BIG_CIRCLE = 100;
	
	private final int DURATION_TIME = 2000;
	private final int DURATION_UNIT = 200;
	/**the unit of alter lenth for circle*/
	private final int ALTER_LENTH = 2;
	/**the path width for progress*/
	private final int PATH_WIDTH = 2;
	/**the path width for text*/
	private final int TEXT_WIDTH = 1;
	/**the size for text*/
	private final int TEXT_SIZE = 20;
	/**the text maring top*/
	private final int MARGIN_TOP = 10;
	
	/**the progress paint*/
	private Paint pPaint;
	/**the progress recf*/
	private RectF pRectF = new RectF();
	/**the small circle paint*/
	private Paint sCirclePaint;
	/**the square paint*/
	private Paint squarePaint;
	/**the text paint*/
	private Paint textPaint;
	
	private ScaleProgressDialog spDialog;
	
	/**a timer for drawing animation*/
	private CountDownTimer timer = new CountDownTimer(DURATION_TIME,DURATION_UNIT) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			progress++;
			invalidate();
		}
		
		@Override
		public void onFinish() {
			if(spDialog!=null){
				spDialog.callDismiss();
			}else {
				ScaleProgressBar.this.setVisibility(View.GONE);
			}
		}
	};

	public ScaleProgressBar(Context context) {
		super(context);
		initPaints();
	}
	
	public ScaleProgressBar(Context context, AttributeSet attrs){
		super(context,attrs);
		initPaints();
	}
	
	public ScaleProgressBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		initPaints();
	}
	
	public void setDialog(ScaleProgressDialog spDialog){
		this.spDialog = spDialog;
	}
		
	
	private void initPaints(){
		//init Paints and RectF
		pPaint = new Paint();
		pPaint.setStyle(Paint.Style.STROKE);
		pPaint.setColor(COLOR_PROGETSS);
		pPaint.setStrokeWidth(PATH_WIDTH);
		pPaint.setAntiAlias(true);  
		
		sCirclePaint = new Paint();
		sCirclePaint.setStyle(Paint.Style.FILL);
		sCirclePaint.setColor(COLOR_S_CIRCLE);
		sCirclePaint.setAntiAlias(true);  
		
		squarePaint = new Paint();
		squarePaint.setStyle(Paint.Style.FILL);
		squarePaint.setColor(COLOR_TRANSLUCENT);
		squarePaint.setAntiAlias(true);  
		
		textPaint = new Paint();
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(COLOR_PROGETSS);
		textPaint.setStrokeWidth(TEXT_WIDTH);
		textPaint.setTextSize(TEXT_SIZE);
		textPaint.setAntiAlias(true);  
		textPaint.setTextAlign(Align.CENTER);  
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int halfWidth = getWidth() / 2;
		int halfHeight = getHeight() /2;
		//this way to draw the path for progress
		if(progress<=MAX_PROGRESS){
			canvas.drawColor(COLOR_TRANSLUCENT);
			pRectF.top = halfHeight - RADIUS_PROGRESS;
			pRectF.bottom = halfHeight + RADIUS_PROGRESS;
			pRectF.left = halfWidth - RADIUS_PROGRESS;
			pRectF.right = halfWidth + RADIUS_PROGRESS;
			canvas.drawArc(pRectF, -90, ((float)progress/(float)MAX_PROGRESS)*360, false, pPaint);
			
			//draw a text to show loading percent
			String textString = "Loading"+(int)((float)progress/(float)MAX_PROGRESS*100)+"%";
			FontMetrics fontMetrics = pPaint.getFontMetrics();  
			float top = fontMetrics.top;
		    float bottom = fontMetrics.bottom;
			float baseY = pRectF.bottom+(bottom-top)+MARGIN_TOP;  
			canvas.drawText(textString, halfWidth, baseY, textPaint);  
			
			//canvas.save();
		//this way to draw the images when animation start
		}else{
			int alter = progress-MAX_PROGRESS;
			Path path = new Path();
			path.addRect(0, 0,getWidth(),getHeight(),Path.Direction.CW);
			path.addCircle(halfWidth, halfHeight,RADIUS_BIG_CIRCLE+alter*ALTER_LENTH,Path.Direction.CCW);
			canvas.drawPath(path, squarePaint);
			canvas.drawCircle(halfWidth, halfHeight, RADIUS_PROGRESS-alter*ALTER_LENTH, sCirclePaint);
			//canvas.save();
		}
		super.onDraw(canvas);
	}
	
	public void setProgress(int progress){
		if(spDialog==null && getVisibility()!=View.VISIBLE){
			setVisibility(View.VISIBLE);
		}
		this.progress = progress;
		invalidate();
		if(progress==MAX_PROGRESS){
			timer.start();
		}
	}
	
	//to callback ScaleProgressDialog to dismiss
	public interface OnAnimationFinishListener {
		public void callDismiss();
	}

}
