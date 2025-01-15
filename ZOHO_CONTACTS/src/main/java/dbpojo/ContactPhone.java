package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class ContactPhone implements Table {
private int id=-1;
private	int contactID=-1;
private	String ContactPhoneNo ;
private long createdAt = -1;
private long modifiedAt = -1;

	
	public ContactPhone(int id,int contactID,String ContactPhoneNo,long createdAt,long modifiedAt) {
		
		this.contactID=contactID;
		this.ContactPhoneNo=ContactPhoneNo;
		
		this.id=id;
		this.createdAt=createdAt;
		this.modifiedAt=modifiedAt;
		
	}
	
	public ContactPhone() {
		
	}
	

		public String getPrimaryIDName() {
			
			return tables.Contact_phone.getPrimaryKey();
		}
	
		public void setID(int id) {
			
			this.id=id;
		}

		public int getID() {
		
			return this.id;
		}
		public String getTableName() {
			
			return tables.Contact_phone.getTableName();
		}
	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	
	public void setContactID(int ContactID) {
		this.contactID = ContactID;
	}

	public int getContactID() {
		return this.contactID;
	}

	public void setContactPhone(String ContactPhone) {
		this.ContactPhoneNo = ContactPhone;
	}

	public String getContactPhone() {
		return this.ContactPhoneNo;
	}

}
