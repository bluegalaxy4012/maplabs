package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ubb.scs.map.domain.Mesaj;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.MesajService;
import ubb.scs.map.utils.events.MesajEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;

public class ChatController implements Observer<MesajEntityChangeEvent> {
    private final ObservableList<Mesaj> model = FXCollections.observableArrayList();
    private final ObservableList<Mesaj> sesiuneMesaje = FXCollections.observableArrayList();
    private Utilizator sender;
    private ObservableList<Utilizator> receivers;
    private MesajService mesajService;

    @FXML
    private ListView<Mesaj> listView;
    @FXML
    private TextArea messageTextArea;

    public void setData(Utilizator sender, ObservableList<Utilizator> receivers, MesajService mesajService, Stage dialog) {
        this.sender = sender;
        this.receivers = receivers;
        this.mesajService = mesajService;
        mesajService.addObserver(this);
        dialog.setOnCloseRequest(event -> mesajService.removeObserver(this));
        initModel();
    }

    private void initModel() {
        if (receivers.size() > 1) {
            listView.setItems(sesiuneMesaje);
        } else {
            List<Mesaj> mesaje = mesajService.getMessages(sender.getId()).stream().filter(mesaj -> (mesaj.getFrom().getId().equals(sender.getId()) && mesaj.getTo().contains(receivers.get(0))) || mesaj.getFrom().equals(receivers.get(0))).collect(Collectors.toList());
            model.setAll(mesaje);
            listView.setItems(model);
        }
    }

    @FXML
    public void handleSendMessage(ActionEvent actionEvent) {
        String text = messageTextArea.getText();
        if (text.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Mesajul nu poate fi gol");
            return;
        }

        Mesaj mesaj = new Mesaj(sender, receivers, text);
        mesajService.sendMessage(mesaj);
        if (receivers.size() > 1) {
            sesiuneMesaje.add(mesaj);
        }
//        else {
//            model.add(mesaj);
//        }
        messageTextArea.clear();
    }

    @FXML
    public void handleReplyToMessage(ActionEvent actionEvent) {
        Mesaj selectedMesaj = listView.getSelectionModel().getSelectedItem();
        if (selectedMesaj == null) {
            MessageAlert.showErrorMessage(null, "Niciun mesaj selectat");
            return;
        }

        String text = messageTextArea.getText();
        if (text.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Mesajul nu poate fi gol");
            return;
        }

        Mesaj newMesaj = new Mesaj(sender, receivers, text);
        newMesaj.setRepliedMessage(selectedMesaj);
        mesajService.sendMessage(newMesaj);

        if (receivers.size() > 1) {
            sesiuneMesaje.add(newMesaj);
        }

        messageTextArea.clear();
    }

    @Override
    public void update(MesajEntityChangeEvent mesajEntityChangeEvent) {
        initModel();
    }
}