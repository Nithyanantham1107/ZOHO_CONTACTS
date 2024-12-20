package dbpojo;

public class Session {

	String Session_id;
	long session_expire;
	int user_id;

	
	Session(String SessionId,long sessionExpire,int userId){
		this.session_expire=sessionExpire;
		this.Session_id=SessionId;
		this.user_id=userId;
	}
	
	
	public Session() {
	
	}
	
	public void setUserID(int UserId) {
		this.user_id = UserId;
	}

	public int getUserId() {
		return this.user_id;
	}
	
	
	public void setSessionID(String SessionId) {
		this.Session_id = SessionId;
	}

	public String getSessionId() {
		return this.Session_id;
	}
	
	
	public void setSessionExpire(long SessionExpire) {
		this.session_expire = SessionExpire;
	}

	public long getSessionExpire() {
		return this.session_expire;
	}
	
}
