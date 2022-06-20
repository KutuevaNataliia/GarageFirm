package edu.rsatu.garage.entities;

import java.util.Date;

public class Car {
    private String number;
    private Integer modelId;
    private Integer clientId;
    private Integer boxId;
    private Integer receiptNumber;
    private Date rentStartDate;
    private Date rentEndDate;

    public Car(String number, Integer modelId, Integer clientId, Integer boxId, Integer receiptNumber, Date rentStartDate, Date rentEndDate) {
        this.number = number;
        this.modelId = modelId;
        this.clientId = clientId;
        this.boxId = boxId;
        this.receiptNumber = receiptNumber;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public Integer getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(Integer receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(Date rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public Date getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(Date rentEndDate) {
        this.rentEndDate = rentEndDate;
    }
}
