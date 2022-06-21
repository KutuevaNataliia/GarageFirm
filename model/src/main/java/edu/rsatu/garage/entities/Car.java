package edu.rsatu.garage.entities;

import java.time.LocalDate;

public class Car {
    private String number;
    private Long modelId;
    private Long clientId;
    private Integer boxId;
    private Long receiptNumber;
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;

    public Car(String number, Long modelId, Long clientId, Integer boxId, Long receiptNumber, LocalDate rentStartDate, LocalDate rentEndDate) {
        this.number = number;
        this.modelId = modelId;
        this.clientId = clientId;
        this.boxId = boxId;
        this.receiptNumber = receiptNumber;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }

    public Car(String number, Long modelId, Long clientId, Integer boxId, LocalDate rentStartDate, LocalDate rentEndDate) {
        this.number = number;
        this.modelId = modelId;
        this.clientId = clientId;
        this.boxId = boxId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public Long getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(Long receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDate getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(LocalDate rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDate getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(LocalDate rentEndDate) {
        this.rentEndDate = rentEndDate;
    }
}
