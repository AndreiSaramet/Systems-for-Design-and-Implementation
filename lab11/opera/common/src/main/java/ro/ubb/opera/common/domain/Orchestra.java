package ro.ubb.opera.common.domain;

import java.util.Objects;

public class Orchestra extends BaseEntity<Integer> {
    private String name;

    private String conductor;

    public Orchestra(Integer integer, String name, String conductor) {
        super(integer);
        this.name = name;
        this.conductor = conductor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Orchestra orchestra = (Orchestra) o;
        return Objects.equals(name, orchestra.name) && Objects.equals(conductor, orchestra.conductor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, conductor);
    }

    @Override
    public String toString() {
        return "Orchestra: " +
                super.toString() +
                String.format("name: %-25s ", this.name) +
                String.format("conductor: %-25s ", this.conductor);

    }
}
