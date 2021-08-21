package gr.liakos.spearo.fragment;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.custom.NestedScrollingListView;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.model.adapter.FishStatGlobalAdapter;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FrgFishingStatsGlobal
extends Fragment
implements AsyncListener{

	TextView textViewAndroidVersion;
	NestedScrollingListView fishStatsListView;
    FishStatGlobalAdapter fishStatAdapter;
    List<FishAverageStatistic> fishAverageStats = new ArrayList<FishAverageStatistic>();
    boolean isPremiumUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	List<FishAverageStatistic> dbCommunityData = ((SpearoApplication)getActivity().getApplication()).getDbCommunityData();
    	fishAverageStats.addAll(dbCommunityData);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if (! isPremiumUser){
    		return;
    	}
    	
    	SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		long lastUpdateMillis = app_preferences.getLong(Constants.LAST_MONGO_UPDATE_MILLIS, -1);
		if ((new Date().getTime() - lastUpdateMillis) > (8*60*60*1000)){
			asyncLoadStats();
			Editor edit = app_preferences.edit();
			edit.putLong(Constants.LAST_MONGO_UPDATE_MILLIS, new Date().getTime());
			edit.apply();
		}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.frg_fish_stats_global, container, false);

        fishStatsListView = v.findViewById(R.id.listview_fishing_stats_global);
        
        SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
        
        fishStatAdapter = new FishStatGlobalAdapter(getActivity(), R.layout.fish_stat_global_row_with_diagrams, fishAverageStats, isMetric, isPremiumUser);
        fishStatsListView.setAdapter(fishStatAdapter);

		textViewAndroidVersion = v.findViewById(R.id.textViewVersionAndroid);


		((ActSpearoStatsMain)getActivity()).queryInventoryForStats(this);


		return v;
    }
    
	void loadAverageStats(boolean isPremium) {
		isPremiumUser = isPremium;

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){
			textViewAndroidVersion.setVisibility(View.VISIBLE);
		}else{
			asyncLoadStats();
			textViewAndroidVersion.setVisibility(View.GONE);
		}

	}

	public static FrgFishingStatsGlobal init(int val) {
		FrgFishingStatsGlobal truitonList = new FrgFishingStatsGlobal();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonList.setArguments(args);
        return truitonList;
    }
	
	void snack(int message){
		((ActSpearoStatsMain)getActivity()).snack(message);
	}
	
	/**
	 * Fires an async call in the application class.
	 * onAsyncFinished will be called when it is completed.
	 */
	void asyncLoadStats() {
		FragmentActivity activity = (FragmentActivity) getActivity();
		if (activity == null){
			return;
		}
		SpearoApplication spearoApplication = (SpearoApplication) activity.getApplication();
		if (spearoApplication == null){
			return;
		}

		spearoApplication.loadCommunityData(this);
	}
	
	
	/**
	 * Will be called when the async mongo load of comm data finishes.
	 */
	@Override
	public void onAsyncCommunityStatsFinished(List<FishAverageStatistic> stats) {

		if (getActivity() == null){
			return;
		}

		if (stats.isEmpty()){
			return;
		}
		fishAverageStats.clear();
		for (FishAverageStatistic fishAverageStatistic : stats) {
			fishAverageStats.add(fishAverageStatistic);
		}

		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);

		fishStatAdapter = new FishStatGlobalAdapter(getActivity(), R.layout.fish_stat_global_row_with_diagrams, fishAverageStats, isMetric, isPremiumUser);
		fishStatsListView.setAdapter(fishStatAdapter);

		fishStatAdapter.notifyDataSetChanged();

	}
	
	@Override
	public void onPurchaseStatsAttemptFinished(boolean success){
		loadAverageStats(success);
	}

	@Override
	public void onPurchaseDiagramsAttemptFinished(boolean success) {
		//nothing to do here
	}

}
