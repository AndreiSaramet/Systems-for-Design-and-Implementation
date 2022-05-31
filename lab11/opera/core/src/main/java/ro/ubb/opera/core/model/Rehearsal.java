package ro.ubb.opera.core.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Rehearsal extends BaseEntity<Integer> {
    private Integer orchestraId;

    private Integer venueId;

    public Rehearsal() {
    }

    public Rehearsal(Integer integer, Integer orchestraId, Integer venueId) {
        super(integer);
        this.orchestraId = orchestraId;
        this.venueId = venueId;
    }

    public Integer getOrchestraId() {
        return orchestraId;
    }

    public void setOrchestraId(Integer orchestraId) {
        this.orchestraId = orchestraId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Rehearsal rehearsal = (Rehearsal) o;
        return Objects.equals(orchestraId, rehearsal.orchestraId) && Objects.equals(venueId, rehearsal.venueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orchestraId, venueId);
    }

    @Override
    public String toString() {
        return "Rehearsal: " +
                super.toString() +
                String.format("orchestra id: %-3s ", this.orchestraId) +
                String.format("venue id: %s3s ", this.venueId);
    }
}
