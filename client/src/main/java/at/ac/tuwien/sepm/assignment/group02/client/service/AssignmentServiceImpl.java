package at.ac.tuwien.sepm.assignment.group02.client.service;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.PersistenceLayerException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.ServiceLayerException;
import at.ac.tuwien.sepm.assignment.group02.client.rest.AssignmentController;
import at.ac.tuwien.sepm.assignment.group02.client.rest.LumberController;
import at.ac.tuwien.sepm.assignment.group02.client.rest.TaskController;
import at.ac.tuwien.sepm.assignment.group02.client.rest.TimberController;
import at.ac.tuwien.sepm.assignment.group02.client.validation.ValidateAssignmentDTO;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.AssignmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AssignmentController assignmentController;
    private TimberController timberController;
    private LumberController lumberController;
    private TaskController taskController;

    private ValidateAssignmentDTO validateAssignmentDTO;

    @Autowired
    public AssignmentServiceImpl(AssignmentController assignmentController,
                                 TimberController timberController,
                                 LumberController lumberController,
                                 TaskController taskController,
                                 ValidateAssignmentDTO validateAssignmentDTO) {
        this.assignmentController = assignmentController;
        this.timberController = timberController;
        this.lumberController = lumberController;
        this.taskController = taskController;
        this.validateAssignmentDTO = validateAssignmentDTO;
    }

    @Override
    public void createAssignment(AssignmentDTO assignmentDTO) throws ServiceLayerException {
    }

    @Override
    public List<AssignmentDTO> getAllOpenAssignments() throws ServiceLayerException {
        return null;
    }

    @Override
    public void setDone(AssignmentDTO assignmentDTO) throws ServiceLayerException {

        if(!assignmentDTO.isDone())
            assignmentDTO.setDone(true);
        else throw new ServiceLayerException("the assignment is already marked as done.");

        try {
            validateAssignmentDTO.isValid(assignmentDTO);
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            throw new ServiceLayerException("the provided assignment is invalid: "+e.getMessage());
        }

        // 3.2.2 (rest/AssignmentController) Aufgabe als erledigt markieren.
        try {
            assignmentController.setDone(assignmentDTO);
        } catch (PersistenceLayerException e) {
            LOG.warn(e.getMessage());
            throw new ServiceLayerException("error on persistence layer.");
        }

    }
}
