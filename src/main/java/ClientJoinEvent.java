import javafx.event.Event;
import javafx.event.EventType;

public class ClientJoinEvent extends Event {
    public static final EventType<ClientJoinEvent> CLIENT_JOINED = new EventType<>(Event.ANY, "CLIENT_JOINED");

    private final int clientId;

    public ClientJoinEvent(int clientId) {
        super(CLIENT_JOINED);
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }
}
