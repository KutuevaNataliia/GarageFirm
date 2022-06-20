package edu.rsatu.garage.entities;

public class Box {
    private Integer id;
    private Double rentPrice;

    public Box(Integer id, Double price) {
        this.id = id;
        this.rentPrice = price;
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
}
