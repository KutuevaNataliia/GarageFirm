package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;

public class RentController {

    public Long addCar(Client client, Model model, Box box, String carNumber,
                       LocalDate rentStartDate, LocalDate rentEndDate) {
        Car car = new Car(carNumber, model.getId(), client.getId(), box.getId(), rentStartDate, rentEndDate);
        CarsDao carsDao = new CarsDao();
        return carsDao.save(car).orElse(null);
    }

    public Car getCar(String carNumber) {
        CarsDao carsDao = new CarsDao();
        return carsDao.get(carNumber).orElse(null);
    }
}
