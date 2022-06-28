package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;
import edu.rsatu.garage.exceptions.DateException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RentController {

    private Client currentClient = null;
    private Model currentModel = null;
    private Box currentBox = null;
    private Car currentCar = null;

    private List<Client> clientsWithoutCar = new ArrayList<>();


    private CarsDao carsDao = new CarsDao();
    private ClientsDao clientsDao = new ClientsDao();

    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}.\\d{2}.\\d{4}$");

    public Long addCar(Client client, Model model, Box box, String carNumber,
                       LocalDate rentStartDate, LocalDate rentEndDate) {
        Car car = new Car(carNumber, model.getId(), client.getId(), box.getId(), rentStartDate, rentEndDate);
        return carsDao.save(car).orElse(null);
    }

    /*
    public static boolean checkNumber(String carNumber){
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
    */

    public static boolean checkNumber2(String carNumber){
        //российский лёгкий автомобиль
        //ГОСТ Р 50577-2018
        String pattern="^[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}";
        return(carNumber.matches(pattern));
    }

    public boolean checkDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    public long getInterval(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new DateException("Нет даты");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new DateException("Дата начала должна быть не меньше, чем сегодняшняя");
        }
        long days = ChronoUnit.DAYS.between(start, end);
        if (days < 1) {
            throw new DateException("Дата окончания аренды должна быть больше даты начала на 1 или более дней");
        }
        if (days > 365) {
            throw new DateException("Бокс не может быть арендован более, чем на 365 дней");
        }
        return days;
    }

    public LocalDate getDateFromString(String date) {
        if (checkDate(date)) {
            return LocalDate.of(Integer.parseInt(date.substring(6)), Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(0, 2)));
        }
        return null;
    }

    public double calculatePrice(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            throw new DateException("Нет даты");
        }
        LocalDate start = getDateFromString(startDate);
        LocalDate end = getDateFromString(endDate);
        long days = getInterval(start, end);
        return calculatePrice(days);
    }

    public double calculatePrice(long days) {
        return currentBox.getRentPrice() * days;
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

    public List<Car> getAllCars(){
        return carsDao.getAll();
    }

    public Long addClient(String surname, String address) {
        Client client = new Client(surname,address);
        return addClient(client);
    }

    public Long addClient(Client client) {
        return clientsDao.save(client).orElse(null);
    }

    public void deleteClient(Client client) {
        clientsDao.delete(client);
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public Model getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(Model currentModel) {
        this.currentModel = currentModel;
    }

    public Box getCurrentBox() {
        return currentBox;
    }

    public void setCurrentBox(Box currentBox) {
        this.currentBox = currentBox;
    }

    public List<Client> getClientsWithoutCar() {
        return clientsWithoutCar;
    }

    public Car getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(Car currentCar) {
        this.currentCar = currentCar;
    }

    public void setClientsWithoutCar(List<Client> clientsWithoutCar) {
        this.clientsWithoutCar = clientsWithoutCar;
    }
}
