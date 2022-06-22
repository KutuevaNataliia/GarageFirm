package edu.rsatu.garage.entities;

import java.util.Objects;

public class Box {
    private Integer id;
    private Double rentPrice;

    public Box(Integer id, Double price) {
        this.id = id;
        this.rentPrice = price;
    }

    public Box(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return id.equals(box.id) && rentPrice.equals(box.rentPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentPrice);
    }
}
