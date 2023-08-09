import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
			String message = in.readObject().toString();
			callback.accept(message);
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(String data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	//sendTo
	//This function is purposely designed to send specific messages
	//to clients that have been selected by the messenger.
	public void sendTo(String data, int... targetClients) {
		try {
			String targetClientString = Arrays.stream(targetClients)
					.mapToObj(Integer::toString)
					.collect(Collectors.joining(","));
			out.writeObject("SENDTO " + targetClientString + " " + data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
