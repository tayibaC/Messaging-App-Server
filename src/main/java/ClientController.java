import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ClientController {

    @FXML
    private MenuItem helpMenu;

    @FXML
    private TextField messageTxt;

    @FXML
    private Button sendButton;

    @FXML
    private VBox clientLists;

    @FXML
    private VBox clientMessages;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    public void initialize() {
        clientLists.addEventHandler(ClientJoinEvent.CLIENT_JOINED, this::onClientJoin);
    }

    private void onClientJoin(ClientJoinEvent event) {
        addButtonForClient(event.getClientId());
    }

    private void addButtonForClient(int clientId) {
        Button clientButton = new Button("Client #" + clientId);

        clientLists.getChildren().add(clientButton);
        clientLists.getStylesheets().add("/styles/style1.css");
    }


    @FXML
    void msgTxtHandler(ActionEvent event) {

    }

    @FXML
    void sendBnHandler(ActionEvent event) {

    }

    @FXML
    void showHelpMenu(ActionEvent event) {

    }

    // Adds clients to the contact list
    // Updates everytime a client enters or leaves the server
    @FXML
    public void addClientLists() {
        CheckBox checkBox = new CheckBox(" All");
        clientLists.getChildren().add(checkBox);

    }

    // Updates messages for clients
    @FXML
    public void updateMessages() {
        Label msgLabel = new Label("Your message history starts here:");
        clientMessages.getChildren().add(msgLabel);
    }

}
