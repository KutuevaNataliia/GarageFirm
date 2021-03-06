package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.Pair;
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.BoxesModelsDao;
import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class InformationController {

    private BoxesDao boxesDao = new BoxesDao();
    private ModelsDao modelsDao = new ModelsDao();
    private BoxesModelsDao boxesModelsDao = new BoxesModelsDao();
    private ClientsDao clientsDao = new ClientsDao();
    private CarsDao carsDao = new CarsDao();

    public List<Box> getAllBoxes() {
        return  boxesDao.getAll();
    }

    public List<Box> getFreeBoxes() {
        return boxesDao.getFreeBoxes();

    }

    public List<Box> getBoxesForModel(Model model) {
        return boxesModelsDao.getBoxesForModel(model);
    }

    public List<Box> getFreeBoxesForModel(Model model) {
        return boxesModelsDao.getFreeBoxesForModel(model);
    }

    public Car getCarByNumber(String number) {
        return carsDao.get(number).orElse(null);
    }

    public Client getClientById(Long id) {
        return clientsDao.get(id).orElse(null);
    }

    public Model getModelById(Long id) {
        return modelsDao.get(id).orElse(null);
    }

    public Box getBoxByNumber(int number) {
        return boxesDao.get(number).orElse(null);
    }

    public List<Model> getAllModels() {

        return modelsDao.getAll();
    }

    public Model getModelByName(String name){
        Model rModel = null;
        List<Model> models = getAllModels();
        for(Model model: models){
            if(model.getName().equals(name)){
                rModel = model;
                break;
            }
        }
        return rModel;
    }

    public List<Model> getModelsForBox(Box box) {

        return boxesModelsDao.getModelsForBox(box);
    }

    public List<Client> getAllClients() {

        return clientsDao.getAll();
    }

    public Client getClientForBox(Box box) {

        return clientsDao.getClientForBox(box).orElse(null);
    }

    public List<Client> getClientsForModel(Model model) {

        return clientsDao.getClientsForModel(model);
    }

    public List<Client> getClientsForDate(LocalDate date) {

        return clientsDao.getClientsForDate(date);
    }

    public List<Client> getClientsForModelDate(Model model, LocalDate date) {

        return clientsDao.getClientsForModelDate(model, date);
    }

    public List<Car> getCarsForClient(Client client) {
        return carsDao.getCarsForClientId(client.getId());
    }

    public List<Car> getCarsForBox(Box box) {
        return carsDao.getCarsForBoxNumber(box.getId());
    }

    public List<Car> getCarsForModel(Model model) {
        return carsDao.getCarsForModelId(model.getId());
    }

    public List<Pair<Integer>> getIntervalsStatistic() {
        return carsDao.getRentIntervalsStatistic();
    }

    public double getAverageRentInterval() {
        return carsDao.getAverageRentInterval();
    }

}
