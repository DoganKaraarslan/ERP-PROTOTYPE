package at.ac.tuwien.sepm.assignment.group02.server.persistence;

import at.ac.tuwien.sepm.assignment.group02.server.entity.Assignment;
import at.ac.tuwien.sepm.assignment.group02.server.exceptions.PersistenceLayerException;

import java.util.List;

public interface AssignmentDAO {

    /**
     * 2.4.4 Neue Aufgabe für Kranfahrer erstellen.
     */
    void createAssignment(Assignment assignment) throws PersistenceLayerException;

    /**
     * 3.1.2 Alle nicht erledigten Aufgaben anfordern.
     * @return list of all open assignments to be shown in the assignment overview for crane
     * @throws PersistenceLayerException if the database is not available
     */
    List<Assignment> getAllAssignments() throws PersistenceLayerException;

    /**
     * 3.2.2 Aufgabe als erledigt markieren.
     */
    void updateAssignment(Assignment assignment) throws PersistenceLayerException;
    void deleteAssignment(int id) throws PersistenceLayerException;

}
