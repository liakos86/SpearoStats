package gr.liakos.spearo.async;

import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.def.AsyncSaveUserListener;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.mongo.SyncHelper;
import gr.liakos.spearo.util.SpearoUtils;
import android.os.AsyncTask;

/**
 *
 */
public class AsyncSaveUserMongo extends AsyncTask<Void, Void, User> {
	    SpearoApplication application;
	    AsyncSaveUserListener listener;
	    User user;

	    public AsyncSaveUserMongo(SpearoApplication application, AsyncSaveUserListener list, User user) {
	        this.application = application;
	        this.listener = list;
	        this.user = user;
	    }

	    protected void onPreExecute() {
	    }

	    @Override
	    protected User doInBackground(Void... unused) {
	    	if (SpearoUtils.isOnline(application.getApplicationContext())){
					user = new SyncHelper(application).createAtlasMongoUser(user);
	        }
	        
	        return user;
	    }

	    @Override
	    protected void onPostExecute(User result) {
	    	
	    	if (user == null){
	    		listener.onAsyncError(application.getResources().getString(R.string.network_error));
	    		return;
	    	}

	    	listener.onUserSaved(result);
	    	
	    }
	}

