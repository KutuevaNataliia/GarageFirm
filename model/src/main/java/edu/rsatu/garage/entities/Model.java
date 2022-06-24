package edu.rsatu.garage.entities;

import java.util.Objects;

public class Model implements  Comparable<Model> {
    private Long id;
    private String name;

    public Model(String name) {
        this.name = name;
    }

    public Model(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(id, model.id) && name.equals(model.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public int compareTo(Model o) {
        if(id == o.getId()){
            return 0;
        }

        if(id <  o.getId()){
            return -1;
        }

        return 1;
    }
}
