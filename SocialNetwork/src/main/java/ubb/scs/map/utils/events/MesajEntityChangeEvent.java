package ubb.scs.map.utils.events;

import ubb.scs.map.domain.Mesaj;

public class MesajEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Mesaj data, oldData;

    public MesajEntityChangeEvent(ChangeEventType type, Mesaj data) {
        this.type = type;
        this.data = data;
    }

    public MesajEntityChangeEvent(ChangeEventType type, Mesaj data, Mesaj oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Mesaj getData() {
        return data;
    }

    public Mesaj getOldData() {
        return oldData;
    }
}