package dbpojo;

public class CategoryRelation {

	ContactDetails contact_id_to_join;
	int Category_id;

	
	
	public CategoryRelation(ContactDetails contactIdtoJoin,int CategoryID) {
		
		
		
		this.contact_id_to_join=contactIdtoJoin;
		this.Category_id=CategoryID;
	}
	
	public void setCategoryID(int Categoryid) {
		this.Category_id = Categoryid;
	}

	public int getCategoryID() {
		return this.Category_id;
	}

	public void setContactIDtoJoin(ContactDetails ContactIDtoJoin) {
		this.contact_id_to_join = ContactIDtoJoin;
	}

	public ContactDetails getContactIDtoJoin() {
		return this.contact_id_to_join;
	}

}
