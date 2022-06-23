package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RentController {

    CarsDao carsDao = new CarsDao();
    public Long addCar(Client client, Model model, Box box, String carNumber,
                       LocalDate rentStartDate, LocalDate rentEndDate) {
        Car car = new Car(carNumber, model.getId(), client.getId(), box.getId(), rentStartDate, rentEndDate);
        return carsDao.save(car).orElse(null);
    }

    public boolean checkNumber(String carNumber){
        boolean result = true;

        if(carNumber.length()<8) return false;
        if(carNumber.length()>9) return false;

        char[] symbols = carNumber.toCharArray();
        if(!Character.isLetter(symbols[0])) return false;
        for(int i = 1; i <= 3;i++){
            if(!Character.isDigit(symbols[i])) return false;
        }
        if(!Character.isLetter(symbols[4])) return false;
        if(!Character.isLetter(symbols[5])) return false;
        for(int i = 6; i < symbols.length;i++){
            if(!Character.isDigit(symbols[i])) return false;
        }

        return true;
    }

    /*что делать с этим выражением дальше ?
    public boolean checkNumber2(String carNumber){
        boolean result = true;
        String pattern="/^[АВЕКМНОРСТУХ]\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\d{2,3}$/ui";
        Pattern r = Pattern.compile(pattern);
        return true;
    }
    */


    public Car getCar(String carNumber) {
        return carsDao.get(carNumber).orElse(null);
    }

    public void deleteCar(String carNumber) {
        carsDao.delete(carsDao.get(carNumber).orElse(null));
    }

    public void updateCar(Car car){
        carsDao.update(car);
    }

    public List<Car> getAllCars(){
        return carsDao.getAll();
    }

}
