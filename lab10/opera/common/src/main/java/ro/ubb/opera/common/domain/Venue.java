package ro.ubb.opera.common.domain;

import java.util.Objects;

public class Venue extends BaseEntity<Integer> {
    private Integer numberOfSeats;

    private Integer floor;

    public Venue(Integer integer, Integer numberOfSeats, Integer floor) {
        super(integer);
        this.numberOfSeats = numberOfSeats;
        this.floor = floor;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Venue venue = (Venue) o;
        return Objects.equals(numberOfSeats, venue.numberOfSeats) && Objects.equals(floor, venue.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numberOfSeats, floor);
    }

    @Override
    public String toString() {
        return "Venue: " +
                super.toString() +
                String.format("number of seats: %-3d ", this.numberOfSeats) +
                String.format("floor: %-2d ", this.floor);
    }
}
