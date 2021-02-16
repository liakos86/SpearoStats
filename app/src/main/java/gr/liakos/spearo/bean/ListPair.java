package gr.liakos.spearo.bean;

import gr.liakos.spearo.model.object.FishingSession;

import java.util.ArrayList;
import java.util.List;

public class ListPair {
	
	List<String> parents;
	
	List<List<FishingSession>> children;
	
	public ListPair(){
		parents = new ArrayList<String>();
		children = new ArrayList<List<FishingSession>>();
	}

	public List<String> getParents() {
		return parents;
	}

	public List<List<FishingSession>> getChildren() {
		return children;
	}
	
}
