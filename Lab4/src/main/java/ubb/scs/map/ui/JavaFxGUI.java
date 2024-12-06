package ubb.scs.map.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.controller.UtilizatorController;
import ubb.scs.map.domain.Mesaj;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.MesajValidator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.MesajDbRepository;
import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.service.MesajService;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;

import java.io.IOException;

public class JavaFxGUI extends Application {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private MesajService mesajService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Retea de socializare");

        String username = "postgres";
        String password = "maplabs";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";

        Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
        UtilizatorDbRepository utilizatorDbRepository = new UtilizatorDbRepository(url, username, password, utilizatorValidator);

        Validator<Prietenie> prietenieValidator = new PrietenieValidator();
        PrietenieDbRepository prietenieDbRepository = new PrietenieDbRepository(url, username, password, prietenieValidator, utilizatorDbRepository);

        Validator<Mesaj> mesajValidator = new MesajValidator();
        Repository<Long, Mesaj> mesajDbRepository = new MesajDbRepository(url, username, password, mesajValidator, utilizatorDbRepository);

        utilizatorService = new UtilizatorService(utilizatorDbRepository);
        prietenieService = new PrietenieService(prietenieDbRepository);
        mesajService = new MesajService(mesajDbRepository);

        initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxGUI.class.getResource("../views/utilizator-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setData(utilizatorService, prietenieService, mesajService, primaryStage);
    }
}