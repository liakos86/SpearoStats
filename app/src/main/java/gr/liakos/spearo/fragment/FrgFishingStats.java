package gr.liakos.spearo.fragment;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.custom.NestedScrollingListView;
import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.adapter.FishStatAdapter;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.FishingHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

public class FrgFishingStats 
extends Fragment {
	
	/**
	 * Flipper positions
	 */
	static final Integer POSITION_NO_STATS = 0;
	static final Integer POSITION_SHOW_STATS = 1;
	
	ViewFlipper statsFlipper;
	
	NestedScrollingListView fishStatsListView;
    FishStatAdapter fishStatAdapter;
    List<FishAverageStatistic> fishAverageStats = new ArrayList<FishAverageStatistic>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.frg_fish_stats, container, false);
        
        fishStatsListView = (NestedScrollingListView) v.findViewById(R.id.listview_fishing_stats);
        fixAverageStats(false);
        
        SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
        
        fishStatAdapter = new FishStatAdapter(getActivity(), R.layout.fish_stat_row, fishAverageStats, isMetric);
        fishStatsListView.setAdapter(fishStatAdapter);
        
        initializeFlipper(v);

        return v;
    }
    
	void initializeFlipper(View v) {
		statsFlipper = (ViewFlipper) v.findViewById(R.id.fishing_stats_flipper);
        setFlipperChild();
	}

	public void fixAverageStatsIfNeeded() {
    	if (((SpearoApplication)getActivity().getApplication()).isSessionsHaveChanged()){
    		fixAverageStats(true);
    		fishStatAdapter.notifyDataSetChanged();
    		setFlipperChild();
    		
    	}
    }
	
	void setFlipperChild() {
		if (fishAverageStats.isEmpty()){
        	statsFlipper.setDisplayedChild(POSITION_NO_STATS);
        }else{
        	statsFlipper.setDisplayedChild(POSITION_SHOW_STATS);
        }
	}

	void fixAverageStats(boolean shouldLoadFromDb) {
		fishAverageStats.clear();
		List<FishingSession> sessionsList = getSessionsList(shouldLoadFromDb);
        
		Map<Fish, FishStatistic> fishToStatsMap = FishingHelper.convertUserSessionsToStats(sessionsList, false);

		List<FishAverageStatistic> averageStats = FishingHelper.getAverageStatsFrom(getActivity(), new ArrayList<FishStatistic>(fishToStatsMap.values()), StatMode.PERSONAL);
		Collections.sort(averageStats);
		Collections.reverse(averageStats);
		
		int totalCatches = 0;
		for (FishAverageStatistic fishAverageStatistic : averageStats) {
			totalCatches += fishAverageStatistic.getTotalCatches();
			fishAverageStats.add(fishAverageStatistic);
		}
		((ActSpearoStatsMain)getActivity()).fixCatchesNum(totalCatches);
		
	}

	List<FishingSession> getSessionsList(boolean shouldLoadFromDb) {
		if (shouldLoadFromDb) {
			return new Database(getActivity().getApplicationContext()).fetchFishingSessionsFromDb();
		}
		return ((SpearoApplication) getActivity().getApplication()).getFishingSessions();
	}
	
	public static FrgFishingStats init(int val) {
		FrgFishingStats truitonList = new FrgFishingStats();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonList.setArguments(args);
        return truitonList;
    }


}
