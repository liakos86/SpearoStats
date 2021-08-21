package gr.liakos.spearo.fragment;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.custom.NestedScrollingListView;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.adapter.FishStatAdapterWithCarousel;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.FishingHelper;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

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
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;


public class FrgFishingStats 
extends Fragment
implements AsyncListener {
	
	/**
	 * Flipper positions
	 */
	static final Integer POSITION_NO_STATS = 0;
	static final Integer POSITION_SHOW_STATS = 1;
	
	ViewFlipper statsFlipper;

	View showcaseView;
	
	NestedScrollingListView fishStatsListView;
    FishStatAdapterWithCarousel fishStatAdapter;

    List<FishAverageStatistic> fishAverageStats = new ArrayList<>();

	boolean isPremiumDiagramsUser = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.frg_fish_stats, container, false);
        
        fishStatsListView = v.findViewById(R.id.listview_fishing_stats);

        fixStatsListView(false);

        initializeFlipper(v);

        showcaseView = v.findViewById(R.id.viewShowcase);

		((ActSpearoStatsMain)getActivity()).queryInventoryForDiagrams(this);

        return v;
    }

    public void statsShowCase(){

    	if (fishAverageStats.isEmpty()){
    		return;
		}

		new MaterialShowcaseView.Builder(getActivity())
				.setTarget(showcaseView)
				.setDismissText(getResources().getString(android.R.string.ok))
				.setContentText(getResources().getString(R.string.showcase_stats))
				.setDelay(2000) // optional but starting animations immediately in onCreate can make them choppy
				.singleUse(Constants.SHOWCASE_STATS) // provide a unique ID used to ensure it is only shown once
				.show();
	}

    void fixStatsListView(boolean loadFromDb){
		fixAverageStats(loadFromDb);
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		boolean isPremiumDiagrams = app_preferences.getBoolean(Constants.SKU_PREMIUM_DIAGRAMS, false);

		fishStatAdapter = new FishStatAdapterWithCarousel(getActivity(), R.layout.fish_stat_row, fishAverageStats, isMetric, isPremiumDiagrams);
		fishStatsListView.setAdapter(fishStatAdapter);
	}
    
	void initializeFlipper(View v) {
		statsFlipper = (ViewFlipper) v.findViewById(R.id.fishing_stats_flipper);
        setFlipperChild();
	}

	public void fixAverageStatsIfNeeded() {
    	if (((SpearoApplication)getActivity().getApplication()).isSessionsHaveChanged()){
    		fixStatsListView(true);
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

		List<FishAverageStatistic> averageStats = FishingHelper.getAverageStatsFrom(getActivity(), new ArrayList<>(fishToStatsMap.values()), StatMode.PERSONAL);
		Collections.sort(averageStats);
		Collections.reverse(averageStats);

		Map<Fish, Map<Integer, Integer>> fishCatchesPerMonth = FishingHelper.convertUserSessionsToMonthlyCatches(sessionsList);
		for (FishAverageStatistic avgStat : averageStats){
			Map<Integer, Integer> fishPerMonth = fishCatchesPerMonth.get(avgStat.getFish());
			avgStat.setCatchesPerMonth(fishPerMonth);
		}

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


	@Override
	public void onAsyncCommunityStatsFinished(List<FishAverageStatistic> stats) {
		//not going to happen
	}

	@Override
	public void onPurchaseStatsAttemptFinished(boolean success) {
		//not going to happen
	}

	@Override
	public void onPurchaseDiagramsAttemptFinished(boolean success) {

    	if (getActivity() == null){
    		return;
		}

		isPremiumDiagramsUser = success;
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putBoolean(Constants.SKU_PREMIUM_DIAGRAMS, success);
		editor.apply();

		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		fishStatAdapter = new FishStatAdapterWithCarousel(getActivity(), R.layout.fish_stat_row, fishAverageStats, isMetric, success);
		fishStatsListView.setAdapter(fishStatAdapter);
		fishStatAdapter.notifyDataSetChanged();

	}
}
