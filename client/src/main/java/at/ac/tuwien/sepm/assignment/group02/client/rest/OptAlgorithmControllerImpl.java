package at.ac.tuwien.sepm.assignment.group02.client.rest;

import at.ac.tuwien.sepm.assignment.group02.client.configuration.RestTemplateConfiguration;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.PersistenceLayerException;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.OptAlgorithmResultDTO;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

@RestController
public class OptAlgorithmControllerImpl implements OptAlgorithmController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private RestTemplate restTemplate;

    @Autowired
    public OptAlgorithmControllerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OptAlgorithmResultDTO getOptAlgorithmResult(@RequestBody TaskDTO taskDTO) throws PersistenceLayerException {
        LOG.debug("sending selected task to the server to execute the optimisation algorithm");

        try {
            //return restTemplate.getForObject("http://"+RestTemplateConfiguration.host+":"+RestTemplateConfiguration.port+"/getOptAlgorithmResult", OptAlgorithmResultDTO.class);
            return restTemplate.postForObject("http://"+RestTemplateConfiguration.host+":"+ RestTemplateConfiguration.port+"/getOptAlgorithmResult", taskDTO, OptAlgorithmResultDTO.class);
        } catch(HttpStatusCodeException e) {
            LOG.warn("HttpStatusCodeException {}", e.getResponseBodyAsString());
            throw new PersistenceLayerException("Connection Problem with Server");
        } catch(RestClientException e){
            //no response payload, probably server not running
            LOG.warn("server is down? - {}", e.getMessage());
            throw new PersistenceLayerException("Connection Problem with Server");
        }
    }
}