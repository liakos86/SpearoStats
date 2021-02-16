package gr.liakos.spearo.async;

import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.mongo.SyncHelper;
import gr.liakos.spearo.util.FishingHelper;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;

public class AsyncLoadCommunityDataFromMongo extends AsyncTask<Void, Void, List<FishAverageStatistic>> {
    SpearoApplication application;
    AsyncListener asyncListener;
    boolean newRecord = false;

    public AsyncLoadCommunityDataFromMongo(SpearoApplication application, AsyncListener asyncListener) {
        this.application = application;
        this.asyncListener = asyncListener;
    }

    protected void onPreExecute() {

    }

    @Override
    protected List<FishAverageStatistic> doInBackground(Void... unused) {
    	Database database = new Database(application);
    	
    	List<FishStatistic> communityStats = new ArrayList<FishStatistic>();
    	List<FishAverageStatistic> communityAvgStats = new ArrayList<FishAverageStatistic>();
		if (SpearoUtils.isOnline(application)){
        	communityStats = new SyncHelper(application).getAtlasCommunityStats();
        	if (!communityStats.isEmpty()){
        		communityAvgStats = FishingHelper.getAverageStatsFrom(application, communityStats, StatMode.GLOBAL);
        		database.addNewStats(communityAvgStats);
        	}else{
        		communityAvgStats = database.fetchCommunityData();
        	}
        }else{
        	communityAvgStats = database.fetchCommunityData();
        	
        }
    	
		newRecord = database.updateRecordsFromCommunityData(communityStats);
		application.setNewCommunityDataRecord(newRecord);
		if (newRecord){
			application.reloadFishWhenNewRecord();
		}
		
		Collections.sort(communityAvgStats);
		Collections.reverse(communityAvgStats);
        return communityAvgStats;
    }

    @Override
    protected void onPostExecute(List<FishAverageStatistic> stats) {
    	application.refreshCommunityData(stats);
        asyncListener.onAsyncCommunityStatsFinished(stats);
    }
}
