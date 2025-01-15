package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.ui.ProfilePage;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.io.File;

public class ProfileController implements Observer<PrietenieEntityChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private Utilizator utilizator;
    private Stage dialog;
    @FXML
    private AnchorPane profilePane;
    private ProfilePage profilePage;

    public void setData(Utilizator utilizator, UtilizatorService utilizatorService, PrietenieService prietenieService, Stage dialog) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.utilizator = utilizator;
        this.dialog = dialog;

        prietenieService.addObserver(this);
        dialog.setOnCloseRequest(event -> prietenieService.removeObserver(this));

        initModel();
    }


    private void initModel() {
        int numarPrieteni = prietenieService.getNumarPrieteni(utilizator.getId());
        if (profilePage == null) {
            profilePage = new ProfilePage(utilizator, numarPrieteni);
            //profilePage.setLayoutY(60);
            profilePane.getChildren().add(profilePage);
        } else {
            profilePage.updateProfile(utilizator, numarPrieteni);
        }
    }

    @FXML
    public void handleEditProfilePicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagini", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Alege o poza de profil");
        File file = fileChooser.showOpenDialog(dialog);

        if (file != null) {
            utilizator.setProfilePictureUrl(file.toURI().toString());
            utilizatorService.updateUtilizator(utilizator);
            initModel();
        }
    }

    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }
}