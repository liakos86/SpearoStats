package gr.liakos.spearo.async;

import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.mongo.SyncHelper;
import gr.liakos.spearo.util.SpearoUtils;
import android.os.AsyncTask;

/**
 *
 */
public class AsyncSaveFishRequestMongo extends AsyncTask<Void, Void, Void> {
	    SpearoApplication application;
	    String fishName;

	    public AsyncSaveFishRequestMongo(SpearoApplication application, String fishName) {
	        this.application = application;
	        this.fishName = fishName;
	    }

	    protected void onPreExecute() {
	    }

	    @Override
	    protected Void doInBackground(Void... unused) {
	    	if (SpearoUtils.isOnline(application.getApplicationContext())){
					new SyncHelper(application).insertMongoFishRequest(fishName);
	        }
	        
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	    }
	    
	}

