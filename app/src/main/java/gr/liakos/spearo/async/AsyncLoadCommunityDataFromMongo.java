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

/**
 * @see gr.liakos.spearo.fragment.FrgFishingStatsGlobal
 *
 * This class loads the mongo db data of all the application users.
 * In order to do this, a user must be {@link gr.liakos.spearo.util.Constants#SKU_PREMIUM_STATS}.
 *
 */
public class AsyncLoadCommunityDataFromMongo extends AsyncTask<Void, Void, List<FishAverageStatistic>> {
    SpearoApplication application;
    AsyncListener asyncListener;
    boolean newRecord = false;

    public AsyncLoadCommunityDataFromMongo(SpearoApplication application, AsyncListener asyncListener) {
        this.application = application;
        this.asyncListener = asyncListener;
    }

    protected void onPreExecute() {
    	//empty
	}

	/**
	 * If app is online we retrieve the mongo data.
	 * Then if the data is not empty we replace the database data with the new ones.
	 * If the data is empty use the database data.
	 * If app is not online use the database data.
	 * Then collect the catches per hour 0-24 for every {@link gr.liakos.spearo.enums.Season}.
	 *
	 * @param unused -
	 * @return avg stats
	 */
	@Override
    protected List<FishAverageStatistic> doInBackground(Void... unused) {
    	Database database = new Database(application);
    	
    	List<FishStatistic> communityStats = new ArrayList<>();
    	List<FishAverageStatistic> communityAvgStats;
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

		FishingHelper.assignCatchesPerHourPerSeasonGlobal(communityAvgStats, communityStats);
		newRecord = database.updateRecordsFromCommunityData(communityStats);
		application.setNewCommunityDataRecord(newRecord);
		if (newRecord){
			application.reloadFishWhenNewRecord();
		}
		
		Collections.sort(communityAvgStats);

        return communityAvgStats;
    }

    @Override
    protected void onPostExecute(List<FishAverageStatistic> stats) {
		application.refreshCommunityData(stats);
        asyncListener.onAsyncCommunityStatsFinished(stats);
    }

}
