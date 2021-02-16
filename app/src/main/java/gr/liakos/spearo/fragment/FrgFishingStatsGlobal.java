package gr.liakos.spearo.fragment;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.custom.NestedScrollingListView;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.model.adapter.FishStatGlobalAdapter;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FrgFishingStatsGlobal
extends Fragment
implements AsyncListener{
	
	Button buttonGoPremium;
	ProgressBar premiumProgressBarLarge;
	TextView textGoPremium;
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
			edit.commit();
		}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.frg_fish_stats_global, container, false);
        
        premiumProgressBarLarge = v.findViewById(R.id.premiumLayoutProgressBar);
        textGoPremium = v.findViewById(R.id.premiumText);
        textGoPremium.setText(getActivity().getResources().getString(R.string.premium_info, ((SpearoApplication) getActivity().getApplication()).getFishies().size()));
        buttonGoPremium = v.findViewById(R.id.button_go_premium);
        fishStatsListView = v.findViewById(R.id.listview_fishing_stats_global);
        
        SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
        
        fishStatAdapter = new FishStatGlobalAdapter(getActivity(), R.layout.fish_stat_global_row, fishAverageStats, isMetric);
        fishStatAdapter.setCommunityStats(true);
        fishStatsListView.setAdapter(fishStatAdapter);

        if (fishAverageStats.isEmpty()){
        	showLargeProgressBar();
        }else{
        	showListView();
        }

		((ActSpearoStatsMain)getActivity()).queryInventoryForPremium(this);


		return v;
    }
    
	void loadAverageStats(boolean isPremium) {
		isPremiumUser = isPremium;
		if (!isPremium){
			setupPremiumButtonListener();
			showTextAndButton();
			return;
		}
		asyncLoadStats();
	}
	
	void showListView() {
		buttonGoPremium.setVisibility(View.GONE);
		premiumProgressBarLarge.setVisibility(View.GONE);
		textGoPremium.setVisibility(View.GONE);
		fishStatsListView.setVisibility(View.VISIBLE);
	}
	
	void showTextAndButton() {
		buttonGoPremium.setVisibility(View.VISIBLE);
		textGoPremium.setVisibility(View.VISIBLE);
		premiumProgressBarLarge.setVisibility(View.GONE);
		fishStatsListView.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi")
	void setupPremiumButtonListener() {
		final AsyncListener listener = this;

		buttonGoPremium.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (!SpearoUtils.isOnline(getContext())){
					snack(R.string.network_error);
					return;
				}

				showLargeProgressBar();
				((ActSpearoStatsMain) getActivity()).purchase(v);

				
			}
		});
		
	}

	void showLargeProgressBar() {
		buttonGoPremium.setVisibility(View.GONE);
		textGoPremium.setVisibility(View.GONE);
		fishStatsListView.setVisibility(View.GONE);
		premiumProgressBarLarge.setVisibility(View.VISIBLE);
		
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
		showListView();
		if (stats.isEmpty()){
			return;
		}
		fishAverageStats.clear();
		for (FishAverageStatistic fishAverageStatistic : stats) {
			fishAverageStats.add(fishAverageStatistic);
		}
		fishStatAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPurchaseAttemptFinished(boolean success){
		loadAverageStats(success);
	}

}
