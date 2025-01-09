package ubb.scs.map.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Tuplu<Long, Long>> {
    private LocalDateTime requestFrom;

    public Prietenie() {
        this.requestFrom = LocalDateTime.now();
    }

    public Tuplu<Long, Long> getTuplu() {
        return getId();
    }

    public LocalDateTime getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(LocalDateTime requestFrom) {
        this.requestFrom = requestFrom;
    }

    @Override
    public String toString() {
        return "{" + "id " + getTuplu().getE1() + " cerere catre id " + getTuplu().getE2() + "}";
    }

    @Override
    public boolean equals(Object o) { // =tuplu
        if (this == o) return true;
        if (!(o instanceof Prietenie prietenie)) return false;
        return getTuplu().equals(prietenie.getTuplu());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestFrom);
    }
}
