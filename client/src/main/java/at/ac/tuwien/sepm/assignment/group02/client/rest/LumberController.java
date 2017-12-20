package at.ac.tuwien.sepm.assignment.group02.client.rest;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.PersistenceLayerException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.FilterDTO;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.LumberDTO;

import java.util.List;

public interface LumberController {

    /**
     * 2.1.2 Eine tabellarische Übersicht des vorhandenen Schnittholz anzeigen.
     * 2.1.3 Suchfunktionalität implementieren
     * @return list of searched lumber
     * @param filter a LumberDTO object with the parameter to be searched
     * @throws PersistenceLayerException if the server is not available
     */
    List<LumberDTO> getAllLumber(LumberDTO filter) throws PersistenceLayerException;

    /**
     * 1.4.3 Schnittholz aus dem Schnittholzlager entfernen.
     */
    void removeLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * 2.2.2 & 3.2.5 Schnittholz als reserviert markieren.
     */
    void reserveLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * 2.2.2 Ausgewähltes Schnittholz als reserviert markieren.
     * @param lumberDTO
     * @throws PersistenceLayerException
     */
    public void updateLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * 3.2.4 Schnittholz ins Lager hinzufügen.
     */
    void createLumber(LumberDTO lumberDTO) throws PersistenceLayerException;

    /**
     * get lumber specified by id
     * @param id int id of lumber to get
     * @return LumberDTO specified by id
     */
    LumberDTO getLumberById(int id) throws PersistenceLayerException;

}
