package ro.ubb.opera.common.domain;

import java.util.Objects;

public class BaseEntity<ID> {
    private ID id;

    public BaseEntity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseEntity<?> baseEntity = (BaseEntity<?>) o;
        return Objects.equals(id, baseEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("id: %-3s \n", this.id);
    }
}
