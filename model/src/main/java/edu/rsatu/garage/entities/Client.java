package edu.rsatu.garage.entities;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && surname.equals(client.surname) && address.equals(client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, address);
    }
}
