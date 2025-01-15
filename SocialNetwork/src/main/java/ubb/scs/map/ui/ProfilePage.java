package ubb.scs.map.ui;

import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import ubb.scs.map.domain.Utilizator;

public class ProfilePage extends AnchorPane {
    private final Label usernameLabel;
    private final Label friendsNumberLabel;
    private final ImageView profileImage;

    public ProfilePage(Utilizator utilizator, int numarPrieteni) {
        usernameLabel = new Label(utilizator.getUsername());
        friendsNumberLabel = new Label(numarPrieteni + " prieteni");
        profileImage = new ImageView(new Image(utilizator.getProfilePictureUrl()));

        usernameLabel.setLayoutX(233);
        usernameLabel.setLayoutY(263);
        usernameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        usernameLabel.setTextAlignment(TextAlignment.CENTER);
        usernameLabel.setEffect(new Reflection());

        friendsNumberLabel.setLayoutX(257);
        friendsNumberLabel.setLayoutY(340);
        friendsNumberLabel.setStyle("-fx-font-size: 18px; -fx-font-style: italic;");
        friendsNumberLabel.setTextAlignment(TextAlignment.CENTER);

        profileImage.setLayoutX(236);
        profileImage.setLayoutY(56);
        profileImage.setFitWidth(129);
        profileImage.setFitHeight(129);

        this.getChildren().addAll(usernameLabel, friendsNumberLabel, profileImage);
        this.setMouseTransparent(true);
    }

    public void updateProfile(Utilizator utilizator, int numarPrieteni) {
        usernameLabel.setText(utilizator.getUsername());
        friendsNumberLabel.setText(numarPrieteni + " prieteni");
        profileImage.setImage(new Image(utilizator.getProfilePictureUrl()));
    }
}