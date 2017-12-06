package at.ac.tuwien.sepm.assignment.group02.rest.restController;

import at.ac.tuwien.sepm.assignment.group02.rest.exceptions.EntityCreationException;
import at.ac.tuwien.sepm.assignment.group02.rest.exceptions.EntityNotFoundException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.AssignmentDTO;

import java.util.List;

public interface AssignmentController {

    /**
     * 2.4.4 Neue Aufgabe für Kranfahrer erstellen.
     */
    void createAssignment(AssignmentDTO assignmentDTO) throws EntityCreationException;

    /**
     * 3.1.2 Alle nicht erledigten Aufgaben anfordern.
     */
    List<AssignmentDTO> getAllOpenAssignments() throws EntityNotFoundException;

    /**
     * 3.2.2 Aufgabe als erledigt markieren.
     */
    void setDone(AssignmentDTO assignmentDTO) throws EntityNotFoundException;

}
