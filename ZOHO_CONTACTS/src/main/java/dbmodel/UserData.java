package dbmodel;

import java.util.ArrayList;

public class UserData {
private int user_id;
private String name;
private String phoneno;
private String address;
private String username;
private String password;
private String[] email=new String[5]; 
private String currentemail;
private String primarymail;
public void setName(String name) {
	this.name=name;
}
public String getName() {
	return this.name;
}

public void setUserId(int user_id) {
	this.user_id=user_id;
}

public int getUserId() {
	return this.user_id;
}

public void setPhoneno(String phoneno) {
	this.phoneno=phoneno;
}

public String getPhoneno() {
	return this.phoneno;
}

public void setAddress(String address) {
	this.address=address;
}


public String getAddress() {
	return this.address;
}

public void setUserName(String username) {
	this.username=username;
}

public String getUserName() {
	return this.username	;
}

public void setEmail(String[] email) {
	for(int i=0;i<email.length;i++) {
		this.email[i]=email[i];
		
	}
	
	
}

public String[] getEmail() {
	return this.email;
}

public void setCurrentEmail(String email) {
	this.currentemail=email;
}

public String getCurrentEmail() {
	
	return this.currentemail;
}
public void setPassword(String password) {
	this.password=password;
}

public String getPassword() {
	return this.password;
}

public void setPrimaryMail(String mail) {
	this.primarymail=mail;
	
}

public String getPrimaryMail() {
	return this.primarymail;
	
}

}
