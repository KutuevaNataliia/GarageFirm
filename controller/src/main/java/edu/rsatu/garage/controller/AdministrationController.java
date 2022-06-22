package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

public class AdministrationController {

    private BoxesDao boxesDao = new BoxesDao();
    private ModelsDao modelsDao = new ModelsDao();
    private ClientsDao clientsDao = new ClientsDao();

    public boolean addBox(int boxId, double rentPrice) {
        Box box = new Box(boxId, rentPrice);
        boxesDao.save(box);
        return true;
    }

    public void deleteBox(int boxNum) {
        Box box = boxesDao.get(boxNum).orElse(null);
        boxesDao.delete(box);
    }

    public boolean addModel(String name) {
        Model model = new Model(name);
        modelsDao.save(model);
        return true;
    }

    public void deleteModel(Model model) {
        modelsDao.delete(model);
    }

    public void increaseBoxPrices(int number) {
        boxesDao.changeAllPrices(number, true);
    }

    public void decreaseBoxPrices(int number) {
        boxesDao.changeAllPrices(number, false);
    }

    public boolean addClient(String surname, String address) {
        Client client = new Client(surname,address);
        clientsDao.save(client);
        return true;
    }


}
