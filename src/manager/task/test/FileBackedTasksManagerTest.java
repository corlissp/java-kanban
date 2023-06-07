package manager.task.test;

import manager.Managers;
import manager.task.FileBackedTasksManager;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    final private FileBackedTasksManager manager = Managers.fileManager();

    FileBackedTasksManagerTest() {
        super(Managers.fileManager());
    }

    @Order(1)
    @Test
    void getHistory() {
        super.getHistory();
        manager.save();
        try {
            Path path = Paths.get("resources");
            Path absPath = path.toAbsolutePath();
            String separator = File.separator;
            File file = new File(absPath + separator + "TaskManager.scv");
            List<String> list = new ArrayList<>();
            String line;
            BufferedReader br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            list.add(line);
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            Assertions.assertEquals(list.get(15), "12,2,14");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void save() throws Exception {
        Path path = Paths.get("resources");
        Path absPath = path.toAbsolutePath();
        String separator = File.separator;
        File file = new File(absPath + separator + "TaskManager.scv");
        List<String> list = new ArrayList<>();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        line = br.readLine();
        list.add(line);
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        Assertions.assertEquals(list.get(0), "1,EPIC,Epic(1),NEW,Empty,13:00 10.01.24,13:00 10.01.24");
        Assertions.assertEquals(list.get(1), "2,EPIC,Epic(2),NEW,All new,13:00 11.01.24,19:00 11.01.24");
        Assertions.assertEquals(list.get(2), "3,SUB,Sub 1 by Epic(2),NEW,New status,2,13:10 11.01.24,16:10 11.01.24");
        Assertions.assertEquals(list.get(3), "4,SUB,Sub 2 by Epic(2),NEW,New status,2,13:00 12.01.24,16:00 12.01.24");
        Assertions.assertEquals(list.get(4), "5,EPIC,Epic(3),NEW,All done,13:00 13.01.24,19:00 13.01.24");
        Assertions.assertEquals(list.get(5), "6,SUB,Sub 1 by Epic(3),NEW,Done status,5,13:10 13.01.24,16:10 13.01.24");
        Assertions.assertEquals(list.get(6), "7,SUB,Sub 2 by Epic(3),NEW,Done status,5,13:00 14.01.24,16:00 14.01.24");
        Assertions.assertEquals(list.get(7), "8,EPIC,Epic(4),NEW,All done,13:00 15.01.24,19:00 15.01.24");
        Assertions.assertEquals(list.get(8), "9,SUB,Sub 1 by Epic(4),NEW,New status,8,13:10 15.01.24,16:10 15.01.24");
        Assertions.assertEquals(list.get(9), "10,SUB,Sub 2 by Epic(4),NEW,Done status,8,13:00 16.01.24,16:00 16.01.24");
        Assertions.assertEquals(list.get(10), "11,EPIC,Epic(5),NEW,All done,13:00 17.01.24,19:00 17.01.24");
        Assertions.assertEquals(list.get(11), "12,SUB,Sub 1 by Epic(5),NEW,New status,11,13:10 17.01.24,16:10 17.01.24");
        Assertions.assertEquals(list.get(12), "13,SUB,Sub 2 by Epic(5),NEW,In progress status,11,13:00 18.01.24,16:00 18.01.24");
        Assertions.assertEquals(list.get(13), "14,SINGLE,Single,NEW,Single id 14,13:00 19.01.24,16:00 19.01.24");
        Assertions.assertTrue(list.get(14).isEmpty());
    }
    @Test
    void getSingleTaskById() {
        super.getSingleTaskById();
    }

    @Test
    void saveNewSingleTask() {
        super.saveNewSingleTask();
    }

    @Test
    void saveNewSubTask() {
        super.saveNewSubTask();
    }

    @Test
    void saveNewEpicTask() {
        super.saveNewEpicTask();
    }

    @Test
    void updateSingleStatus() {
        super.updateSingleStatus();
    }

    @Test
    void updateSubStatus() {
        super.updateSubStatus();
    }

    @Test
    void updateEpicStatus() {
        super.updateEpicStatus();
    }

    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Test
    void getEpicTaskById() {
        super.getEpicTaskById();
    }

    @Test
    void getAllTasks() {
        super.getAllTasks();
    }

    @Test
    void getAllSingleTasks() {
        super.getAllSingleTasks();
    }

    @Test
    void getAllSubTasks() {
        super.getAllSubTasks();
    }

    @Test
    void getAllEpicTasks() {
        super.getAllEpicTasks();
    }

    @Test
    void getSubFromEpic() {
        super.getSubFromEpic();
    }

    @Test
    void getEpicFromSub() {
        super.getEpicFromSub();
    }

    @Test
    void deleteSingleById() {
        super.deleteSingleById();
    }

    @Test
    void deleteSubById() {
        super.deleteSubById();
    }

    @Test
    void deleteEpicById() {
        super.deleteEpicById();
    }

    @Test
    void clearSubTasks() {
        super.clearSubTasks();
    }

    @Test
    void clearSingleTasks() {
        super.clearSingleTasks();
    }

    @Test
    void clearEpicTasks() {
        super.clearEpicTasks();
    }

    @Test
    void clearAll() {
        super.clearAll();
    }
}