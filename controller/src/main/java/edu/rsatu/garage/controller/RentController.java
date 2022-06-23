package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;
import java.util.Collection;

public class RentController {

    CarsDao carsDao = new CarsDao();
    public Long addCar(Client client, Model model, Box box, String carNumber,
                       LocalDate rentStartDate, LocalDate rentEndDate) {
        Car car = new Car(carNumber, model.getId(), client.getId(), box.getId(), rentStartDate, rentEndDate);
        return carsDao.save(car).orElse(null);
    }

    public Car getCar(String carNumber) {
        return carsDao.get(carNumber).orElse(null);
    }

    public void deleteCar(String carNumber) {
        carsDao.delete(carsDao.get(carNumber).orElse(null));
    }

    public void updateCar(Car car){
        carsDao.update(car);
    }

    public Collection<Car> getAllCars(){
        return carsDao.getAll();
    }

}
