package at.ac.tuwien.sepm.assignment.group02.client.validation;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.NoValidIntegerException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.AssignmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ValidateAssignmentDTO implements ValidateInput<AssignmentDTO> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public boolean isValid(AssignmentDTO input) throws InvalidInputException {
        LOG.debug("validating input");

        int id = input.getId();
        int amount = input.getAmount();
        int box_id = input.getBox_id();

        if (id < 0) {
            LOG.warn("input.getId() is negative");
            throw new InvalidInputException("id must not be negative");
        }
        isNumber(id+"");

        if (amount < 1) {
            LOG.warn("input.getAmount() less than 1");
            throw new InvalidInputException("amount must be greater than zero");
        }
        isNumber(amount+"");


        if (box_id < 0) {
            LOG.warn("input.getBox_id() is negative");
            throw new InvalidInputException("box id must not be negative");
        }
        isNumber(box_id+"");


        /* TODO: validate Date
        if (input.getCreation_date().before(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))) {
            LOG.warn("input.getCreation_date() is in the future");
            throw new InvalidInputException("creation date must be in the past");
        }
        */

        return true;
    }

    private void isNumber(String string) throws NoValidIntegerException {
        try {
            int num = Integer.parseInt(string);
            if (num < 0) {
                throw new NoValidIntegerException("Negative Integer entered");
            }
        } catch (NumberFormatException e) {
            LOG.error("No valid Integer entered");
            throw new NoValidIntegerException("No valid Integer entered");
        }
    }
}
