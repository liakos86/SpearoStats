package gr.liakos.spearo.def;

import gr.liakos.spearo.model.object.FishAverageStatistic;

import java.util.List;

public interface AsyncListener {
	
	void onAsyncCommunityStatsFinished(List<FishAverageStatistic> stats);

	void onPurchaseAttemptFinished(boolean success);

}
