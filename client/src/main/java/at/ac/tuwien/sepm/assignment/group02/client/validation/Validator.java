package at.ac.tuwien.sepm.assignment.group02.client.validation;

import at.ac.tuwien.sepm.assignment.group02.client.entity.*;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.EmptyInputException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.NoValidIntegerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.lang.invoke.MethodHandles;

@Service
public class Validator {

    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public int[] temporaryAddTaskToLumberValidation(String id, String amount) throws InvalidInputException {
        int[] result = new int[2];
        int validatedId;
        try {
            validatedId = validateNumber(id);
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Id: " + e.getMessage());
            throw new InvalidInputException("Error at Id: " + e.getMessage());
        }
        result[0] = validatedId;
        int validatedAmount;
        try {
            validatedAmount = validateNumber(amount);
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Amount: " + e.getMessage());
            throw new InvalidInputException("Error at Amount: " + e.getMessage());
        }
        result[1] = validatedAmount;
        return result;
    }

    public void inputValidationOrder(Order toValidate) throws InvalidInputException {
        try {
            validateText(toValidate.getCustomerName());
        }catch(EmptyInputException e) {
            LOG.error("Error at Customer Name: " + e.getMessage());
            throw new InvalidInputException("Error at Customer Name: " + e.getMessage());
        }
        try {
            validateText(toValidate.getCustomerAddress());
        }catch(EmptyInputException e) {
            LOG.error("Error at Customer Address: " + e.getMessage());
            throw new InvalidInputException("Error at Customer Address: " + e.getMessage());
        }
        try {
            validateText(toValidate.getCustomerUID());
        }catch(EmptyInputException e) {
            LOG.error("Error at Customer UID: " + e.getMessage());
            throw new InvalidInputException("Error at Customer UID: " + e.getMessage());
        }
    }

    public Task inputValidationTask(UnvalidatedTask toValidate) throws InvalidInputException {
        try {
            validateText(toValidate.getDescription());
        }catch(EmptyInputException e) {
            LOG.error("Error at Description: " + e.getMessage());
            throw new InvalidInputException("Error at Description: " + e.getMessage());
        }
        try {
            validateText(toValidate.getFinishing());
        }catch(EmptyInputException e) {
            LOG.error("Error at Finishing: " + e.getMessage());
            throw new InvalidInputException("Error at Finishing: " + e.getMessage());
        }
        try {
            validateText(toValidate.getWood_type());
        }catch(EmptyInputException e) {
            LOG.error("Error at Wood Type: " + e.getMessage());
            throw new InvalidInputException("Error at Wood Type: " + e.getMessage());
        }
        try {
            validateText(toValidate.getQuality());
        }catch(EmptyInputException e) {
            LOG.error("Error at Quality: " + e.getMessage());
            throw new InvalidInputException("Error at Quality: " + e.getMessage());
        }
        int validatedSize;
        try {
            validatedSize = validateNumber(toValidate.getSize());
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Size: " + e.getMessage());
            throw new InvalidInputException("Error at Size: " + e.getMessage());
        }
        int validatedWidth;
        try {
            validatedWidth = validateNumber(toValidate.getWidth());
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Width: " + e.getMessage());
            throw new InvalidInputException("Error at Width: " + e.getMessage());
        }
        int validatedLength;
        try {
            validatedLength = validateNumber(toValidate.getLength());
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Length: " + e.getMessage());
            throw new InvalidInputException("Error at Length: " + e.getMessage());
        }
        int validatedQuantity;
        try {
            validatedQuantity = validateNumber(toValidate.getQuantity());
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Quantity: " + e.getMessage());
            throw new InvalidInputException("Error at Quantity: " + e.getMessage());
        }
        int validatedPrice;
        try {
            validatedPrice = validateNumber(toValidate.getPrice());
        }catch(NoValidIntegerException e) {
            LOG.error("Error at Price: " + e.getMessage());
            throw new InvalidInputException("Error at Price: " + e.getMessage());
        }
        Task validated = new Task(-1,-1,toValidate.getDescription(),toValidate.getFinishing(),toValidate.getWood_type(),toValidate.getQuality(),validatedSize,validatedWidth,validatedLength,validatedQuantity,0,false,validatedPrice);
        return validated;
    }

    private int validateNumber(String toValidate) throws NoValidIntegerException {
        int num = -1;
        if(toValidate.length() == 0 || toValidate == null) {
            throw new NoValidIntegerException("Empty Field, No Number entered");
        }
        try {
            num = Integer.parseInt(toValidate);
            if (num <= 0) {
                throw new NoValidIntegerException("Negative Integer or Null entered");
            }
        } catch (NumberFormatException e) {
            LOG.error("No valid Integer entered");
            throw new NoValidIntegerException("No valid Integer entered");
        }
        return num;
    }

    private void validateText(String toValidate) throws EmptyInputException {
        if(toValidate == null || toValidate.length() == 0) {
            throw new EmptyInputException("Empty Field");
        }
    }


    public Lumber validateLumber(UnvalidatedLumber filter) throws InvalidInputException{

        String description = filter.getDescription().trim();
        String finishing = filter.getFinishing().trim().equals("keine Angabe")? "" :
                filter.getFinishing().trim();
        String wood_type = filter.getWood_type().trim().equals("keine Angabe")? "" :
                filter.getWood_type().trim();
        String quality = filter.getQuality().trim().equals("keine Angabe")? "" :
                filter.getQuality().trim();
        String strength = filter.getSize().trim();
        String width = filter.getWidth().trim();
        String length = filter.getLength().trim();

        Lumber validatedLumber = new Lumber();

        if (!description.equals("")){
            validatedLumber.setDescription(description);
        }
        if (!finishing.equals("")){
            validatedLumber.setFinishing(finishing);
        }

        if (!wood_type.equals("")){
            validatedLumber.setWood_type(wood_type);
        }

        if (!quality.equals("")){
            validatedLumber.setQuality(quality);
        }

        if (!strength.equals("")){
            if (strength.matches("^\\d+$")) {
                if (strength.length() < 10) {
                    validatedLumber.setSize(Integer.parseInt(strength));
                }else{
                    throw new InvalidInputException("Die angegebene Stärke ist zu groß.");
                }
            }
            else{
                throw new InvalidInputException("Die angegebene Stärke ist keine positive ganze Zahl.");
            }
        }else{
            validatedLumber.setSize(-1);
        }

        if (!width.equals("")){
            if (width.matches("^\\d+$")) {
                if (width.length() < 10) {
                    validatedLumber.setWidth(Integer.parseInt(width));
                }else{
                    throw new InvalidInputException("Die angegebene Breite ist zu groß.");
                }
            }else{
                throw new InvalidInputException("Die angegebene Breite ist keine positive ganze Zahl.");
            }
        }else{
            validatedLumber.setWidth(-1);
        }

        if (!length.equals("")){
            if (length.matches("^\\d+$")) {
                if (length.length() < 10) {
                    validatedLumber.setLength(Integer.parseInt(length));
                }else{
                    throw new InvalidInputException("Die angegebene Länge ist zu groß.");
                }
            }else{
                throw new InvalidInputException("Die angegebene Länge ist keine positive ganze Zahl.");
            }
        }else{
            validatedLumber.setLength(-1);
        }

        return validatedLumber;

    }


}
