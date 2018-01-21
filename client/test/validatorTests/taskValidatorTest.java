package validatorTests;

import at.ac.tuwien.sepm.assignment.group02.client.entity.Task;
import at.ac.tuwien.sepm.assignment.group02.client.entity.UnvalidatedTask;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import at.ac.tuwien.sepm.assignment.group02.client.validation.PrimitiveValidator;
import at.ac.tuwien.sepm.assignment.group02.client.validation.Validator;
import io.swagger.models.auth.In;
import org.junit.*;

public class taskValidatorTest {

    private static Validator validator;
    private static UnvalidatedTask task;

    @BeforeClass
    public static void setUp() {
        PrimitiveValidator primitiveValidator = new PrimitiveValidator();
        validator = new Validator(primitiveValidator);
    }


    @Test(expected = InvalidInputException.class)
    public void testEmptyTaskFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask(null,null,null,null,null,null,null,null,null);
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoDescriptionFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("","b","c","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskTooLongDescriptionFailedValidation() throws InvalidInputException {
        String tooLong = "";
        for(int i = 0; i < 55;i++)
            tooLong += "a";
        task = new UnvalidatedTask(tooLong,"b","c","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskWrongFinishingFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","","c","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskTooLongFinishingFailedValidation() throws InvalidInputException {
        String tooLong = "";
        for(int i = 0; i < 20;i++)
            tooLong += "a";
        task = new UnvalidatedTask("a",tooLong,"c","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoWoodTypeFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskWrongWoodTypeFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","abc","d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskTooLongWoodTypeFailedValidation() throws InvalidInputException {
        String tooLong = "";
        for(int i = 0; i < 15;i++)
            tooLong += "a";
        task = new UnvalidatedTask("a","roh",tooLong,"d","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskWrongQualityFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","","1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskTooLongQualityFailedValidation() throws InvalidInputException {
        String tooLong = "";
        for(int i = 0; i < 15;i++)
            tooLong += "a";
        task = new UnvalidatedTask("a","roh","Fi",tooLong,"1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoValidIntegerSizeFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","arohc","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNegativeIntegerSizeFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","-1","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoSizeFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","","2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoValidIntegerWidthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","abc","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNegativeIntegerWidthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","-2","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoWidthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","","3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoValidIntegerLengthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","abc","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNegativeIntegerLengthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","-3","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoLengthFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoValidIntegerQuantityFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","abc","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNegativeIntegerQuantityFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","-4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoQuantityFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoValidIntegerPriceFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","4","abc");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNegativeIntegerPriceFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","4","-5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testTaskNoPriceFailedValidation() throws InvalidInputException{
        task = new UnvalidatedTask("a","roh","Fi","I","1","2","4000","4","");
        validator.inputValidationTask(task);
    }


    @Test
    public void testCorrectTask() {
        boolean success = true;
        task = new UnvalidatedTask("Latten","roh","Fi","I","1","2","3500","4","5");
        Task temp = null;
        try {
            temp = validator.inputValidationTask(task);
        } catch(InvalidInputException e) {
            success = false;
        }
        if(temp == null) {
            success = false;
        }
        Assert.assertEquals(success,true);
        Assert.assertEquals(temp.getDescription(),"Latten");
        Assert.assertEquals(temp.getFinishing(),"roh");
        Assert.assertEquals(temp.getWood_type(),"Fi");
        Assert.assertEquals(temp.getQuality(),"I");
        Assert.assertEquals(temp.getSize(),1);
        Assert.assertEquals(temp.getWidth(),2);
        Assert.assertEquals(temp.getLength(),3500);
        Assert.assertEquals(temp.getQuantity(),4);
        Assert.assertEquals(temp.getPrice(),5);

    }

    @Test(expected = InvalidInputException.class)
    public void testFailedWrongLength() throws InvalidInputException{
        task = new UnvalidatedTask("Latten","roh","Fi","I","1","2","3000","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testFailedWrongQuality() throws InvalidInputException {
        task = new UnvalidatedTask("Latten","roh","Fi","VII","1","2","3500","4","5");
        validator.inputValidationTask(task);
    }

    @Test(expected = InvalidInputException.class)
    public void testFailedWrongFinishing() throws InvalidInputException{
        task = new UnvalidatedTask("Latten","rohkant","Fi","I","1","2","3500","4","5");
        validator.inputValidationTask(task);
    }

    @After
    public void afterMethod() {
        task = null;
    }

    @AfterClass
    public static void tearDown() {
        validator = null;
    }
}
