package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.io.File;


public class ProfileController implements Observer<PrietenieEntityChangeEvent> {
    private final String baseDir = System.getProperty("user.dir");
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private Utilizator utilizator;
    private Stage dialog;
    @FXML
    private Label usernameLabel, friendsNumberLabel;
    @FXML
    private ImageView profileImage;

    public void setData(Utilizator utilizator, UtilizatorService utilizatorService, PrietenieService prietenieService, Stage dialog) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.utilizator = utilizator;
        this.dialog = dialog;
        prietenieService.addObserver(this);
        dialog.setOnCloseRequest(event -> prietenieService.removeObserver(this));

        initModel();
    }

    //default https://i.imgur.com/ELQuYa3.png


    private void initModel() {
        usernameLabel.setText(utilizator.getUsername());
        friendsNumberLabel.setText(prietenieService.getNumarPrieteni(utilizator.getId()) + " prieteni");
        profileImage.setImage(new Image(utilizator.getProfilePictureUrl()));
    }

    public void handleEditProfilePicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Alege o poza de profil");
        File file = fileChooser.showOpenDialog(dialog);

        if (file != null) {
            //System.out.println(file.toURI().toString());
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
