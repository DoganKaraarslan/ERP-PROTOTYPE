package at.ac.tuwien.sepm.assignment.group02.client.rest;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.PersistenceLayerException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.FilterDTO;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.LumberDTO;

import java.util.List;

public interface LumberController {

    /**
     * show an overview of all availables lumbers
     * @param filter
     * @return a list of all Lumbers
     * @throws PersistenceLayerException
     */
    List<LumberDTO> getAllLumber(LumberDTO filter) throws PersistenceLayerException;

    /**
     * remove lumber from the stock
     * @param lumberDTO
     * @throws PersistenceLayerException
     */
    void removeLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * marking a select lumber als reserved.
     * @param lumberDTO
     * @throws PersistenceLayerException
     */
    void reserveLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * update a lumber and marking it  as reserved
     * @param lumberDTO
     * @throws PersistenceLayerException
     */
    public void updateLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * create a new lumber.
     * @param lumberDTO
     * @throws PersistenceLayerException
     */
    void createLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * get lumber specified by id
     * @param id int id of lumber to get
     * @return LumberDTO specified by id
     * @throws PersistenceLayerException
     */
    LumberDTO getLumberById(int id) throws PersistenceLayerException;

}
