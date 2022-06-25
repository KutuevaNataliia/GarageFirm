package edu.rsatu.garage.controller;

import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.BoxesModelsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

import java.util.List;

public class AdministrationController {

    private BoxesDao boxesDao = new BoxesDao();
    private ModelsDao modelsDao = new ModelsDao();
    private BoxesModelsDao boxesModelsDao = new BoxesModelsDao();

    private InformationController informationController = new InformationController();

    public boolean addBox(int boxId, double rentPrice, List<Model> models) {
        //надо бы ещё сделать исключение, если список моделей null или пустой
        Box box = new Box(boxId, rentPrice);
        boxesDao.save(box);
        boxesModelsDao.addModelsForBox(box, models);
        return true;
    }

    public void deleteBox(Box box) {
        boxesDao.delete(box);
    }

    public void deleteBoxById(Integer id){
        List<Box> boxes = informationController.getAllBoxes();
        for(Box box:boxes){
            if(box.getId().equals(id)){
                deleteBox(box);
                break;
            }
        }
    }

    public boolean addModel(String name) {
        Model model = new Model(name);
        modelsDao.save(model);
        return true;
    }

    public void deleteModel(Model model) {
        modelsDao.delete(model);
    }

    public void deleteModelByName(String name) {
        List<Model> models = informationController.getAllModels();
        for(Model model: models){
            if(model.getName().equals(name)){
                modelsDao.delete(model);
                break;
            }
        }
    }

    public void increaseBoxPrices(double number) {
        boxesDao.changeAllPrices(number, true);
    }

    public void decreaseBoxPrices(double number) {
        boxesDao.changeAllPrices(number, false);
    }

}
