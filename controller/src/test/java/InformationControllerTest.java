
import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.BoxesModelsDao;
import edu.rsatu.garage.db.entitiesDao.CarsDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Car;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;
import org.junit.jupiter.api.Test;
import edu.rsatu.garage.controller.InformationController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;




public class InformationControllerTest {

    AdministrationController administrationController = new AdministrationController();

    InformationController informationController = new InformationController();
    RentController rentController = new RentController();

    BoxesDao boxesDao = new BoxesDao();
    ModelsDao modelsDao = new ModelsDao();
    ClientsDao clientsDao =  new ClientsDao();
    CarsDao carsDao = new CarsDao();
    BoxesModelsDao boxesModelsDao = new BoxesModelsDao();

    public void clean(){

        List<Box> boxes = boxesDao.getAll();
        List<Model> models = modelsDao.getAll();
        List<Client> clients = clientsDao.getAll();
        Collection<Car> cars = carsDao.getAll();

        boxesModelsDao.deleteAll();
        for(Car car: cars){
            carsDao.delete(car);
        }
        for(Box box: boxes){
            boxesDao.delete(box);
        }
        for(Model model: models){
            modelsDao.delete(model);
        }
        for(Client client: clients){
            clientsDao.delete(client);
        }
    }

    @Test
    public void getAllBoxes() {
        clean();
        AdministrationController administrationController = new AdministrationController();
        InformationController informationController = new InformationController();
        Model model = new Model("Ford");
        ModelsDao modelsDao = new ModelsDao();
        Long modelId = modelsDao.save(model).orElse(null);
        model = modelsDao.get(modelId).orElse(null);
        assertNotNull(model);
        administrationController.addBox(1,2000, List.of(model));
        administrationController.addBox(2,3000, List.of(model));
        List<Box> boxes = informationController.getAllBoxes();
        assertEquals(2000,boxes.get(0).getRentPrice());
        assertEquals(3000,boxes.get(1).getRentPrice());
    }

    @Test
    public void getFreeBoxes(){
        clean();
        AdministrationController administrationController = new AdministrationController();
        InformationController informationController = new InformationController();
        Model model = new Model("Ford");
        ModelsDao modelsDao = new ModelsDao();
        Long modelId = modelsDao.save(model).orElse(null);
        model = modelsDao.get(modelId).orElse(null);
        assertNotNull(model);
        administrationController.addBox(1,2000, List.of(model));
        administrationController.addBox(2,3000, List.of(model));
        List<Box> boxes = informationController.getFreeBoxes();
        assertEquals(boxes.size(),2);

    }

    @Test
    public void getBoxesForModel(){
        clean();
        administrationController.addModel("Tesla");
        administrationController.addModel("Ford");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        administrationController.addBox(2,3000.00,List.of(models.get(0)));
        List<Box> boxes = informationController.getAllBoxes();
        List<Box> boxesF = informationController.getBoxesForModel(models.get(1));
        assertEquals(boxesF.get(0).hashCode(),boxes.get(0).hashCode());
        for(Box box: boxesF){
            System.out.println("Подходит: "+ box.getId());
        }
    }

    @Test
    public void getFreeBoxesForModel(){
        clean();
        administrationController.addModel("Tesla");
        administrationController.addModel("Ford");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        administrationController.addBox(2,3000.00,List.of(models.get(0)));
        List<Box> boxes = informationController.getAllBoxes();
        administrationController.addClient("Sharov","AdressSharova");
        List<Client> clients = informationController.getAllClients();
        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),boxes.get(0),"s111ss",rentStartDate,rentEndDate);
        Car car = rentController.getCar("s111ss");
        List<Box> boxesF = informationController.getFreeBoxesForModel(models.get(0));
        assertEquals(boxes.get(1).hashCode(),boxesF.get(0).hashCode());
        for(Box box: boxesF){
            System.out.println("Свободен для Tesla: "+ box.getId());
        }
    }

    @Test
    public void getAllModels(){
        clean();
        AdministrationController administrationController = new AdministrationController();
        InformationController informationController = new InformationController();

        administrationController.addModel("Ford");
        administrationController.addModel("Tesla");

        List<Model> models = informationController.getAllModels();
        assertEquals("Ford",models.get(0).getName());
        assertEquals("Tesla",models.get(1).getName());
    }


    @Test
    public void getModelsForBox(){
        clean();
        administrationController.addModel("Tesla");
        administrationController.addModel("Ford");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        administrationController.addBox(2,3000.00,List.of(models.get(0)));
        List<Box> boxes = informationController.getAllBoxes();

        List<Model> modelsF = informationController.getModelsForBox(boxes.get(1));
        assertEquals(modelsF.size(),1);
        assertEquals(modelsF.get(0).getName(),"Tesla");

    }

    @Test
    public void getAllClients(){
        clean();
        AdministrationController administrationController = new AdministrationController();
        InformationController informationController = new InformationController();

        administrationController.addClient("Sharov","AdressSharova");
        administrationController.addClient("Zadorina","AdressZadorinoy");

        List<Client> clients = informationController.getAllClients();
        assertEquals(clients.get(0).getSurname(),"Sharov");
        assertEquals(clients.get(1).getSurname(),"Zadorina");
    }

    @Test
    public void getClientForBox(){
        clean();
        administrationController.addModel("Tesla");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00, models);
        Box box = boxesDao.get(1).orElse(null);
        administrationController.addClient("Sharov","AdressSharova");
        List<Client> clients = informationController.getAllClients();
        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),box,"s111ss",rentStartDate,rentEndDate);
        Client clientF = informationController.getClientForBox(box);
        assertEquals(clientF.getSurname(), "Sharov");

    }

    @Test
    public void getClientsForModel(){
        clean();
        administrationController.addModel("Tesla");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        Box box = boxesDao.get(1).orElse(null);
        administrationController.addClient("Sharov","AdressSharova");
        List<Client> clients = informationController.getAllClients();
        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),box,"s111ss",rentStartDate,rentEndDate);
        List<Client> clientsF = informationController.getClientsForModel(models.get(0));
        assertEquals(clientsF.get(0).getSurname(), "Sharov");
    }

    @Test
    public void getClientsForDate(){
        clean();
        administrationController.addModel("Tesla");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        Box box = boxesDao.get(1).orElse(null);
        administrationController.addClient("Sharov","AdressSharova");
        List<Client> clients = informationController.getAllClients();
        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),box,"s111ss",rentStartDate,rentEndDate);
        LocalDate nowDate = LocalDate.of(2022, 10, 19);
        List<Client> clientsF = informationController.getClientsForDate(nowDate);
        assertEquals(clientsF.get(0).getSurname(), "Sharov");
    }

    @Test
    public void getClientsForModelDate(){
        clean();
        administrationController.addModel("Tesla");
        List<Model> models = informationController.getAllModels();
        administrationController.addBox(1,2500.00,models);
        Box box = boxesDao.get(1).orElse(null);
        administrationController.addClient("Sharov","AdressSharova");
        List<Client> clients = informationController.getAllClients();
        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),box,"s111ss",rentStartDate,rentEndDate);
        LocalDate nowDate = LocalDate.of(2022, 10, 19);
        List<Client> clientsF = informationController.getClientsForModelDate(models.get(0),nowDate);
        assertEquals(clientsF.get(0).getSurname(), "Sharov");
    }

}
