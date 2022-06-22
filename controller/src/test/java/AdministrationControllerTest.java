
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.db.entitiesDao.ModelsDao;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;
import org.junit.jupiter.api.Test;
import edu.rsatu.garage.controller.AdministrationController;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdministrationControllerTest {

    @Test
    public void addBox() {
        AdministrationController administrationController = new AdministrationController();
        BoxesDao dao = new BoxesDao();
        if(dao.get(1).equals(null))administrationController.deleteBox(1);
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
        AdministrationController administrationController = new AdministrationController();
        administrationController.deleteBox(1);
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(1).orElse(null);
        assertNull(box);
    }

    @Test
    public void updateBox() {
        BoxesDao dao = new BoxesDao();
        Box box = dao.get(5).orElse(null);
        if (box != null) {
            box.setRentPrice(1050.00);
        }
        dao.update(box);
        Box newBox = dao.get(5).orElse(null);
        assertNotNull(newBox);
        assertEquals(1050, newBox.getRentPrice());
    }



    //id марки постоянно растёт в базе данных, поэтому удаление и добавление учитывает номер марки в списке
    //всех марок БД
    @Test
    public void addModel(){
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
        AdministrationController administrationController = new AdministrationController();
        ModelsDao dao = new ModelsDao();
        List<Model> models = dao.getAll();
        if(!models.get(0).equals(null))
        administrationController.deleteModel(models.get(0));
        Model m = dao.get(models.get(0).getId()).orElse(null);
        assertNull(m);
    }

    @Test
    public void increaseBoxPrices() {
        AdministrationController administrationController = new AdministrationController();
        BoxesDao boxesDao = new BoxesDao();

        if(boxesDao.get(1).equals(null))
        administrationController.addBox(1,1000.00);
        else
        {
            Box box = boxesDao.get(1).orElse(null);
            box.setRentPrice(1000.00);
            boxesDao.update(box);
        }
        if(boxesDao.get(2).equals(null))
        administrationController.addBox(2,500);
        else
        {
            Box box = boxesDao.get(2).orElse(null);
            box.setRentPrice(500.00);
            boxesDao.update(box);
        }
        administrationController.increaseBoxPrices(2);
        List<Box> boxes = boxesDao.getAll();
        assertEquals(2000,boxes.get(0).getRentPrice());
        assertEquals(1000,boxes.get(1).getRentPrice());

    }

    @Test
    public void decreaseBoxPrices() {
        AdministrationController administrationController = new AdministrationController();
        BoxesDao boxesDao = new BoxesDao();

        if(boxesDao.get(1).equals(null))
            administrationController.addBox(1,1000.00);
        else
        {
            Box box = boxesDao.get(1).orElse(null);
            box.setRentPrice(1000.00);
            boxesDao.update(box);
        }
        if(boxesDao.get(2).equals(null))
            administrationController.addBox(2,500);
        else
        {
            Box box = boxesDao.get(2).orElse(null);
            box.setRentPrice(500.00);
            boxesDao.update(box);
        }
        administrationController.decreaseBoxPrices(2);
        List<Box> boxes = boxesDao.getAll();
        assertEquals(500,boxes.get(0).getRentPrice());
        assertEquals(250,boxes.get(1).getRentPrice());
    }
}
