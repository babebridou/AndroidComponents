package com.fairyteller.db.example;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBean {

	private long idUser;
	private String lastName;
	private String firstName;
	public long getIdUser() {
		return idUser;
	}
	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("idUser", idUser);
		jsonObject.put("lastName", lastName);
		jsonObject.put("firstName", firstName);
		return jsonObject;
	}
	
	public void parseJSONObject(JSONObject jsonObject) throws JSONException{
		setIdUser(jsonObject.getLong("idUser"));
		setFirstName(jsonObject.getString("firstName"));
		setLastName(jsonObject.getString("lastName"));
	}
	
	@Override
	public String toString() {
		try {
			return toJSONObject().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
