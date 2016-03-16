package com.colonycount.cklab.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.colonycount.cklab.activity.R;

public class CircleHintView extends View {
	//circle color
	private int circleCol;
	//paint for drawing custom view
	private Paint circlePaint;
	
	public CircleHintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//paint object for drawing in onDraw
		circlePaint = new Paint();
		//get the attributes specified in attrs.xml using the name we included
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleHintView, 0, 0);
		
		try {
		    //get the text and colors specified using the names in attrs.xml
		    circleCol = a.getInteger(R.styleable.CircleHintView_circleColor, 0);//0 is default
		} finally {
		    a.recycle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    //draw the View
		//get half of the width and height as we are working with a circle
		int viewWidthHalf = this.getMeasuredWidth()/2;
		int viewHeightHalf = this.getMeasuredHeight()/2;
		// count radius
		int radius = viewWidthHalf - 10;
		// Log.d("Test2", "radius = " + radius);
		
		circlePaint.setStyle(Style.STROKE);
		circlePaint.setAntiAlias(true);
		//set the paint color using the circle color specified
		circlePaint.setColor(circleCol);
		circlePaint.setStrokeWidth(2);
		
		canvas.save();
        Path path = new Path();
        path.addCircle(viewWidthHalf, viewHeightHalf, radius, Path.Direction.CW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.restore();
        canvas.drawPath(path, circlePaint);
        
//		canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);
	}
	
	public int getCircleColor(){
	    return circleCol;
	} 
	
	public void setCircleColor(int newColor){
	    //update the instance variable
	    circleCol=newColor;
	    //redraw the view
	    invalidate();
	    requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(width < height)
        	setMeasuredDimension(width, width); //Snap to width
        else
        	setMeasuredDimension(height, height); //Snap to height
	}
}
