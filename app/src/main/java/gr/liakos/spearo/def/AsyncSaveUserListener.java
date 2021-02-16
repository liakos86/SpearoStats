package gr.liakos.spearo.def;

import gr.liakos.spearo.model.object.User;

public interface AsyncSaveUserListener {
	
	public void onUserSaved(User user);
	
	//public void onUserAuthenticated(User user);

	public void onAsyncError(String errorMsg);

	//public void setLoadingScreen(boolean doLoad);

}
