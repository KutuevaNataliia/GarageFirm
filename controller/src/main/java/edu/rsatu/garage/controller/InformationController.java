package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.BoxesModelsDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;
import java.util.List;

public class InformationController {

    private BoxesDao boxesDao = new BoxesDao();
    private ModelsDao modelsDao = new ModelsDao();
    private BoxesModelsDao boxesModelsDao = new BoxesModelsDao();
    private ClientsDao clientsDao = new ClientsDao();

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

    public List<Model> getAllModels() {

        return modelsDao.getAll();
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
}
