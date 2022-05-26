package ro.ubb.opera.core.model;

import java.util.Objects;

public class Composer extends BaseEntity<Integer> {
    private String name;

    private String nationality;

    private String musicalPeriod;

    public Composer(Integer id, String name, String nationality, String musicalPeriod) {
        super(id);
        this.name = name;
        this.nationality = nationality;
        this.musicalPeriod = musicalPeriod;
    }

    public Composer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMusicalPeriod() {
        return musicalPeriod;
    }

    public void setMusicalPeriod(String musicalPeriod) {
        this.musicalPeriod = musicalPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Composer composer = (Composer) o;
        return Objects.equals(name, composer.name) && Objects.equals(nationality, composer.nationality) && Objects.equals(musicalPeriod, composer.musicalPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, nationality, musicalPeriod);
    }

    @Override
    public String toString() {
        return "Composer: " +
                super.toString() +
                String.format("name: %-25s \n", this.name) +
                String.format("nationality: %-13s \n", this.nationality) +
                String.format("musical period: %-20s \n", this.musicalPeriod);
    }
}
