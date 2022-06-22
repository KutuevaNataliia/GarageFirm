
import edu.rsatu.garage.controller.AdministrationController;
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

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;




public class InformationControllerTest {

    AdministrationController administrationController = new AdministrationController();

    InformationController informationController = new InformationController();

    BoxesDao boxesDao = new BoxesDao();
    ModelsDao modelsDao = new ModelsDao();
    ClientsDao clientsDao =  new ClientsDao();
    CarsDao carsDao = new CarsDao();

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

    //что делать с fit ?
    @Test
    public void getBoxesForModel(){

    }

    //что делать с fit ?
    @Test
    public void getFreeBoxesForModel(){

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

    //что делать с fit ?
    @Test
    public void getModelsForBox(){

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

    //надо разобраться с Car
    @Test
    public void getClientForBox(){

    }

    //надо разобраться с Car
    @Test
    public void getClientsForModel(){

    }

    //надо разобраться с Car
    @Test
    public void getClientsForDate(){

    }

    //надо разобраться с Car
    @Test
    public void getClientsForModelDate(){

    }

}
