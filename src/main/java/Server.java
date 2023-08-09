import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class Server {

	int count = 1;

	private GuiClient guiClient;

	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	ArrayList<Integer> clientNumber = new ArrayList<Integer>();
	TheServer server;
	private Consumer<Serializable> callback;

	int[] targetClients;
	boolean newClient = false;
	
	
	Server(Consumer<Serializable> call, GuiClient guiClient){
		this.guiClient = guiClient;
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				clientNumber.add(count);

				//Platform.runLater(() -> {
				//	ClientJoinEvent event = new ClientJoinEvent(count);
				//	guiClient.getCurrentScene().getRoot().fireEvent(event);
				//});

				newClient = true;
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			private Object clientConnection;

			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}

			//public void updateClientList() {
			//	String clientList = "ClientList: ";
			//	for (ClientThread t : clients) {
			//		clientList += " " + t.count;
			//	}
			//	updateClients(clientList);
			//}

			public void newClient(String message) {
				ClientThread t = clients.get(clients.size()-1);
				try {
					t.out.writeObject(message);
				}
				catch(Exception e) {}
			}

			public void clientDisconnects(String message) {
				for (int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i); //specific client
					try {
						t.out.writeObject(message);
					}
					catch(Exception e) {}
				}

			}
			
			public void updateClients(String message, int[] targetClients){ //vararg allows multiple numbers of integer args
				String newMessage = message;
				for(int i = 0; i < clients.size(); i++) { //List of clients
					ClientThread t = clients.get(i); //specific client
					try {
						if (newClient) {
							for (int j = 0; j < clientNumber.size(); j++) {
								newMessage = newMessage + clientNumber.get(j) + " ";
							}
							t.out.writeObject(newMessage);
							newMessage = message;
						}
						System.out.println("Target length: " + targetClients.length);
						if (targetClients.length == 0 || containsNumber(targetClients, t.count)) {
							System.out.println(message);
							t.out.writeObject(message); //Does our array of client ids contain that specific client
							System.out.println("written");
						}
					}
					catch(Exception e) {

					}
				}
				newClient = false;
			}

			public boolean containsNumber(int[] array, int target) {
				for (int number : array) {
					if (number == target) {
						return true;
					}
				}
				return false;
			}
			
			public void run(){


				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(EOFException e) {
					callback.accept("Client" + count + " has disconnected from the server!");
					clientDisconnects("Client #"+count+" has left the server!");
					clients.remove(this);
					clientNumber.remove(Integer.valueOf(count));
				} catch (Exception e) {
					System.out.println("Streams not open");
				}
				newClient("You are client number " + count);
				updateClients("Clients on server: client #", targetClients);

				 while(true) {
					    try {
					    	String data = in.readObject().toString();
							String[] tokens = data.split(" ", 2);
							String command = tokens[0];
							String message = data;
							if (tokens.length > 1) {
								message = tokens[1];
							}

							if (command.equals("SENDTO")) {
								String[] args = message.split(" ", 2);
								String targetClientString = args[0];
								targetClients = Arrays.stream(targetClientString.split(",")).mapToInt(Integer::parseInt).toArray();
								if (args.length > 1) {
									message = args[1];
								} else {
									message = "";
								}

							} else {
								targetClients = new int[0];
							}
							callback.accept("client: " + count + " sent: " + message);
							updateClients("client #" + count + " said: " + message, targetClients);

						}
					    catch(Exception e) {
							e.printStackTrace();
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
							clientDisconnects("Client #"+count+" has left the server!");
					    	clients.remove(this);
							clientNumber.remove(Integer.valueOf(count));
					    	break;
					    }
					}
				}//end of run
		}//end of client thread
}


	
	

	
