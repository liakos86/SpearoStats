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
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

/**
 * Fragment for displaying user's personal statistics and {@link FishingSession}s.
 */
public class FrgFishingStats 
extends Fragment
implements AsyncListener {
	
	/**
	 * Flipper position when no {@link FishingSession}s exist yet.
	 */
	static final Integer POSITION_NO_STATS = 0;

	/**
	 * Flipper position when {@link FishingSession}s exist.
	 */
	static final Integer POSITION_SHOW_STATS = 1;

	/**
	 * Flipper for changing between views.
	 */
	ViewFlipper statsFlipper;

	/**
	 * The tutorial screens require this view.
	 */
	View showcaseView;

	/**
	 * ListView for the statistics.
	 */
	NestedScrollingListView fishStatsListView;

	/**
	 * Adapter for the statistics ListView.
	 */
    FishStatAdapterWithCarousel fishStatAdapter;

	/**
	 * ArrayList with the {@link FishAverageStatistic}s of the user.
	 */
	List<FishAverageStatistic> fishAverageStats = new ArrayList<>();

	/**
	 * Will be true when user pays for premium diagrams.
	 */
	boolean isPremiumDiagramsUser = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.frg_fish_stats, container, false);
        
        fishStatsListView = v.findViewById(R.id.listview_fishing_stats);

        loadUserStatsAndSetupDiagramsAdapter(false);

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

	/**
	 * Calculates {@link FishAverageStatistic}s for the user.
	 * Then creates the adapter for the above list.
	 * Finally assigns the adapter to the listview of the above list.
	 *
	 * @param loadFromDb flag
	 */
    void loadUserStatsAndSetupDiagramsAdapter(boolean loadFromDb){
		calculateAverageStats(loadFromDb);
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		boolean isPremiumDiagrams = app_preferences.getBoolean(Constants.SKU_PREMIUM_DIAGRAMS, false);

		fishStatAdapter = new FishStatAdapterWithCarousel(getActivity(), R.layout.fish_stat_row, fishAverageStats, isMetric, isPremiumDiagrams);
		fishStatsListView.setAdapter(fishStatAdapter);
	}
    
	void initializeFlipper(View v) {
		statsFlipper = v.findViewById(R.id.fishing_stats_flipper);
        setFlipperChild();
	}

	/**
	 * If user has inserted or deleted a {@link FishingSession} we need
	 * to calculate the statistics again.
	 */
	public void recalculateAverageStatsIfNeeded() {
    	if (((SpearoApplication)getActivity().getApplication()).isSessionsHaveChanged()){
    		loadUserStatsAndSetupDiagramsAdapter(true);
    		calculateAverageStats(true);

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

	/**
	 * Retrieves or loads from db the user's {@link FishingSession}s.
	 * Converts them to {@link FishStatistic}s.
	 * Converts again to {@link FishAverageStatistic}s.
	 * Also computes the total number of catches for the user.
	 *
	 * @param shouldLoadFromDb flag
	 */
	void calculateAverageStats(boolean shouldLoadFromDb) {
		fishAverageStats.clear();
		List<FishingSession> userSessionsList = getSessionsList(shouldLoadFromDb);
		Map<Fish, FishStatistic> userStatisticsPerFish = FishingHelper.convertUserSessionsToStats(userSessionsList, false);
		List<FishAverageStatistic> userAverageStats = FishingHelper.getAverageStatsFrom(getActivity(), new ArrayList<>(userStatisticsPerFish.values()), StatMode.PERSONAL);
		Collections.sort(userAverageStats);
		Collections.reverse(userAverageStats);

		Map<Fish, Map<Integer, Integer>> fishCatchesPerMonth = FishingHelper.convertUserSessionsToMonthlyCatches(userSessionsList);
		for (FishAverageStatistic avgStat : userAverageStats){
			Map<Integer, Integer> fishPerMonth = fishCatchesPerMonth.get(avgStat.getFish());
			avgStat.setCatchesPerMonth(fishPerMonth);
		}

		int totalCatches = 0;
		for (FishAverageStatistic fishAverageStatistic : userAverageStats) {
			totalCatches += fishAverageStatistic.getTotalCatches();
			fishAverageStats.add(fishAverageStatistic);
		}
		((ActSpearoStatsMain)getActivity()).fixCatchesNum(totalCatches);
		
	}

	/**
	 * The {@link SpearoApplication} calculates the {@link FishingSession}s list upon initialization.
	 * We will use that list unless a fishing session has been inserted or deleted later.
	 *
	 * @param shouldLoadFromDb flag
	 * @return sessions
	 */
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
