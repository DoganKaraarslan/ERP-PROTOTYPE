package at.ac.tuwien.sepm.assignment.group02.client.rest;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.PersistenceLayerException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.AssignmentDTO;

import java.util.List;

public interface AssignmentController {

    /**
     * create a new assignment for crane
     * @param assignmentDTO
     * @throws PersistenceLayerException
     */
    void createAssignment(AssignmentDTO assignmentDTO) throws PersistenceLayerException;

    /**
     * retrieve all not finished assignments
     * @return a list of all open assignments
     * @throws PersistenceLayerException
     */
    List<AssignmentDTO> getAllOpenAssignments() throws PersistenceLayerException;

    /**
     * retrieve all assignments
     * @return a list of all assignments
     * @throws PersistenceLayerException
     */
    List<AssignmentDTO> getAllAssignments() throws PersistenceLayerException;

    /**
     * to mark an assignment as done
     * @param assignmentDTO
     * @throws PersistenceLayerException
     */
    void setDone(AssignmentDTO assignmentDTO) throws PersistenceLayerException;

}
