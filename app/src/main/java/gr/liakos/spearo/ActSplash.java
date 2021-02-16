package gr.liakos.spearo;

import gr.liakos.spearo.application.BaseFrgActivityWithBottomButtons;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by liakos on 9/6/2016.
 */
public class ActSplash extends BaseFrgActivityWithBottomButtons {
	
	boolean mainStarted = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        startImagePulse();
        if	( ((SpearoApplication)getApplication()).isLoaded()){
        	startMain();
        }else{
        	startCountDown();
        }
    }

    void startCountDown() {
    	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        final int fiveSecs = 1 * 5 * 1000; // 1 minute in milli seconds

        new CountDownTimer(fiveSecs, 1000) {
            public void onTick(long millisUntilFinished) {
                long finishedSeconds = fiveSecs - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)fiveSecs) * 100.0);
                progressBar.setProgress(total);
                if	( ((SpearoApplication)getApplication()).isLoaded()){
                	startMain();
                	this.cancel();
                	return;
                }
            }

            public void onFinish() {
            	progressBar.setProgress(100);
            	
            	if (mainStarted){
            		return;
            	}
            	
            	while	( !((SpearoApplication)getApplication()).isLoaded()){
            	// wait for it	
            	}
            	startMain();
            }
        }.start();
	}

	void startImagePulse() {
    	ImageView iv = (ImageView) findViewById(R.id.img_splash);
    	ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
    	                    iv,
    	                    PropertyValuesHolder.ofFloat("scaleX", 1.05f),
    	                    PropertyValuesHolder.ofFloat("scaleY", 1.05f));
    	scaleDown.setDuration(310);
    	scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
    	scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
    	scaleDown.setInterpolator(new FastOutSlowInInterpolator());
    	scaleDown.start();
	}

	void startMain() {
		mainStarted = true;
        Intent intent = new Intent(this, ActSpearoStatsMain.class);
        startActivity(intent);
        finish();
    }

}

