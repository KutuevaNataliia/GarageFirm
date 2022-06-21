package edu.rsatu.garage.entities;

public class Client {

    private Long id;
    private String surname;
    private String address;

    public Client(String surname, String address) {
        this.surname = surname;
        this.address = address;
    }

    public Client(Long id, String surname, String address) {
        this.id = id;
        this.surname = surname;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
