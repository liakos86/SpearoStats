package gr.liakos.spearo.application;


import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A fragment activity with a bottom button layout
 */
public class BaseFrgActivityWithBottomButtons extends AppCompatActivity {// FragmentActivity {

    /**
     * Key represents the position of the button while value the layoutId of the button
     */
    protected Map<Integer, Integer> bottomButtons;

    @Override
    protected void onResume() {
        super.onResume();
    }

    
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Populates the hashmap of the buttons and assigns a listener to each one of them
     *
     * @param mPager
     */
    protected void setBottomButtons(NonSwipeableViewPager mPager) {
        bottomButtons = new HashMap<Integer, Integer>();
        bottomButtons.put(0, R.id.btn_fishing_sessions);
        bottomButtons.put(1, R.id.btn_fishing_stats);
        bottomButtons.put(2, R.id.btn_guns);
        bottomButtons.put(3, R.id.btn_fishing_stats_global);
       // bottomButtons.put(3, R.id.btn_fish);
        bottomButtons.put(4, R.id.btn_map);
        for (int counter = 0; counter < ActSpearoStatsMain.getPagerSize(); counter++) {
            setBottomButtonListener(mPager, bottomButtons.get(counter), counter);
        }
    }

    /**
     * Sets the global position of the bottom buttons and then starts the
     * appropriate fragment or the new interval activity
     *
     * @param mPager
     * @param position the position that the user selected
     */
    private void startMain(NonSwipeableViewPager mPager, int position) {
        if (null != mPager) {
            mPager.setCurrentItem(position);
        } else {
            startMainWhenNoPager(position);
        }
    }

    /**
     * Restarts the activity if the mPager is null.
     *
     * @param position
     */
    private void startMainWhenNoPager(int position) {
        Intent intent = new Intent(this, ActSpearoStatsMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }

    /**
     * If the positions is 0 or 1 we just change the displayed fragment
     * If the position is 2 we start a new IntervalActivity
     *
     * @param mPager
     * @param btn      the xml id of the button
     * @param position the position of the button in the layout
     */
    private void setBottomButtonListener(final NonSwipeableViewPager mPager, int btn, final int position) {
        LinearLayout bottomButton = (LinearLayout) findViewById(btn);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain(mPager, position);
            }

        });
    }
}
