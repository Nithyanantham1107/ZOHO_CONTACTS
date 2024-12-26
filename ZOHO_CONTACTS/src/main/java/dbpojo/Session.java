package dbpojo;

public class Session {

private	String sessionId;
private	long lastAccessed;
private	int userId;

	
	Session(String sessionId,long lastAccessed,int userId){

		this.sessionId=sessionId;
		this.lastAccessed=lastAccessed;
		
		this.userId=userId;
	}
	
	
	public Session() {
	
	}
	
	public void setUserId(int UserId) {
		this.userId = UserId;
	}

	public int getUserId() {
		return this.userId;
	}
	
	
	public void setSessionID(String SessionId) {
		this.sessionId = SessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}
	
	
	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public long getLastAccessed() {
		return this.lastAccessed;
	}
	
}
