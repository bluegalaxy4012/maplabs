package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.MesajService;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorMenuController implements Observer<PrietenieEntityChangeEvent> {
    private final ObservableList<Utilizator> model = FXCollections.observableArrayList();
    private Utilizator utilizator;
    private PrietenieService prietenieService;
    private UtilizatorService utilizatorService;
    private MesajService mesajService;
    @FXML
    private TableView<Utilizator> tableView;
    @FXML
    private TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    private TableColumn<Utilizator, String> tableColumnLastName;
    @FXML
    private TableColumn<Utilizator, String> tableColumnRequestStatus;

    public void setData(Utilizator utilizator, UtilizatorService utilizatorService, PrietenieService prietenieService, MesajService mesajService, Stage dialog) {
        this.utilizator = utilizator;
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.mesajService = mesajService;
        prietenieService.addObserver(this);
        dialog.setOnCloseRequest(event -> prietenieService.removeObserver(this));
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnRequestStatus.setCellValueFactory(cellData -> {
            Utilizator utilizator2 = cellData.getValue();
            String status = prietenieService.getStatus(utilizator.getId(), utilizator2.getId());
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> utilizatori = utilizatorService.getUtilizatori();
        List<Utilizator> utilizatoriList = StreamSupport.stream(utilizatori.spliterator(), false).filter(u -> !u.getId().equals(utilizator.getId())).collect(Collectors.toList());
        model.setAll(utilizatoriList);
    }

    public void handleAddPrietenie(ActionEvent actionEvent) {
        //doar daca e un singur utilizator selectat
        if (tableView.getSelectionModel().getSelectedItems().size() != 1) {
            MessageAlert.showErrorMessage(null, "Selecteaza un singur utilizator");
            return;
        }

        Utilizator utilizator2 = tableView.getSelectionModel().getSelectedItem();

        try {
            prietenieService.addCererePrietenie(utilizator.getId(), utilizator2.getId());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleRemovePrietenie(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedItems().size() != 1) {
            MessageAlert.showErrorMessage(null, "Selecteaza un singur utilizator");
            return;
        }

        Utilizator utilizator2 = tableView.getSelectionModel().getSelectedItem();

        try {
            prietenieService.removePrietenie(utilizator.getId(), utilizator2.getId());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleChatWith(ActionEvent actionEvent) {
        ObservableList<Utilizator> selectedUtilizatori = tableView.getSelectionModel().getSelectedItems();
        if (selectedUtilizatori.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
            return;
        }
        try {
            showChatDialog(selectedUtilizatori);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void showChatDialog(ObservableList<Utilizator> selectedUtilizatori) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/chat-view.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            String selectedUtilizatoriString = selectedUtilizatori.stream().map(Utilizator::getFirstName).collect(Collectors.joining(", "));
            dialogStage.setTitle(utilizator.getFirstName() + " " + utilizator.getLastName() + ". Chat cu " + selectedUtilizatoriString);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            ChatController chatController = loader.getController();
            chatController.setData(utilizator, selectedUtilizatori, mesajService, dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {

        if (prietenieEntityChangeEvent.getType().equals(ChangeEventType.ADD) && prietenieEntityChangeEvent.getData().getId().getE2().equals(utilizator.getId())) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Notificare pentru " + utilizator.getFirstName() + " " + utilizator.getLastName(), "Ai primit o cerere de prietenie");
        }

        initModel();
    }
}