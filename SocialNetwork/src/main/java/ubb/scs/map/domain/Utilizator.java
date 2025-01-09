package ubb.scs.map.domain;

import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private final String firstName;
    private final String lastName;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Utilizator{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", id=" + getId() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) && getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}