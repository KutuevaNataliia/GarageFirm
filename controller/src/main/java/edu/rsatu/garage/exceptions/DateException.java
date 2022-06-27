package edu.rsatu.garage.exceptions;

public class DateException extends IllegalArgumentException{
    public DateException(String s) {
        super(s);
    }

    public String getMessage() {
        return "Неправильно задана дата: " + super.getMessage();
    }
}
