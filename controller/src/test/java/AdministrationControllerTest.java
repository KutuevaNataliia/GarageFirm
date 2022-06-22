
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ClientsDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;
import org.junit.jupiter.api.Test;
import edu.rsatu.garage.controller.AdministrationController;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdministrationControllerTest {

    public void clean(){
        BoxesDao boxesDao = new BoxesDao();
        ModelsDao modelsDao = new ModelsDao();
        ClientsDao clientsDao =  new ClientsDao();

        List<Box> boxes = boxesDao.getAll();
        List<Model> models = modelsDao.getAll();
        List<Client> clients = clientsDao.getAll();

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
    public void addBox() {
        AdministrationController administrationController = new AdministrationController();
        BoxesDao dao = new BoxesDao();
        clean();
        administrationController.addBox(1,1000.00);
        Box box = dao.get(1).orElse(null);
        assertNotNull(box);
        assertEquals(1000.00, box.getRentPrice());
        List<Box> boxes = dao.getAll();
        assertEquals(1, (int) boxes.get(0).getId());
        assertEquals(1000.00, boxes.get(0).getRentPrice());
    }

    @Test
    public void deleteBox() {
        clean();
        AdministrationController administrationController = new AdministrationController();
        administrationController.addBox(1,1000.00);
        administrationController.deleteBox(1);
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(1).orElse(null);
        assertNull(box);
    }

    @Test
    public void updateBox() {
        clean();
        AdministrationController administrationController = new AdministrationController();
        administrationController.addBox(1,1000.00);
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(1).orElse(null);
        if (box != null) {
            box.setRentPrice(1050.00);
        }
        dao.update(box);
        Box newBox = dao.get(1).orElse(null);
        assertNotNull(newBox);
        assertEquals(1050, newBox.getRentPrice());
    }



    //id марки постоянно растёт в базе данных, поэтому удаление и добавление учитывает номер марки в списке
    //всех марок БД
    @Test
    public void addModel(){
        clean();
        AdministrationController administrationController = new AdministrationController();
        ModelsDao dao = new ModelsDao();
        administrationController.addModel("Ford");
        List<Model> models = dao.getAll();
        Model model = dao.get(models.get(0).getId()).orElse(null);
        assertNotNull(model);
        assertEquals("Ford", model.getName());
        assertEquals("Ford", models.get(0).getName());
    }
    @Test
    public void deleteModel() {
        clean();
        AdministrationController administrationController = new AdministrationController();
        ModelsDao dao = new ModelsDao();
        administrationController.addModel("Ford");
        List<Model> models = dao.getAll();
        if(!models.get(0).equals(null))
        administrationController.deleteModel(models.get(0));
        Model m = dao.get(models.get(0).getId()).orElse(null);
        assertNull(m);
    }

    @Test
    public void increaseBoxPrices() {
        clean();

        AdministrationController administrationController = new AdministrationController();
        BoxesDao boxesDao = new BoxesDao();

        administrationController.addBox(1,1000.00);
        administrationController.addBox(2,500);
        administrationController.increaseBoxPrices(2);
        List<Box> boxes = boxesDao.getAll();
        assertEquals(2000,boxes.get(0).getRentPrice());
        assertEquals(1000,boxes.get(1).getRentPrice());

    }

    @Test
    public void decreaseBoxPrices() {
        clean();
        AdministrationController administrationController = new AdministrationController();
        BoxesDao boxesDao = new BoxesDao();

        administrationController.addBox(1,1000.00);
        administrationController.addBox(2,500);

        administrationController.decreaseBoxPrices(2);
        List<Box> boxes = boxesDao.getAll();
        assertEquals(500,boxes.get(0).getRentPrice());
        assertEquals(250,boxes.get(1).getRentPrice());
    }
}
