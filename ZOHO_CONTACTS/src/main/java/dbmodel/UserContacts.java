package dbmodel;

import java.time.Instant;

/**
 * Represents a user's contact information.
 * This class holds details such as name, phone number, address, and more.
 */
public class UserContacts {

    private Integer user_id;
    private Integer contact_id;
    private String f_name;
    private String m_name;
    private String l_name;
    private String phoneno;
    private String address;
    private String gender;
    private String email;
    private Long createdAt;
    private String timezone;

    /**
     * Sets the user ID.
     *
     * @param user_id the user ID to set
     */
    public void setUserid(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserid() {
        return this.user_id;
    }

    /**
     * Sets the contact ID.
     *
     * @param contact_id the contact ID to set
     */
    public void setContactid(int contact_id) {
        this.contact_id = contact_id;
    }

    /**
     * Gets the contact ID.
     *
     * @return the contact ID
     */
    public int getContactid() {
        return this.contact_id;
    }

    /**
     * Sets the first name.
     *
     * @param f_name the first name to set
     */
    public void setFname(String f_name) {
        this.f_name = f_name;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFname() {
        return this.f_name;
    }

    /**
     * Sets the middle name.
     *
     * @param m_name the middle name to set
     */
    public void setMname(String m_name) {
        this.m_name = m_name;
    }

    /**
     * Gets the middle name.
     *
     * @return the middle name
     */
    public String getMname() {
        return this.m_name;
    }

    /**
     * Sets the last name.
     *
     * @param l_name the last name to set
     */
    public void setLname(String l_name) {
        this.l_name = l_name;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLname() {
        return this.l_name;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneno the phone number to set
     */
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public String getPhoneno() {
        return this.phoneno;
    }

    /**
     * Sets the address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the gender.
     * Accepts "male", "M", "female", or "F".
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        if ("male".equals(gender) || "M".equals(gender)) {
            this.gender = "M";
        } else if ("female".equals(gender) || "F".equals(gender)) {
            this.gender = "F";
        }
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdat the creation timestamp to set
     */
    public void setCreatedAt(long createdat) {
        this.createdAt = createdat;
    }

    /**
     * Sets the creation timestamp to the current time.
     */
    public void setCreatedAt() {
        Instant now = Instant.now();
        this.createdAt = now.toEpochMilli();
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    public long getCreatedAt() {
        return this.createdAt;
    }
}
