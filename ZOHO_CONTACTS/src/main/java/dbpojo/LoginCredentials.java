package dbpojo;

public class LoginCredentials {

	int id;
	String username;

	
	public LoginCredentials(int id,String username) {
		this.id=id;
		this.username=username;
		
	}
	
	
	public void setUserID(int UserId) {
		this.id = UserId;
	}

	public int getUserId() {
		return this.id;
	}

	public void setUserName(String UserName) {
		this.username = UserName;
	}

	public String getUserName() {
		return this.username;
	}

}
