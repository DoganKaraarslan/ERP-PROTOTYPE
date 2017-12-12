package at.ac.tuwien.sepm.assignment.group02.client.gui;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static at.ac.tuwien.sepm.assignment.group02.client.MainApplication.lumberService;

@Controller
public class LeadWorkerFXML {

    @FXML
    private TextField tf_taskAmount;

    @FXML
    private Label lead_label;

    @FXML
    private TextField tf_taskId;

    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public void testLeadController() {
        lead_label.setText("Success Lead Controller");
    }

    @FXML
    public void addLumberToTask(ActionEvent actionEvent) {
        try {
            lumberService.addReservedLumberToTask(tf_taskId.getText(),tf_taskAmount.getText());
        } catch(InvalidInputException e) {
            LOG.error("Failed to add Lumber to Task: " + e.getMessage());
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Failed to add Lumber");
            error.setHeaderText(null);
            error.setContentText("Failed to add Lumber to Task\nReason: " + e.getMessage());
            error.showAndWait();
        }

    }
}
