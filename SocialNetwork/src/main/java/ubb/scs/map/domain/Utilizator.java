package ubb.scs.map.domain;

import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String hashedPassword;
    private String profilePictureUrl;

    public Utilizator(String firstName, String lastName, String username, String hashedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.hashedPassword = hashedPassword;
        profilePictureUrl = "@../images/defaultprofilepicture.png";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "Utilizator{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", id=" + getId() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}