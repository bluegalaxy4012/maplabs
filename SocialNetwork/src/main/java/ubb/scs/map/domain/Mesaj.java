package ubb.scs.map.domain;

import ubb.scs.map.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class Mesaj extends Entity<Long> {
    private Utilizator from;
    private List<Utilizator> to;
    private String text;
    private LocalDateTime date;
    private Mesaj repliedMesaj;

    public Mesaj(Utilizator from, List<Utilizator> to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = LocalDateTime.now();
        repliedMesaj = null;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Mesaj getRepliedMessage() {
        return repliedMesaj;
    }

    public void setRepliedMessage(Mesaj repliedMesaj) {
        this.repliedMesaj = repliedMesaj;
    }

    @Override
    public String toString() {
        if (repliedMesaj == null) {
            return from.getFirstName() + " " + from.getLastName() + "(la " + date.format(Constants.DATE_TIME_FORMATTER) + "): " + text;
        }
        return from.getFirstName() + " " + from.getLastName() + "(la " + date.format(Constants.DATE_TIME_FORMATTER) + "): " + text + "         [raspuns la mesajul lui " + repliedMesaj.getFrom().getFirstName() + " " + repliedMesaj.getFrom().getLastName() + ": " + repliedMesaj.getText() + "]";
    }
}
