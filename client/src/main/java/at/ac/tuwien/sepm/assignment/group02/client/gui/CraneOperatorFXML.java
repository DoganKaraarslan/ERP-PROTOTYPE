package at.ac.tuwien.sepm.assignment.group02.client.gui;

import at.ac.tuwien.sepm.assignment.group02.client.exceptions.ServiceLayerException;
import at.ac.tuwien.sepm.assignment.group02.client.service.AssignmentService;
import at.ac.tuwien.sepm.assignment.group02.client.util.AlertBuilder;
import at.ac.tuwien.sepm.assignment.group02.rest.restDTO.AssignmentDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CraneOperatorFXML {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private AssignmentService assignmentService;

    private AssignmentDTO currentAssignment;

    @Autowired
    public CraneOperatorFXML(AssignmentService assignmentService) {
        LOG.debug("called constructor CraneOperatorFXML");
        this.assignmentService = assignmentService;
    }

    @FXML
    private TableView<AssignmentDTO> table_open_assignment;

    @FXML
    private TableColumn col_open_assignmentNr;

    @FXML
    private TableColumn col_open_assignmentCreated;

    @FXML
    private TableColumn col_open_assignmentAmount;

    @FXML
    private TableColumn col_open_assignmentBoxID;

    @FXML
    private TableView<AssignmentDTO> table_done_assignment;

    @FXML
    private TableColumn col_done_assignmentNr;

    @FXML
    private TableColumn col_done_assignmentCreated;

    @FXML
    private TableColumn col_done_assignmentAmount;

    @FXML
    private TableColumn col_done_assignmentBoxID;

    @FXML
    private Label currentAssignment_amount;
    @FXML
    private Label currentAssignment_box;

    @FXML
    private Button btn_done;
    @FXML
    private Button btn_inProgress;
    @FXML
    private Button btn_inProgressAbort;

    @FXML
    public void initialize() {
        initializeAssignmentTable();
        updateAssignmentTable();
        btn_done.setVisible(false);
        btn_inProgressAbort.setVisible(false);
    }

    private void initializeAssignmentTable() {
        table_open_assignment.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        col_open_assignmentNr.setCellValueFactory(new PropertyValueFactory("id"));
        col_open_assignmentCreated.setCellValueFactory(new PropertyValueFactory("creation_date"));
        col_open_assignmentAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        col_open_assignmentBoxID.setCellValueFactory(new PropertyValueFactory("box_id"));

        ObservableList<AssignmentDTO> assignments = FXCollections.observableArrayList();
        table_open_assignment.setItems(assignments);

        col_done_assignmentNr.setCellValueFactory(new PropertyValueFactory("id"));
        col_done_assignmentCreated.setCellValueFactory(new PropertyValueFactory("creation_date"));
        col_done_assignmentAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        col_done_assignmentBoxID.setCellValueFactory(new PropertyValueFactory("box_id"));

        ObservableList<AssignmentDTO> assignments_done = FXCollections.observableArrayList();
        table_done_assignment.setItems(assignments_done);

        table_done_assignment.getSelectionModel().setSelectionMode(null);
        table_done_assignment.setDisable(true);

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                while(true){
                    if(isCancelled()) break;
                    Thread.sleep(5000);
                    int selected_index = table_open_assignment.getSelectionModel().getSelectedIndex();
                    updateAssignmentTable();
                    table_open_assignment.getSelectionModel().select(selected_index);
                }
                return 1;
            }
        };

        //start the auto-refresh task
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void updateAssignmentTable() {
        List<AssignmentDTO> allOpenAssignments = new LinkedList<>();
        List<AssignmentDTO> allDoneAssignments = new LinkedList<>();
        try {
            allOpenAssignments = assignmentService.getAllOpenAssignments();
            allDoneAssignments = assignmentService.getAllAssignments();
        } catch (ServiceLayerException e) {
            LOG.warn("error while updating assignment table for crane operator");
            AlertBuilder alertBuilder = new AlertBuilder();
            alertBuilder.showErrorAlert("Fehlermeldung", "Aufgaben-Service",
                    "Tabelle konnte nicht aktualisiert werden. "+ e.getMessage());
        }

        table_open_assignment.setItems(FXCollections.observableArrayList(allOpenAssignments));
        table_done_assignment.setItems(FXCollections.observableArrayList(allDoneAssignments));

        // set row factory in order to create the context menu and set row color
        table_open_assignment.setRowFactory(
                new Callback<TableView<AssignmentDTO>, TableRow<AssignmentDTO>>() {
                    @Override
                    public TableRow<AssignmentDTO> call(TableView<AssignmentDTO> table_open_assignment) {

                        final TableRow<AssignmentDTO> row = new TableRow<>() {
                            @Override
                            protected void updateItem(AssignmentDTO assignmentDTO, boolean empty){
                                super.updateItem(assignmentDTO, empty);

                                if (assignmentDTO == null) {
                                    setStyle("");
                                } else {
                                    setStyle("-fx-background-color: lightgray;");
                                }

                                if(isSelected()){
                                    setStyle("-fx-background-color: blue;");
                                }

                            }
                        };
                        return row;
                    }
                });

        // set row factory in order to create context menu and set row color
        table_done_assignment.setRowFactory(
                new Callback<TableView<AssignmentDTO>, TableRow<AssignmentDTO>>() {
                    @Override
                    public TableRow<AssignmentDTO> call(TableView<AssignmentDTO> table_open_assignment) {

                        final TableRow<AssignmentDTO> row = new TableRow<>() {
                            @Override
                            protected void updateItem(AssignmentDTO assignmentDTO, boolean empty){
                                super.updateItem(assignmentDTO, empty);

                                if (assignmentDTO == null) {
                                    setStyle("");
                                } else if (assignmentDTO.isDone()) {
                                    setStyle("-fx-background-color: lightgreen;");
                                } else {
                                    setStyle("-fx-background-color: lightgray;");
                                }

                            }
                        };
                        return row;
                    }
                });

        table_open_assignment.refresh();
        table_done_assignment.refresh();
    }

    @FXML
    public void setInProgressButtonPressed() {
        LOG.info("setInProgressButtonPressed button pressed");
        AssignmentDTO assignmentDTO = this.currentAssignment;

        if (assignmentDTO == null){
            if (table_open_assignment.getSelectionModel().getSelectedItem() == null) {
                AlertBuilder alertBuilder = new AlertBuilder();
                alertBuilder.showInformationAlert("Information", "Aufgabe ", "Bitte wählen Sie eine Aufgabe aus!");
                return;
            } else {
                assignmentDTO = table_open_assignment.getSelectionModel().getSelectedItem();
            }
        } else {
            return;
        }

        this.currentAssignment = assignmentDTO;

        currentAssignment_amount.setText("Anzahl: "+this.currentAssignment.getAmount()+"");
        currentAssignment_box.setText("Box Nr: "+this.currentAssignment.getBox_id());
        table_open_assignment.getSelectionModel().clearSelection();
        btn_done.setVisible(true);
        btn_inProgress.setVisible(false);
        btn_inProgressAbort.setVisible(true);
    }

    @FXML
    public void abortInProgressButtonPressed(){
        this.currentAssignment = null;
        btn_done.setVisible(false);
        btn_inProgress.setVisible(true);
        btn_inProgressAbort.setVisible(false);
        currentAssignment_amount.setText("");
        currentAssignment_box.setText("");
    }

    @FXML
    public void setDoneButtonPressed() {
        LOG.info("setDone button pressed");
        AssignmentDTO assignmentDTO = this.currentAssignment;

        // get the selected assignmentDTO from the table
        if (assignmentDTO == null) {
            AlertBuilder alertBuilder = new AlertBuilder();
            alertBuilder.showInformationAlert("Information", "Aufgabe abschließen", "Keine Aufgabe in Arbeit!");
            return;
        }

        setDone(assignmentDTO);
        this.currentAssignment = null;
    }

    private void setDone(AssignmentDTO assignmentDTO){

        if(assignmentDTO.isDone()){
            AlertBuilder alertBuilder = new AlertBuilder();
            alertBuilder.showInformationAlert("Information", "Aufgabe abschließen", "Diese Aufgabe ist bereits abgeschlossen.");
            return;
        }

        AlertBuilder alertBuilder = new AlertBuilder();
        boolean confirmed = alertBuilder.showConfirmationAlert("Aufgabe abschließen", null, "Möchten Sie die Aufgabe wirklich abschließen?");
        if(confirmed) {

            // create a thread and task to prevent ui from freezing
            Task task = new Task<Integer>() {

                @Override
                protected Integer call() throws ServiceLayerException {
                    LOG.debug("setDone thread called");
                    assignmentService.setDone(assignmentDTO);
                    return 1;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    LOG.debug("set done succeeded with value {}", getValue());
                    AlertBuilder alertBuilder = new AlertBuilder();
                    alertBuilder.showInformationAlert("Information", "Aufgabe abgeschlossen", "Aufgabe " + assignmentDTO.getId() + " wurde als erledigt markiert.");
                    updateAssignmentTable();
                    btn_done.setVisible(false);
                    btn_inProgress.setVisible(true);
                    btn_inProgressAbort.setVisible(false);
                    currentAssignment_amount.setText("");
                    currentAssignment_box.setText("");
                }

            };

            task.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
                if(newValue != null) {
                    ServiceLayerException e = (ServiceLayerException) newValue;
                    LOG.warn(e.getMessage().trim());
                    alertBuilder.showErrorAlert("Fehlermeldung", "Aufgaben-Service", "Die Aufgabe konnte nicht als erledigt markiert werden. "+ e.getMessage());
                }
            });

            Thread t = new Thread(task,"setDone");
            t.start();
        }
    }

}
