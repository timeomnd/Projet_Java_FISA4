package models;

public class UserAccount {
    private int id;
    private String username;
    private String passwordHash;
    private Registration registrationDetails;

    public UserAccount(int id, String username, String passwordHash, Registration registrationDetails) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.registrationDetails = registrationDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Registration getRegistrationDetails() {
        return registrationDetails;
    }

    public void setRegistrationDetails(Registration registrationDetails) {
        this.registrationDetails = registrationDetails;
    }
}