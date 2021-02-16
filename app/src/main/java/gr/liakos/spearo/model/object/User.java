package gr.liakos.spearo.model.object;

import org.bson.types.ObjectId;

import java.io.Serializable;


public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObjectId _id;
	
	String username;
	
	public User(String id, String username){
		this._id = new ObjectId(id);
		this.username = username;
	}
	
	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

}
