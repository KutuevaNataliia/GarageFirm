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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentControllerTest {

    BoxesDao boxesDao = new BoxesDao();
    ModelsDao modelsDao = new ModelsDao();
    ClientsDao clientsDao =  new ClientsDao();
    CarsDao carsDao = new CarsDao();
    BoxesModelsDao boxesModelsDao = new BoxesModelsDao();


    AdministrationController administrationController = new AdministrationController();

    InformationController informationController = new InformationController();

    RentController rentController = new RentController();
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
    public void addCar() {
        clean();
        Model model = new Model("Ford");
        ModelsDao modelsDao = new ModelsDao();
        Long modelId = modelsDao.save(model).orElse(null);
        model = modelsDao.get(modelId).orElse(null);
        assertNotNull(model);
        administrationController.addBox(1,500, List.of(model));
        administrationController.addClient("Sharov","AdressSharova");
        administrationController.addModel("Ford");
        List<Client> clients = informationController.getAllClients();
        List<Box> boxes = informationController.getAllBoxes();
        List<Model> models  = informationController.getAllModels();

        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);

        rentController.addCar(clients.get(0),models.get(0),boxes.get(0),"s111ss",rentStartDate,rentEndDate);

        Car car = rentController.getCar("s111ss");
        assertEquals(car.getRentEndDate(),rentEndDate);

    }

    @Test
    public void getCar() {
        clean();
        //уже протестирован при добавлении
    }

    @Test
    public void deleteCar() {
        clean();

        Model model = new Model("Ford");
        ModelsDao modelsDao = new ModelsDao();
        Long modelId = modelsDao.save(model).orElse(null);
        model = modelsDao.get(modelId).orElse(null);
        assertNotNull(model);
        administrationController.addBox(1,500, List.of(model));
        administrationController.addClient("Sharov","AdressSharova");
        administrationController.addModel("Ford");
        List<Client> clients = informationController.getAllClients();
        List<Box> boxes = informationController.getAllBoxes();
        List<Model> models  = informationController.getAllModels();

        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);

        rentController.addCar(clients.get(0),models.get(0),boxes.get(0),"s111ss",rentStartDate,rentEndDate);
        rentController.deleteCar("s111ss");
        Car car = carsDao.get("s111ss").orElse(null);
        assertEquals(car,null);
    }


    @Test
    public void updateCar() {
        clean();
        Model model = new Model("Ford");
        Long modelId = modelsDao.save(model).orElse(null);
        model = modelsDao.get(modelId).orElse(null);
        assertNotNull(model);
        administrationController.addBox(1,500, List.of(model));
        administrationController.addClient("Sharov","AdressSharova");
        administrationController.addModel("Ford");
        List<Client> clients = informationController.getAllClients();
        List<Box> boxes = informationController.getAllBoxes();
        List<Model> models  = informationController.getAllModels();

        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),boxes.get(0),"s111ss",rentStartDate,rentEndDate);

        Car newcar = rentController.getCar("s111ss");
        LocalDate newRentEndDate = LocalDate.of(2022, 10, 19);
        newcar.setRentEndDate(newRentEndDate);
        rentController.updateCar(newcar);
        Car nowcar  = rentController.getCar("s111ss");
        assertEquals(newcar.getRentEndDate(),nowcar.getRentEndDate());
    }

    @Test
    public void getAllCars() {
        clean();
        administrationController.addModel("Ford");
        administrationController.addModel("Tesla");
        List<Model> models  = informationController.getAllModels();
        administrationController.addBox(1,500, List.of(models.get(0)));
        administrationController.addBox(2,1000, List.of(models.get(1)));
        List<Box> boxes = informationController.getAllBoxes();
        administrationController.addClient("Sharov","AdressSharova");
        administrationController.addClient("Zadorina","AdressZadorinoy");
        List<Client> clients = informationController.getAllClients();

        LocalDate rentStartDate = LocalDate.of(2022, 6, 19);
        LocalDate rentEndDate = LocalDate.of(2022, 8, 19);
        rentController.addCar(clients.get(0),models.get(0),boxes.get(0),"s111ss",rentStartDate,rentEndDate);
        rentController.addCar(clients.get(1),models.get(1),boxes.get(1),"s222ss",rentStartDate,rentEndDate);

        List<Car> cars = rentController.getAllCars();
        Car car1 = cars.get(0);
        assertEquals("s111ss    ", car1.getNumber());
        assertEquals(rentStartDate, car1.getRentStartDate());
        assertEquals(rentEndDate, car1.getRentEndDate());
        Car car2 = cars.get(1);
        assertEquals("s222ss    ", car2.getNumber());
        assertEquals(rentStartDate, car2.getRentStartDate());
        assertEquals(rentEndDate, car2.getRentEndDate());
    }

}
