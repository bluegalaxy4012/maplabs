package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorMenuController implements Observer<PrietenieEntityChangeEvent> {
    private final ObservableList<Utilizator> model = FXCollections.observableArrayList();
    private Utilizator utilizator;
    private PrietenieService prietenieService;
    private UtilizatorService utilizatorService;
    @FXML
    private TableView<Utilizator> tableView;
    @FXML
    private TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    private TableColumn<Utilizator, String> tableColumnLastName;
    @FXML
    private TableColumn<Utilizator, String> tableColumnRequestStatus;

    public void setData(Utilizator utilizator, UtilizatorService utilizatorService, PrietenieService prietenieService) {
        this.utilizator = utilizator;
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        prietenieService.addObserver(this);
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
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> utilizatori = utilizatorService.getUtilizatori();
        List<Utilizator> utilizatoriList = StreamSupport.stream(utilizatori.spliterator(), false).filter(u -> !u.getId().equals(utilizator.getId())).collect(Collectors.toList());
        model.setAll(utilizatoriList);
    }

    public void handleAddPrietenie(ActionEvent actionEvent) {
        Utilizator utilizator2 = tableView.getSelectionModel().getSelectedItem();
        if (utilizator2 == null) {
            MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
            return;
        }
        try {
            prietenieService.addCererePrietenie(utilizator.getId(), utilizator2.getId());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleRemovePrietenie(ActionEvent actionEvent) {
        Utilizator utilizator2 = tableView.getSelectionModel().getSelectedItem();
        if (utilizator2 == null) {
            MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
            return;
        }
        try {
            prietenieService.removePrietenie(utilizator.getId(), utilizator2.getId());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }
}