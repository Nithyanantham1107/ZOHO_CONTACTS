package dbmodel;

/**
 * Represents a user's data including personal information and contact details.
 */
public class UserData {
    private int user_id;
    private String name;
    private String phoneno;
    private String address;
    private String username;
    private String password;
    private String[] email = new String[5];
    private String currentemail;
    private String primarymail;
    private String timezone;

    /**
     * Sets the user's name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's name.
     *
     * @return the user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the user ID.
     *
     * @param user_id the user ID to set
     */
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return this.user_id;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phoneno the phone number to set
     */
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    /**
     * Gets the user's phone number.
     *
     * @return the phone number
     */
    public String getPhoneno() {
        return this.phoneno;
    }

    /**
     * Sets the user's address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the user's address.
     *
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * Sets the user's email addresses.
     *
     * @param email an array of email addresses to set
     */
    public void setEmail(String[] email) {
        for (int i = 0; i < email.length; i++) {
            this.email[i] = email[i];
        }
    }

    /**
     * Gets the user's email addresses.
     *
     * @return an array of email addresses
     */
    public String[] getEmail() {
        return this.email;
    }

    /**
     * Sets the current email address.
     *
     * @param email the current email address to set
     */
    public void setCurrentEmail(String email) {
        this.currentemail = email;
    }

    /**
     * Gets the current email address.
     *
     * @return the current email address
     */
    public String getCurrentEmail() {
        return this.currentemail;
    }

    /**
     * Sets the user's password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the primary email address.
     *
     * @param mail the primary email address to set
     */
    public void setPrimaryMail(String mail) {
        this.primarymail = mail;
    }

    /**
     * Gets the primary email address.
     *
     * @return the primary email address
     */
    public String getPrimaryMail() {
        return this.primarymail;
    }

    /**
     * Sets the user's timezone.
     *
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Gets the user's timezone.
     *
     * @return the timezone
     */
    public String getTimezone() {
        return this.timezone;
    }
}
