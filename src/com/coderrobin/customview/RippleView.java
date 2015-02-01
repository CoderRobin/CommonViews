/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 coderrobin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.coderrobin.customview;


import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


 class RippleView extends View {
    	private static final int DEFAULT_RIPPLE_COLOR = Color.rgb(0x33, 0x99, 0xcc);
        private int mRippleColor = DEFAULT_RIPPLE_COLOR;
    	private int mMinSize=50;
    	private boolean animationRunning = false;
    	private int currentProgress=0;
    	private int mRippleNum=3;
    	private int mTotalTime=1000000; //无限长的数值，使动画不停止
    	private int mPeriod=15;
    	private int mCenterX;
    	private int mCenterY;
    	private int mRadius;
    	private Paint mPaint;
    	private ObjectAnimator mAnimator;
    	
    	
    	
        public RippleView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			 initPaint();
	         initAnimimation();
		}

		public RippleView(Context context, AttributeSet attrs) {
			super(context, attrs);
			 initPaint();
	         initAnimimation();
		}

		public RippleView(Context context) {
            super(context);
            initPaint();
            initAnimimation();
        }
        
        private void initPaint() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mRippleColor);
        }
        
        
        public void startRippleAnimation() {
        	if(!animationRunning){
            mAnimator.start();
            animationRunning = true;
        	}
    }

        public void stopRippleAnimation() {
        	if(animationRunning){
        	mAnimator.end();
            animationRunning = false;
        	}
    }

        public boolean isRippleAnimationRunning() {
        	return animationRunning;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        	int resultWidth = 0;
        	int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        	int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        	if (modeWidth == MeasureSpec.EXACTLY) {
        		resultWidth = sizeWidth;
        	}
        	else {
        		resultWidth =mMinSize;
        		if (modeWidth == MeasureSpec.AT_MOST) {
        			resultWidth = Math.min(resultWidth, sizeWidth);
        		}
        	}
        	int resultHeight = 0;
        	int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        	int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        	if (modeHeight == MeasureSpec.EXACTLY) {
        	resultHeight = sizeHeight;
        	} else {
        	resultHeight =mMinSize;
        	if (modeHeight == MeasureSpec.AT_MOST) {
        	resultHeight = Math.min(resultHeight, sizeHeight);
        	}
        	}
        // 设置测量尺寸,一定要设置,否则无用且报错
        	mCenterX=resultWidth/2;
        	mCenterY=resultHeight/2;
        	mRadius= (Math.min(resultWidth,resultHeight)) / 2;
        	setMeasuredDimension(resultWidth, resultHeight);
        }




		public int getCurrentProgress() {
			return currentProgress;
		}



		public void setCurrentProgress(int currentProgress) {
			this.currentProgress = currentProgress;
			this.invalidate();
		}

	    private void initAnimimation() {
	        mAnimator = ObjectAnimator.ofInt(this, "currentProgress",
	                0,100);
	        mAnimator.setRepeatCount(ObjectAnimator.INFINITE);
	       	mAnimator.setRepeatMode(ObjectAnimator.RESTART);
	       	mAnimator.setInterpolator(new LinearInterpolator());
	        mAnimator.setEvaluator(mProgressEvaluator);
	        mAnimator.setDuration(mTotalTime);
	       
	    }
	    
	    private  TypeEvaluator mProgressEvaluator=new TypeEvaluator() {

			@Override
			public Object evaluate(float fraction, Object startValue, Object endValue) {
				fraction=(fraction*mTotalTime/mPeriod)%100;
			    return fraction; 
			}
	    	
	    	};


		@Override
        protected void onDraw(Canvas canvas) {
            for(int i=0;i<mRippleNum;i++){
            int progress=(currentProgress+i*100/(mRippleNum))%100; 
            mPaint.setAlpha(255-255*progress/100);
            canvas.drawCircle(mCenterX, mCenterY,mRadius*progress/100, mPaint);
        	}
        }
}
