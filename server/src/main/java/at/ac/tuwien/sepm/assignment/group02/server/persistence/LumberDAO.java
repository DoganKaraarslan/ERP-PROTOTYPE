package at.ac.tuwien.sepm.assignment.group02.server.persistence;

import at.ac.tuwien.sepm.assignment.group02.server.entity.Lumber;
import at.ac.tuwien.sepm.assignment.group02.server.exceptions.PersistenceLayerException;

import java.sql.SQLException;
import java.util.List;

public interface LumberDAO {

    /**
     * hello world
     */
    Lumber readLumberById(int id) throws PersistenceLayerException;

    /**
     * inserte a new lumber in lumber sotore
     * @param lumber
     * @throws PersistenceLayerException
     */
    void createLumber(Lumber lumber) throws PersistenceLayerException;

    /**
     * update an existing lumber
     * @param lumber
     * @throws PersistenceLayerException
     */
    void updateLumber(Lumber lumber) throws PersistenceLayerException;

    /**
     * delete lumber from the lumber store.
     * @param lumber
     * @throws PersistenceLayerException
     */
    void deleteLumber(Lumber lumber) throws PersistenceLayerException;

    /**
     * retrieve all lumber from data base
     * @param filter
     * @return a list of lumbers
     * @throws PersistenceLayerException
     */
    List<Lumber> getAllLumber(Lumber filter) throws PersistenceLayerException;

}
