package at.ac.tuwien.sepm.assignment.group02.client.gui;

import at.ac.tuwien.sepm.assignment.group02.client.entity.Order;
import at.ac.tuwien.sepm.assignment.group02.client.entity.Task;
import at.ac.tuwien.sepm.assignment.group02.client.entity.Timber;
import at.ac.tuwien.sepm.assignment.group02.client.entity.UnvalidatedTask;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.InvalidInputException;
import at.ac.tuwien.sepm.assignment.group02.client.exceptions.ServiceLayerException;
import at.ac.tuwien.sepm.assignment.group02.client.service.CostBenefitService;
import at.ac.tuwien.sepm.assignment.group02.client.service.OrderService;
import at.ac.tuwien.sepm.assignment.group02.client.service.TaskService;
import at.ac.tuwien.sepm.assignment.group02.client.service.TimberService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class OfficeFXML {
    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Order currentOrder = new Order();
    private static List<Task> currentOrderTaskList = new ArrayList<>();
    private static int currentOrderIndex = 1;
    private static int currentOrderSum = 0;

    @FXML
    private Button bt_deleteOrder;

    @FXML
    private Button bt_acceptOrder;

    @FXML
    private Label l_sumorders;

    @FXML
    private Label kn_result;

    @FXML
    private TableColumn col_taskLength;

    @FXML
    private TableColumn col_taskWidth;

    @FXML
    private TableColumn col_taskSize;

    @FXML
    private TableColumn col_taskQuantity;

    @FXML
    private TableColumn col_taskDescription;

    @FXML
    private TableColumn col_taskNr;

    @FXML
    private TableView table_addedTask;

    @FXML
    private TextField tf_order_customerUID;

    @FXML
    private TextField tf_order_customeraddress;

    @FXML
    private TextField tf_order_customername;

    @FXML
    private Tab tab_timber;

    @FXML
    private Tab tab_order;

    @FXML
    private TextField tf_timber_amount;

    @FXML
    private ChoiceBox cb_timber_box;

    @FXML
    private TableColumn col_orderID;

    @FXML
    private TableColumn col_costumerName;

    @FXML
    private TableColumn col_taskAmount;

    @FXML
    private TableColumn col_amount;

    @FXML
    private TableColumn col_grossSum;


    @FXML
    private TableColumn col_billID;

    @FXML
    private TableColumn col_billCostumerName;

    @FXML
    private TableColumn col_billTaskAmount;

    @FXML
    private TableColumn col_billAmount;

    @FXML
    private TableColumn col_billGrossSum;

    @FXML
    TableView<Order> table_openOrder;

    @FXML
    TableView<Order> table_bill;

    private OrderService orderService;
    private TimberService timberService;
    private TaskService taskService;
    private CostBenefitService costBenefitService;
    private AlertBuilder alertBuilder = new AlertBuilder();

    @Autowired
    public OfficeFXML(OrderService orderService, TimberService timberService, TaskService taskService, CostBenefitService costBenefitService){
        this.orderService = orderService;
        this.timberService = timberService;
        this.taskService = taskService;
        this.costBenefitService = costBenefitService;
    }

    @FXML
    void initialize() {
        table_openOrder.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        col_orderID.setCellValueFactory(new PropertyValueFactory("ID"));
        col_costumerName.setCellValueFactory(new PropertyValueFactory("customerName"));
        col_taskAmount.setCellValueFactory(new PropertyValueFactory("taskAmount"));
        col_amount.setCellValueFactory(new PropertyValueFactory("quantity"));
        col_grossSum.setCellValueFactory(new PropertyValueFactory("grossAmount"));


        table_bill.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        col_billID.setCellValueFactory(new PropertyValueFactory("ID"));
        col_billCostumerName.setCellValueFactory(new PropertyValueFactory("customerName"));
        col_billTaskAmount.setCellValueFactory(new PropertyValueFactory("taskAmount"));
        col_billAmount.setCellValueFactory(new PropertyValueFactory("quantity"));
        col_billGrossSum.setCellValueFactory(new PropertyValueFactory("grossAmount"));




        col_taskNr.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("id")
        );
        col_taskDescription.setCellValueFactory(
                new PropertyValueFactory<Task, String>("description")
        );
        col_taskQuantity.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("quantity")
        );
        col_taskSize.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("size")
        );
        col_taskWidth.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("width")
        );
        col_taskLength.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("length")
        );

        ObservableList<Task> orderTasks = FXCollections.observableArrayList();
        table_addedTask.setItems(orderTasks);
        l_sumorders.setText(currentOrderSum + " €");
        initTimberTab();
        updateTable();
        updateBillTable();
    }

    @FXML
    public void deleteOrder() {
        LOG.trace("called deleteOrder");

        Order order = new Order();

        if(table_openOrder.getSelectionModel().getSelectedItem() != null) {
            AlertBuilder confirmDeletion = new AlertBuilder();
            boolean confirmed = confirmDeletion.showConfirmationAlert("Bestellung löschen", null, "Möchten Sie die Bestellung wirklich löschen?");

            if(confirmed) {
                order.setID(table_openOrder.getSelectionModel().getSelectedItem().getID());

                Task task = new Task();
                task.setOrder_id(order.getID());

                try {
                    orderService.deleteOrder(order);
                    taskService.deleteTask(task);
                } catch (InvalidInputException e) {
                    //InvalidInputException is never thrown
                    //the only user input is to select an order
                    //LOG.warn(e.getMessage());
                } catch (ServiceLayerException e) {
                    LOG.warn(e.getMessage());
                }
            }
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("Deletion failed");
            noSelection.setHeaderText(null);
            noSelection.setContentText("You have to choose an order to delete it!");
            noSelection.showAndWait();
        }

        updateTable();
    }

    @FXML
    public void createOrder(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sind sie sich sicher?");
        alert.setHeaderText(null);
        alert.setContentText("Wollen sie diese Bestellung wirklich erstellen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        LOG.info("createOrder called");
        if(currentOrderTaskList.size() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("No Tasks added");
            error.setHeaderText(null);
            error.setContentText("There is not Task added to the Order");
            error.showAndWait();
            return;
        }
        currentOrder.setCustomerName(tf_order_customername.getText());
        currentOrder.setCustomerAddress(tf_order_customeraddress.getText());
        currentOrder.setCustomerUID(tf_order_customerUID.getText());
        LOG.debug(currentOrder.toString());
        try {
            orderService.addOrder(currentOrder,currentOrderTaskList);
            clearCurrentOrder(null);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Creation successful");
            success.setHeaderText(null);
            success.setContentText("Order created successfully!");
            success.showAndWait();
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Creation failed");
            error.setHeaderText(null);
            error.setContentText("Order Creation failed!");
            error.showAndWait();
        } catch (ServiceLayerException e) {
            LOG.warn(e.getMessage());
        }

        updateTable();
    }


    private void updateTable() {

        List<Order> allOpen = null;
        try {
            allOpen = orderService.getAllOpen();
        } catch (ServiceLayerException e) {
            LOG.warn(e.getMessage());
        }

        if (allOpen != null) {
            ObservableList<Order> openOrderForTable = FXCollections.observableArrayList();

            for (Order order: allOpen) {
                openOrderForTable.add(order);

            }

            table_openOrder.setItems(openOrderForTable);
            table_openOrder.refresh();
        } else {
            table_openOrder.refresh();
        }

    }


    private void updateBillTable() {

        List<Order> allClosed = null;
        ObservableList<Order> closedOrderForTable;

        try {
            allClosed = orderService.getAllClosed();
        } catch (ServiceLayerException e) {
            LOG.warn(e.getMessage());
        }

        if (allClosed != null) {

            closedOrderForTable = FXCollections.observableArrayList();

            for (Order bill: allClosed) {
                closedOrderForTable.add(bill);
            }

            table_bill.setItems(closedOrderForTable);
            table_bill.refresh();
        } else {
            table_bill.refresh();
        }

    }


    @FXML
    public void addTimber(ActionEvent actionEvent){

        if(tf_timber_amount.getText().isEmpty() || cb_timber_box.getSelectionModel().getSelectedIndex()==-1){
            LOG.error("No Input");
            alertBuilder.showErrorAlert("Fehler bei Eingabe", "Eigabe unvollständig.", "Bitte nur gültige Eingaben benützen!");
        }
        else{
            Timber timber = new Timber(cb_timber_box.getSelectionModel().getSelectedIndex()+1, Integer.parseInt(tf_timber_amount.getText()));
            try {
                boolean addTimberConfirmation = alertBuilder.showConfirmationAlert("Rundholz hinzufügen", "Wollen Sie " +
                        timber.getAmount() + " Stück Rundholz zu Box " + timber.getBox_id() + " hinzufügen?", "");
                if(addTimberConfirmation){
                    timberService.addTimber(timber);
                    tf_timber_amount.setText("");
                    cb_timber_box.getSelectionModel().clearSelection();
                }
            } catch (InvalidInputException e) {
                LOG.error("Invalid Input Error: " + e.getMessage());
            } catch (ServiceLayerException e) {
                LOG.warn(e.getMessage());
            }
        }
    }

    //initialize the timberTab
    public void initTimberTab() {
        ObservableList<String> boxes = FXCollections.observableArrayList();
        int numberOfBoxes = 0;
        try {
            numberOfBoxes = timberService.getNumberOfBoxes();
        } catch (ServiceLayerException e) {
            LOG.warn(e.getMessage());
        }
        for(int i=0; i<numberOfBoxes; i++){
            boxes.add("Box " + (i+1));
        }

        cb_timber_box.setItems(boxes);
        // force the field to be numeric only
        tf_timber_amount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf_timber_amount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void addTaskToOrder(ActionEvent actionEvent) {
        Dialog<UnvalidatedTask> dialog = new Dialog<>();
        dialog.setTitle("Create Task");
        dialog.setHeaderText(null);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        GridPane gridPane =  new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(10);

        Label l_description =  new Label("Beschreibung:");
        TextField description = new TextField("");
        gridPane.add(l_description,0,0,2,1);
        gridPane.add(description,2,0);

        Label l_finishing =  new Label("Ausführung:");
        ComboBox<String> finishing =  new ComboBox<>();
        finishing.setItems(FXCollections.observableArrayList( "roh", "gehobelt", "besäumt", "prismiert", "trocken","lutro","frisch", "imprägniert"));
        finishing.setMaxWidth(200);
        finishing.getSelectionModel().selectFirst();
        finishing.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        getListView().setMaxWidth(200);
                        if (!empty) {
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        gridPane.add(l_finishing,0,1,2,1);
        gridPane.add(finishing,2,1);

        ComboBox<String> wood_type =  new ComboBox<>();
        wood_type.setItems(FXCollections.observableArrayList(  "Fi", "Ta", "Lä", "Ki", "Zi"));
        wood_type.setMaxWidth(200);
        wood_type.getSelectionModel().selectFirst();
        wood_type.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        getListView().setMaxWidth(200);
                        if (!empty) {
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        Label l_wood_type =  new Label("Holzart:");
        gridPane.add(l_wood_type,0,2,2,1);
        gridPane.add(wood_type,2,2);

        ComboBox<String> quality =  new ComboBox<>();
        quality.setItems(FXCollections.observableArrayList(  "O","I","II","III","IV","V", "O/III", "III/IV", "III/V"));
        quality.setMaxWidth(200);
        quality.getSelectionModel().selectFirst();
        quality.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        getListView().setMaxWidth(200);
                        if (!empty) {
                            setText(item);
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        Label l_quality =  new Label("Qualität:");
        gridPane.add(l_quality,0,3,2,1);
        gridPane.add(quality,2,3);

        TextField size = new TextField("");
        size.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    size.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Label l_size =  new Label("Dicke:");
        gridPane.add(l_size,0,4,2,1);
        gridPane.add(size,2,4);

        TextField width = new TextField("");
        width.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    width.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Label l_width =  new Label("Breite:");
        gridPane.add(l_width,0,5,2,1);
        gridPane.add(width,2,5);

        TextField length = new TextField("");
        length.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    length.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Label l_length =  new Label("Länge:");
        gridPane.add(l_length,0,6,2,1);
        gridPane.add(length,2,6);

        TextField quantity = new TextField("");
        quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    quantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Label l_quantity =  new Label("Menge:");
        gridPane.add(l_quantity,0,7,2,1);
        gridPane.add(quantity,2,7);

        TextField cost = new TextField("");
        cost.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    cost.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Label l_cost =  new Label("Preis:");
        gridPane.add(l_cost,0,8,2,1);
        gridPane.add(cost,2,8);

        dialogPane.setContent(gridPane);
        //dialogPane.setContent(new VBox(8,hb_description,hb_finishing,hb_wood_type,hb_quality,hb_size,hb_width,hb_length,hb_quantity,hb_cost));
        Platform.runLater(description::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new UnvalidatedTask(description.getText(),finishing.getSelectionModel().getSelectedItem(),wood_type.getSelectionModel().getSelectedItem(),quality.getSelectionModel().getSelectedItem(),size.getText(),width.getText(),length.getText(),quantity.getText(),cost.getText());
            }
            return null;
        });
        Optional<UnvalidatedTask> unvalidatedResult = dialog.showAndWait();
        unvalidatedResult.ifPresent((UnvalidatedTask results) -> {
            try {
                Task toAdd = taskService.validateTaskInput(results);
                toAdd.setId(currentOrderIndex);
                currentOrderIndex++;
                currentOrderTaskList.add(toAdd);
                table_addedTask.getItems().add(toAdd);
                currentOrderSum += toAdd.getPrice();
                l_sumorders.setText(currentOrderSum + " €");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully created Task");
                alert.setHeaderText(null);
                alert.setContentText("Task successfully created and added to your Order");
                alert.showAndWait();
            } catch(InvalidInputException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error creating Task");
                alert.setHeaderText(null);
                alert.setContentText("Error creating Task!\nReason: " + e.getMessage());
                alert.showAndWait();
            }
        });

    }

    public void invoiceBtnClicked(ActionEvent actionEvent) {
        LOG.info("invoice Button clicked");

        //get the selected order
        Order selectedOrder = table_openOrder.getSelectionModel().getSelectedItem();

        //TODO remove
        selectedOrder.setCustomerAddress("fas");
        selectedOrder.setCustomerName("asdf");
        selectedOrder.setCustomerUID("adsf");
        selectedOrder.setOrderDate(new Date());
        List<Task> testTaskList = new ArrayList<>();
        Task testTask = new Task();
        testTask.setPrice(2400);
        testTaskList.add(testTask);
        selectedOrder.setTaskList(testTaskList);

        try {
            orderService.invoiceOrder(selectedOrder);
            updateBillTable();
        } catch (InvalidInputException e) {
            //TODO use alertBuilder
            alertBuilder.showErrorAlert("Fehler beim Abrechnen", "Bestellung konnte nicht erfolgreich abgerechnet werden!", e.getMessage());
        } catch (ServiceLayerException e) {
            alertBuilder.showErrorAlert("Fehler beim Abrechnen!", "", "");
        }

        updateTable();
        updateBillTable();

    }

    public void clearCurrentOrder(ActionEvent actionEvent) {
        if(actionEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sind sie sich sicher?");
            alert.setHeaderText(null);
            alert.setContentText("Wollen sie diese Bestellung wirklich verwerfen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }
        }
        ObservableList<Task> orderTasks = FXCollections.observableArrayList();
        table_addedTask.setItems(orderTasks);
        currentOrder = new Order();
        currentOrderTaskList = new ArrayList<>();
        tf_order_customername.setText("");
        tf_order_customeraddress.setText("");
        tf_order_customerUID.setText("");
        currentOrderSum = 0;
        l_sumorders.setText(currentOrderSum + " €");
        kn_result.setText("");
    }

    public void initiateCostValueFunction(ActionEvent actionEvent) {
        if(currentOrderTaskList.size() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("No Tasks to evaluate");
            error.setHeaderText(null);
            error.setContentText("There is no Task added to the Order!\nPlease add Tasks to proceed to Cost/Benefit Function");
            error.showAndWait();
            return;
        }
        int randomized = costBenefitService.costValueFunctionStub(currentOrderSum);
        if(randomized < 0) {
            kn_result.setTextFill(Color.web("#dd0000"));
            kn_result.setText(randomized+"");
        } else {
            kn_result.setTextFill(Color.web("#00dd00"));
            kn_result.setText(randomized+"");
        }
    }
}
