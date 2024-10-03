package dbmodel;

public class UserGroup {
	
	private String groupname;
	private int[] contactid ;
	private int userid;
	private int groupid;
	public int getUserid() {
		return this.userid;
	}
	public void setUserid(int userid) {
		this.userid=userid;
		
	}
	
	public int getGroupid() {
		return this.groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid=groupid;
		
	}
	
	public String getGroupName() {
		return this.groupname;
	}
	public void setGroupName(String groupname) {
		this.groupname=groupname;
		
	}
	
	
	public int[] getContacId() {
		return this.contactid;
	}
	public void setcontactid(int[] contactid) {
		int j=0;
		this.contactid=new int[contactid.length];
		for(int i :contactid) {
		
			this.contactid[j]=i;
			j++;
		}
	
		
	}
	
	public boolean checkcontact(int[] contactid,int usercontactid){
		for(int i:contactid){
			if(i==usercontactid){
				return true;
			}
		}
		return false;
	}

}
