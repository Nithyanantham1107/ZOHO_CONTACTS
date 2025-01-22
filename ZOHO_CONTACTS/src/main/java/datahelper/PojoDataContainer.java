package datahelper;

import java.util.Queue;

public class PojoDataContainer {

	private Queue<String> pojoKey;
	private Queue<Object> pojoValue;
	private String json;
	private int id;

	public void setID(int id) {

		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public Queue<String> getPojoKey() {
		return pojoKey;
	}

	public void setPojoKey(Queue<String> pojoKey) {
		this.pojoKey = pojoKey;
	}

	public Queue<Object> getPojoValue() {
		return pojoValue;
	}

	public void setPojoValue(Queue<Object> pojoValue) {
		this.pojoValue = pojoValue;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}


}
