package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.BoxesModelsDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.util.List;

public class AdministrationController {

    private BoxesDao boxesDao = new BoxesDao();
    private ModelsDao modelsDao = new ModelsDao();
    private ClientsDao clientsDao = new ClientsDao();
    private BoxesModelsDao boxesModelsDao = new BoxesModelsDao();

    public boolean addBox(int boxId, double rentPrice, List<Model> models) {
        //надо бы ещё сделать исключение, если список моделей null или пустой
        Box box = new Box(boxId, rentPrice);
        boxesDao.save(box);
        boxesModelsDao.addModelsForBox(box, models);
        return true;
    }

    public void deleteBox(Box box) {
        List<Model> models = boxesModelsDao.getModelsForBox(box);
        boxesModelsDao.deleteModelsFromBox(box, models);
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
