import edu.rsatu.garage.controller.AdministrationController;
import edu.rsatu.garage.db.entitiesDao.BoxesDao;
import edu.rsatu.garage.entities.Box;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdministrationControllerTest {

    @Test
    public void addBox() {
        AdministrationController administrationController = new AdministrationController();
        administrationController.addBox(1,1000.00);
        BoxesDao dao = new BoxesDao();
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
        Box box = dao.get(1).orElse(null);
        if (box != null) {
            box.setRentPrice(1050.00);
        }
        dao.update(box);
        Box newBox = dao.get(1).orElse(null);
        assertNotNull(newBox);
        assertEquals(1050, newBox.getRentPrice());
    }
}
