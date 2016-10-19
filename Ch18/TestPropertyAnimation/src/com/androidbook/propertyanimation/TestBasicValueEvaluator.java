package com.androidbook.propertyanimation;

import android.animation.ValueAnimator;
import android.util.Log;

public class TestBasicValueEvaluator 
{
	private String tag = "TestBasicValueEvaluator";
	public  void test()
	{
		Log.d(tag, "Setting up the evaluator");
		ValueAnimator anim = ValueAnimator.ofInt(10, 200);
		anim.setDuration(200); //100 milliseconds
		ValueAnimator.setFrameDelay(5);
		anim.addUpdateListener(
			    new ValueAnimator.AnimatorUpdateListener()  {        
			        public void onAnimationUpdate(ValueAnimator animation) { 
			        	dosomethingWiththeValue(animation);
			        }    
			    }
			);
		anim.start();
	}
	private void dosomethingWiththeValue(ValueAnimator aimator)
	{
        Integer value = (Integer) aimator.getAnimatedValue(); 
        Log.d(tag,"Value from the animator:" + value);
	}
}
