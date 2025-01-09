package ubb.scs.map.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;

import java.util.Arrays;
import java.util.List;

public class JavaFxGUIController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button btnHello;

    @FXML
    private ListView<Utilizator> listView;

    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    public void setPrietenieService(PrietenieService prietenieService) {
        this.prietenieService = prietenieService;
    }

    @FXML
    public void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        btnHello.setText("alt text");
        List<Utilizator> list = Arrays.asList(new Utilizator("dan", "ana"));
        ObservableList<Utilizator> observableList = FXCollections.observableList(list);
        listView.setItems(observableList);
    }
}