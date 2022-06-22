import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
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

public class RentControllerTest {

    BoxesDao boxesDao = new BoxesDao();
    ModelsDao modelsDao = new ModelsDao();
    ClientsDao clientsDao =  new ClientsDao();
    CarsDao carsDao = new CarsDao();


    AdministrationController administrationController = new AdministrationController();

    InformationController informationController = new InformationController();

    RentController rentController = new RentController();
    public void clean(){

        List<Box> boxes = boxesDao.getAll();
        List<Model> models = modelsDao.getAll();
        List<Client> clients = clientsDao.getAll();
        Collection<Car> cars = carsDao.getAll();

        for(Box box: boxes){
            boxesDao.delete(box);
        }
        for(Model model: models){
            modelsDao.delete(model);
        }
        for(Client client: clients){
            clientsDao.delete(client);
        }
        for(Car car: cars){
            carsDao.delete(car);
        }
    }

    @Test
    public void addCar() {
        clean();
        administrationController.addBox(1,500);
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

}
