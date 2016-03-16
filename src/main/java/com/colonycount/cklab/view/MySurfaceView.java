package com.colonycount.cklab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView {
	private int r = 235;
	private float centerX = 240;
	private float centerY = 309;
	private RectF displayRect = new RectF(centerX-r, centerY-r, centerX+r, centerY+r);
	
	
	public MySurfaceView(Context context, int r, float centerX, float centerY, RectF displayRect) {
		super(context);
//		this.r = r;
//		this.centerX = centerX;
//		this.centerY = centerY;
//		this.displayRect = displayRect;
		
//		centerX = 240;
//		centerY = 309;
//		r = 235;
//		displayRect = new RectF(centerX-r, centerY-r, centerX+r, centerY+r);
	}
	
	public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Log.d("test4", "surface on draw");
		// add highlight
		canvas.save();
        Path path = new Path();
        
        Paint mFocusPaint = new Paint();
        Paint mOutlinePaint = new Paint();
        mFocusPaint.setARGB(125, 50, 50, 50);
        mOutlinePaint.setColor(0xFFEF04D6);
        mOutlinePaint.setStrokeWidth(2F);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);
        
//        int radius = (Math.min(Math.min(screenWidth, screenHeight), (int)Math.min(getDisplayRect().width(), getDisplayRect().height())) - 20) / 2;
//        float centerX = (getDisplayRect().left + getDisplayRect().right) / 2;
//        float centerY = (getDisplayRect().top + getDisplayRect().bottom) / 2;
//        path.addCircle(centerX, centerY, radius, Path.Direction.CW);
        path.addCircle(centerX, centerY, r, Path.Direction.CW);
        mOutlinePaint.setColor(0xFFEF04D6);
        
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawRect(displayRect, mFocusPaint);
//        canvas.drawRect(getDisplayRect(), mFocusPaint);

        canvas.restore();
        canvas.drawPath(path, mOutlinePaint);
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//        setMeasuredDimension(width, height);
//        
////        if (mSupportedPreviewSizes != null) {
////           mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
////        }
//        
//        // Log.d("test4", "surface width = " + width + ", height = " + height);
//	}
}
