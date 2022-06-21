package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.time.LocalDate;
import java.util.List;

public class InformationController {

    public List<Box> getAllBoxes() {
        BoxesDao dao = new BoxesDao();
        return  dao.getAll();
    }

    public List<Box> getFreeBoxes() {
        BoxesDao boxesDao = new BoxesDao();
        return boxesDao.getFreeBoxes();
    }

    public List<Box> getBoxesForModel(Model model) {
        BoxesDao boxesDao = new BoxesDao();
        return boxesDao.getBoxesForModel(model);
    }

    public List<Box> getFreeBoxesForModel(Model model) {
        BoxesDao boxesDao = new BoxesDao();
        return boxesDao.getFreeBoxesForModel(model);
    }

    public List<Model> getAllModels() {
        ModelsDao modelsDao = new ModelsDao();
        return modelsDao.getAll();
    }

    public List<Model> getModelsForBox(Box box) {
        ModelsDao modelsDao = new ModelsDao();
        return modelsDao.getModelsForBox(box);
    }

    public List<Client> getAllClients() {
        ClientsDao clientsDao = new ClientsDao();
        return clientsDao.getAll();
    }

    public Client getClientForBox(Box box) {
        ClientsDao clientsDao = new ClientsDao();
        return clientsDao.getClientForBox(box).orElse(null);
    }

    public List<Client> getClientsForModel(Model model) {
        ClientsDao clientsDao = new ClientsDao();
        return clientsDao.getClientsForModel(model);
    }

    public List<Client> getClientsForDate(LocalDate date) {
        ClientsDao clientsDao = new ClientsDao();
        return clientsDao.getClientsForDate(date);
    }

    public List<Client> getClientsForModelDate(Model model, LocalDate date) {
        ClientsDao clientsDao = new ClientsDao();
        return clientsDao.getClientsForModelDate(model, date);
    }
}
