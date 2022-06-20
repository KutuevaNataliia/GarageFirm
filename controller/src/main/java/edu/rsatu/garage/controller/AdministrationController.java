package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

public class AdministrationController {

    public void addBox(int boxId, double rentPrice) {
        Box box = new Box(boxId, rentPrice);
        BoxesDao dao = new BoxesDao();
        dao.save(box);
    }

    public void deleteBox(int boxNum) {
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(boxNum).orElse(null);
        dao.delete(box);
    }

    public void addModel(String name) {
        Model model = new Model(name);
        ModelsDao modelsDao = new ModelsDao();
    }

}
