
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{

	private Stage primaryStage;
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;

	ListView<String> listItems, listItems2;

		@Override
		public void start(Stage primaryStage) {
			primaryStage.setTitle("Chat App - Server");
			this.serverChoice = new Button("Server");
			this.serverChoice.setStyle("-fx-font-family: Arial; -fx-pref-width: 300px; -fx-pref-height: 300px");


			this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
				primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				}, this);

			});


			this.clientChoice = new Button("Client");
			this.clientChoice.setStyle("-fx-font-family: Arial; -fx-pref-width: 300px; -fx-pref-height: 300px");


			this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
				primaryStage.setTitle("Messaging App");
				s1.getStylesheets().add("/styles/style1.css");
				clientConnection = new Client(data->{
					Platform.runLater(()->{listItems2.getItems().add(data.toString());
					});
				});

				clientConnection.start();
			});

			this.buttonBox = new HBox(400, serverChoice, clientChoice);
			startPane = new BorderPane();
			startPane.setPadding(new Insets(70));
			startPane.setCenter(buttonBox);

			startScene = new Scene(startPane, 800,800);
			startScene.getStylesheets().add("/styles/style1.css");

			listItems = new ListView<String>();
			listItems.getStylesheets().add("/styles/style1.css");
			listItems2 = new ListView<String>();
			listItems2.getStylesheets().add("/styles/style1.css");

			c1 = new TextField();
			c1.getStylesheets().add("/styles/style1.css");
			b1 = new Button("Send");
			b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});

			sceneMap = new HashMap<String, Scene>();

			sceneMap.put("server",  createServerGui());
			sceneMap.put("client",  createClientGui());

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});



			primaryStage.setScene(startScene);
			primaryStage.show();
		}

		public static void main(String[] args) {
			launch(args);
		}

	public Scene createClientGui() {
		Parent root = null;
		try {
			// Read file fxml and draw interface.
			FXMLLoader loadFile = new FXMLLoader(getClass()
					.getResource("/FXML/Clientfxml.fxml"));

			root = loadFile.load();

			ClientController controller = loadFile.getController();
			controller.addClientLists();
			controller.updateMessages();

			root.getStylesheets().add("/styles/style1.css");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return new Scene(root, 900, 600);
	}

	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-font-family: Arial; -fx-background-color: coral");

		pane.setCenter(listItems);

		return new Scene(pane, 500, 400);


	}

	public Scene getCurrentScene() {
		return primaryStage.getScene();
	}

//
//	private TextArea messages = new TextArea();
//	private TextField input = new TextField();
//	private Button sendToAll = new Button("Send to All");
//	private Button sendToClients = new Button("Send to Clients");
//	private TextField targetClients = new TextField();
//
//	private Client connection;
//
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		messages.setPrefHeight(550);
//		messages.setEditable(false);
//
//		VBox root = new VBox(8);
//		root.setAlignment(Pos.CENTER);
//		root.setStyle("-fx-background-color: LIGHTBLUE;");
//		root.getChildren().addAll(messages, input);
//
//		HBox buttons =  new HBox(8);
//		buttons.setAlignment(Pos.CENTER);
//		buttons.getChildren().addAll(sendToAll, sendToClients, targetClients);
//
//		root.getChildren().add(buttons);
//
//		Label infoLabel = new Label("Type your message and click 'Send to All' to send to everyone. " +
//				"To send to specific clients, type their client numbers separated by commas in the text box next to 'Send to Clients' button and click it.");
//		infoLabel.setWrapText(true);
//		root.getChildren().add(infoLabel);
//
//		sendToAll.setOnAction(e -> {
//			String message = input.getText();
//			if (!message.isEmpty()) {
//				connection.send(message);
//				input.clear();
//			}
//		});
//		sendToClients.setOnAction(e -> {
//			String message = input.getText();
//			String targets = targetClients.getText();
//
//			if (!message.isEmpty() && !targets.isEmpty()) {
//				//The array below captures every client number into an array of integers.
//				int[] targetArray = Arrays.stream(targets.split(","))
//						.mapToInt(Integer::parseInt).toArray();
//				connection.sendTo(message, targetArray);
//				input.clear();
//				targetClients.clear();
//			}
//		});
//        root.getStylesheets().add("/styles/styles1.css");
//		Scene scene = new Scene(root);
//        scene.getStylesheets().add("/styles/styles1.css");
//		primaryStage.setScene(scene);
//		primaryStage.setTitle("Client GUI");
//		primaryStage.show();
//
//		connection = new Client(data -> {
//			messages.appendText(data.toString() + "\n");
//		});
//		connection.start();
//	}

}
