package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorController implements Observer<UtilizatorEntityChangeEvent> {
    private final ObservableList<Utilizator> model = FXCollections.observableArrayList();
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    @FXML
    private TableView<Utilizator> tableView;
    @FXML
    private TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    private TableColumn<Utilizator, String> tableColumnLastName;
    @FXML
    private TextField firstNameFilter;
    @FXML
    private TextField lastNameFilter;

    public void setServices(UtilizatorService utilizatorService, PrietenieService prietenieService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        utilizatorService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);

        firstNameFilter.textProperty().addListener((observable, oldValue, newValue) -> initModel());
        lastNameFilter.textProperty().addListener((observable, oldValue, newValue) -> initModel());
    }


    private void initModel() {
        Iterable<Utilizator> utilizatori = utilizatorService.getUtilizatori();
        List<Utilizator> filteredList = StreamSupport.stream(utilizatori.spliterator(), false).filter(utilizator -> filterByFirstName(utilizator) && filterByLastName(utilizator)).collect(Collectors.toList());
        model.setAll(filteredList);
    }

    @Override
    public void update(UtilizatorEntityChangeEvent event) {
        initModel();
    }

    public void handleSelectUtilizator(ActionEvent actionEvent) {
        Utilizator utilizator = tableView.getSelectionModel().getSelectedItem();
        if (utilizator == null) MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
        else {
            showUtilizatorMenuDialog(utilizator);
        }
    }

    public void handleRemoveUtilizator(ActionEvent actionEvent) {
        Utilizator utilizator = tableView.getSelectionModel().getSelectedItem();
        if (utilizator != null) {
            utilizatorService.removeUtilizator(utilizator.getId());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Stergere utilizator", "Utilizatorul a fost sters");
        } else {
            MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
        }
    }

    public void handleUpdateUtilizator(ActionEvent actionEvent) {
        Utilizator utilizator = tableView.getSelectionModel().getSelectedItem();
        if (utilizator != null) {
            showMessageTaskEditDialog(utilizator);
        } else {
            MessageAlert.showErrorMessage(null, "Niciun utilizator selectat");
        }
    }

    private boolean filterByFirstName(Utilizator utilizator) {
        if (firstNameFilter.getText() == null || firstNameFilter.getText().trim().isEmpty()) {
            return true;
        }
        return utilizator.getFirstName().toLowerCase().startsWith(firstNameFilter.getText().toLowerCase().trim());
    }

    private boolean filterByLastName(Utilizator utilizator) {
        if (lastNameFilter.getText() == null || lastNameFilter.getText().trim().isEmpty()) {
            return true;
        }
        return utilizator.getLastName().toLowerCase().startsWith(lastNameFilter.getText().toLowerCase().trim());
    }


    public void showUtilizatorMenuDialog(Utilizator utilizator) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/utilizator-menu-view.fxml"));
            AnchorPane userMenu = fxmlLoader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Meniu utilizator " + utilizator.getFirstName() + " " + utilizator.getLastName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(userMenu);
            dialogStage.setScene(scene);

            UtilizatorMenuController utilizatorMenuController = fxmlLoader.getController();
            utilizatorMenuController.setData(utilizator, utilizatorService, prietenieService);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void showMessageTaskEditDialog(Utilizator utilizator) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/utilizator-menu-view.fxml")); //momentan
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(utilizatorService, dialogStage, utilizator);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}