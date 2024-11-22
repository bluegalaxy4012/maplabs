package ubb.scs.map.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.controller.UtilizatorController;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;

import java.io.IOException;

public class JavaFxGUI extends Application {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;

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
        Repository<Long, Utilizator> utilizatorDbRepository = new UtilizatorDbRepository(url, username, password, utilizatorValidator);

        Validator<Prietenie> prietenieValidator = new PrietenieValidator();
        Repository<Tuplu<Long, Long>, Prietenie> prietenieDbRepository = new PrietenieDbRepository(url, username, password, prietenieValidator);


        utilizatorService = new UtilizatorService(utilizatorDbRepository);
        prietenieService = new PrietenieService(prietenieDbRepository, utilizatorDbRepository);

        initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxGUI.class.getResource("../views/utilizator-view.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setServices(utilizatorService, prietenieService);
    }

}