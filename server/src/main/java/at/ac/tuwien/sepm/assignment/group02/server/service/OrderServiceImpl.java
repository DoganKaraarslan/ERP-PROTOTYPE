package at.ac.tuwien.sepm.assignment.group02.server.service;

import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.FilterDTO;
import at.ac.tuwien.sepm.assignment.group02.server.converter.OrderConverter;
import at.ac.tuwien.sepm.assignment.group02.server.converter.TaskConverter;
import at.ac.tuwien.sepm.assignment.group02.server.entity.Filter;
import at.ac.tuwien.sepm.assignment.group02.server.entity.Lumber;
import at.ac.tuwien.sepm.assignment.group02.server.entity.Order;
import at.ac.tuwien.sepm.assignment.group02.server.entity.Task;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.OrderDTO;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.TaskDTO;
import at.ac.tuwien.sepm.assignment.group02.server.exceptions.*;
import at.ac.tuwien.sepm.assignment.group02.server.persistence.LumberDAO;
import at.ac.tuwien.sepm.assignment.group02.server.persistence.OrderDAO;
import at.ac.tuwien.sepm.assignment.group02.server.persistence.TaskDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static OrderDAO orderManagementDAO;
    private static TaskDAO taskManagementDAO;
    private static LumberDAO lumberDAO;
    private static OrderConverter orderConverter;
    private static TaskConverter taskConverter;

    @Autowired
    public OrderServiceImpl(OrderDAO orderManagementDAO,TaskDAO taskManagementDAO, LumberDAO lumberDAO, OrderConverter orderConverter, TaskConverter taskConverter) {
        OrderServiceImpl.orderManagementDAO = orderManagementDAO;
        OrderServiceImpl.taskManagementDAO = taskManagementDAO;
        OrderServiceImpl.lumberDAO = lumberDAO;
        OrderServiceImpl.orderConverter = orderConverter;
        OrderServiceImpl.taskConverter = taskConverter;
    }

    @Override
    public void deleteOrder(OrderDTO orderDTO) throws ServiceLayerException {
        Order orderToDelete = orderConverter.convertRestDTOToPlainObject(orderDTO);
        try {
            orderManagementDAO.deleteOrder(orderToDelete);
        } catch (PersistenceLayerException e) {
            LOG.error("Error while deleting an order");
            throw new ServiceLayerException("Failed Persistenz");
        }
    }

    @Override
    public void createOrder(OrderDTO orderDTO) throws ServiceLayerException {
        List<TaskDTO> toConvert = orderDTO.getTaskList();
        List<Task> converted = new ArrayList<>();
        orderDTO.setTaskList(null);
        Order order = orderConverter.convertRestDTOToPlainObject(orderDTO);
        for(int i  = 0; i < toConvert.size();i++) {
            converted.add(taskConverter.convertRestDTOToPlainObject(toConvert.get(i)));
        }
        order.setTaskList(converted);
        try{
            orderManagementDAO.createOrder(order);
        } catch(PersistenceLayerException e) {
            LOG.error("Error while trying to create Object in Database");
            throw new EntityCreationException("Bestellung nicht erstellt.");
        }
    }

    @Override
    public List<OrderDTO> getAllOpen() throws ServiceLayerException {

        List<OrderDTO> allOpenConverted = new ArrayList<>();

        try{
            List<Order> allOpen = orderManagementDAO.getAllOpen();

            for (int i = 0; allOpen!=null && i < allOpen.size(); i++) {
                allOpenConverted.add(orderConverter.convertPlainObjectToRestDTO(allOpen.get(i)));
            }

            setTaskInfo(allOpenConverted);

        } catch(PersistenceLayerException e) {
            LOG.error("Error while trying to get objects from Database: " + e.getMessage());
            throw new ServiceLayerException(e.getMessage());
        }


        return allOpenConverted;
    }

    @Override
    public void updateOrder(OrderDTO orderDTO) {
        LOG.info("called updateOrder");

    }

    @Override
    public List<OrderDTO> getAllClosed() throws ServiceLayerException {
        List<OrderDTO> allClosedConverted =  new ArrayList<>();

        try{

            List<Order> allClosed = orderManagementDAO.getAllClosed();

            if (allClosed!= null) {

                for (int i = 0; i < allClosed.size(); i++) {
                    allClosedConverted.add(orderConverter.convertPlainObjectToRestDTO(allClosed.get(i)));
                }
                setTaskInfo(allClosedConverted);
            }


        } catch(PersistenceLayerException e) {
            LOG.error(e.getMessage());
            throw new ServiceLayerException(e.getMessage());
        }


        return allClosedConverted;
    }

    @Override
    public OrderDTO getOrderById(int order_id) throws ServiceLayerException {

        try {
            return orderConverter.convertPlainObjectToRestDTO(orderManagementDAO.getOrderById(order_id));
        } catch (PersistenceLayerException e) {
            LOG.error("Error while trying to get objects from Database: " + e.getMessage());
            throw new EntityNotFoundExceptionService(e.getMessage());
        }
    }

    @Override
    public void invoiceOrder(OrderDTO orderDTO) throws ServiceLayerException {
        Order order = orderConverter.convertRestDTOToPlainObject(orderDTO);

        List<TaskDTO> taskDTOList = orderDTO.getTaskList();

        List<Task> taskList = new ArrayList<>();

        for(TaskDTO taskDTO : taskDTOList){
            taskList.add(taskConverter.convertRestDTOToPlainObject(taskDTO));
        }

        order.setTaskList(taskList);
        //TODO delete lumber from database

        try {
            orderManagementDAO.invoiceOrder(order);
            for(Task task : order.getTaskList()){
                //delete lumber for this task
                deleteLumberForInvoicedTask(task);
            }
        } catch (PersistenceLayerException e) {
            LOG.error("Error while tying to invoice Order: " + e.getMessage());
            throw new ServiceLayerException("couldn't edit order");
        }
    }

    private void deleteLumberForInvoicedTask(Task task) throws PersistenceLayerException, ServiceLayerException {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setLength(task.getLength()+"");
        filterDTO.setSize(task.getSize()+"");
        filterDTO.setWidth(task.getWidth()+"");
        filterDTO.setDescription(task.getDescription());
        filterDTO.setFinishing(task.getFinishing());
        filterDTO.setQuality(task.getQuality());
        filterDTO.setWood_type(task.getWood_type());
        List<Lumber> compatibleLumber = lumberDAO.getAllLumber(filterDTO);
        int remainingQuantity = task.getQuantity()-task.getProduced_quantity();
        for(Lumber lumber : compatibleLumber){
            int availableLumber = lumber.getQuantity()-lumber.getReserved_quantity();
            remainingQuantity = remainingQuantity - availableLumber;
            if(remainingQuantity <= 0){
                lumberDAO.removeLumber(lumber.getId(), remainingQuantity);
                break;
            }
            else{
                lumberDAO.removeLumber(lumber.getId(), availableLumber);
            }
        }
        if(remainingQuantity > 0){
                throw new ServiceLayerException("Not enough Lumber for task {" + task.toString() + "} available");
        }
    }


    private void setTaskInfo(List<OrderDTO> orderDTOList) throws PersistenceLayerException {
        LOG.debug("called setTaskInfo");
        for (OrderDTO current : orderDTOList) {

            List<Task> tasks = null;
            try {
                tasks = taskManagementDAO.getTasksByOrderId(current.getID());
            }catch (Exception e){

            }
            List<TaskDTO> convertedTasks = new ArrayList<>();

            if (tasks != null) {
                for (Task task : tasks) {
                    convertedTasks.add(taskConverter.convertPlainObjectToRestDTO(task));
                }

                current.setTaskList(convertedTasks);
            }
            else{
                //order.remove(current);
            }

        }

    }
}
