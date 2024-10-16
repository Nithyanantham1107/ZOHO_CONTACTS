package dbmodel;

/**
 * Represents a user group containing details about the group and its members.
 */
public class UserGroup {

    private String groupname;
    private int[] contactid;
    private int userid;
    private int groupid;

    /**
     * Gets the user ID associated with the group.
     *
     * @return the user ID
     */
    public int getUserid() {
        return this.userid;
    }

    /**
     * Sets the user ID associated with the group.
     *
     * @param userid the user ID to set
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * Gets the group ID.
     *
     * @return the group ID
     */
    public int getGroupid() {
        return this.groupid;
    }

    /**
     * Sets the group ID.
     *
     * @param groupid the group ID to set
     */
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    /**
     * Gets the name of the group.
     *
     * @return the group name
     */
    public String getGroupName() {
        return this.groupname;
    }

    /**
     * Sets the name of the group.
     *
     * @param groupname the group name to set
     */
    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }

    /**
     * Gets the contact IDs associated with the group.
     *
     * @return an array of contact IDs
     */
    public int[] getContacId() {
        return this.contactid;
    }

    /**
     * Sets the contact IDs associated with the group.
     *
     * @param contactid an array of contact IDs to set
     */
    public void setcontactid(int[] contactid) {
        int j = 0;
        this.contactid = new int[contactid.length];
        for (int i : contactid) {
            this.contactid[j] = i;
            j++;
        }
    }

    /**
     * Checks if a specific contact ID exists in the group's contact IDs.
     *
     * @param contactid      an array of contact IDs to check
     * @param usercontactid  the contact ID to find
     * @return true if the contact ID exists, false otherwise
     */
    public boolean checkcontact(int[] contactid, int usercontactid) {
        for (int i : contactid) {
            if (i == usercontactid) {
                return true;
            }
        }
        return false;
    }
}
