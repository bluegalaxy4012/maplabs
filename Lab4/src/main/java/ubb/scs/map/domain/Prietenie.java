package ubb.scs.map.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Tuplu<Long, Long>> {
    private final LocalDateTime data;

    public Prietenie() {
        this.data = LocalDateTime.now();
    }

    public Tuplu<Long, Long> getTuplu() {
        return getId();
    }

    @Override
    public String toString() {
        return "{" + "id1=" + getTuplu().getE1() + " catre id2=" + getTuplu().getE2() + "}";
    }

    @Override
    public boolean equals(Object o) { // =tuplu
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        Prietenie prietenie = (Prietenie) o;
        return getTuplu().equals(prietenie.getTuplu());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}
