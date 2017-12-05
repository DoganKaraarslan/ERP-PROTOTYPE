package at.ac.tuwien.sepm.assignment.group02.server.persistence;

import at.ac.tuwien.sepm.assignment.group02.rest.entity.Lumber;
import at.ac.tuwien.sepm.assignment.group02.rest.entity.Task;
import at.ac.tuwien.sepm.assignment.group02.server.exceptions.PersistenceLevelException;

import java.util.List;

public interface TaskDAO {


    /**
     * 1.1.3 Aufträge erstellen
     */
    void createTask(Task task) throws PersistenceLevelException;

    /**
     * 1.2.1 Aufträge löschen
     */
    void deleteTask(Task task) throws PersistenceLevelException;

    /**
     * 2.2.3 & 3.2.6 Reserviertes Schnittholz dem Auftrag hinzufügen.
     */
    void updateTask(Task task) throws PersistenceLevelException;

    /**
     * 2.5.2 Eine tabellarische Übersicht der vorhandenen Aufträge anzeigen.
     */
    List<Task> getAllOpenTasks() throws PersistenceLevelException;

    /**
     * 3.2.7 maybe not needed.
     */
    void getTaskById(int task_id) throws PersistenceLevelException;

}