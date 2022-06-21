package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

public class AdministrationController {

    public boolean addBox(int boxId, double rentPrice) {
        Box box = new Box(boxId, rentPrice);
        BoxesDao dao = new BoxesDao();
        dao.save(box);
        return true;
    }

    public void deleteBox(int boxNum) {
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(boxNum).orElse(null);
        dao.delete(box);
    }

    public boolean addModel(String name) {
        Model model = new Model(name);
        ModelsDao modelsDao = new ModelsDao();
        modelsDao.save(model);
        return true;
    }

    public void deleteModel(Model model) {
        ModelsDao modelsDao = new ModelsDao();
        modelsDao.delete(model);
    }

    public void increaseBoxPrices(int number) {
        BoxesDao boxesDao = new BoxesDao();
        boxesDao.changeAllPrices(number, true);
    }

    public void decreaseBoxPrices(int number) {
        BoxesDao boxesDao = new BoxesDao();
        boxesDao.changeAllPrices(number, false);
    }
}
