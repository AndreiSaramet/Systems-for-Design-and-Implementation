package ro.ubb.opera.core.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Opera extends BaseEntity<Integer> {
    private String title;

    private String language;

    private Integer composerId;

    public Opera() {
    }

    public Opera(Integer integer, String title, String language, Integer composerId) {
        super(integer);
        this.title = title;
        this.language = language;
        this.composerId = composerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getComposerId() {
        return composerId;
    }

    public void setComposerId(Integer composerId) {
        this.composerId = composerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Opera opera = (Opera) o;
        return Objects.equals(title, opera.title) && Objects.equals(language, opera.language) && Objects.equals(composerId, opera.composerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, language, composerId);
    }

    @Override
    public String toString() {
        return "Opera: " +
                super.toString() +
                String.format("title: %-40s ", this.title) +
                String.format("language: %-15s ", this.language) +
                String.format("composer id: %-3s ", this.composerId);
    }
}
