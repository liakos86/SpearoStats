package gr.liakos.spearo.application;

import gr.liakos.spearo.R;
import gr.liakos.spearo.util.Constants;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
//import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.res.ResourcesCompat;

/**
 * sub class of {@link android.widget.AutoCompleteTextView} that includes a clear (dismiss / close) button with
 * a OnClearListener to handle the event of clicking the button
 * based on code from {@link http://www.gubed.net/clearableautocompletetextview}
 */
public class ClearableAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    // was the text just cleared?
    boolean justCleared = false;
    Context context;

    // if not set otherwise, the default clear listener clears the text in the
    // text view
    private OnClearListener defaultClearListener = new OnClearListener() {

        @Override
        public void onClear() {
            ClearableAutoCompleteTextView et = ClearableAutoCompleteTextView.this;
            et.setText(Constants.EMPTY);
        }
    };

    private OnClearListener onClearListener = defaultClearListener;

    // The image we defined for the clear button
    public Drawable imgClearButton = ResourcesCompat.getDrawable(getResources(), R.drawable.close_48, null);// getResources().getDrawable(R.drawable.close_48);

    public interface OnClearListener {
        void onClear();
    }

    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        // Set the bounds of the button
        this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                imgClearButton, null);

        // if the clear button is pressed, fire up the handler. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ClearableAutoCompleteTextView et = ClearableAutoCompleteTextView.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                    onClearListener.onClear();
                    justCleared = true;
                }
                return false;
            }
        });
    }

    public void setImgClearButton(Drawable imgClearButton) {
        this.imgClearButton = imgClearButton;
    }

    public void setOnClearListener(final OnClearListener clearListener) {
        this.onClearListener = clearListener;
    }

    public void hideClearButton() {
        this.setCompoundDrawables(null, null, null, null);
    }

    public void showClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
    }

    

    @Override
    public void onFilterComplete(int count) {
    	super.onFilterComplete(count);
    	
    	if (count >0 && getText().toString().length() ==0){
    		InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0);
    	}



    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

	public void setContext(Context applicationContext) {
		this.context = applicationContext;
		
	}
}
