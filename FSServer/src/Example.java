
/**
 * A sample server application. Both server and clients must implement the
 * MessageListener interface.
 */
class Server implements MessageListener {
	/**
	 * Creates a server channel on the current machine and assigns port 8888 to
	 * it.
	 */
	TCPChannel channel;// = new TCPChannel("data.cs.purdue.edu",8888);

	/**
	 * Constructor.
	 * 
	 * @throws ChannelException
	 */
	public Server() throws ChannelException {
		// inform the channel that when new messages are received forward them
		// to the current server object.
		this.channel = new TCPChannel(8888);
		channel.setMessageListener(this);

	}

	/**
	 * Implements the message received method from the MessageListener
	 * interface. Will be invoked (called) whenever a message is received on the
	 * channel "channel"
	 * 
	 * @param message
	 *            the info received
	 * @param clientID
	 *            an id that identifies where it came from.
	 */
	@Override
	public void messageReceived(String message, int clientID) {
		System.out.println(message);
		// simple reply that message received, send it to the same client it
		// came from.
		try {
			channel.sendMessage(message + ": Received", clientID);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}
}

/**
 * A sample client.
 */
class Client implements MessageListener {
	/**
	 * Unlike the server channel, the client channel also needs to specify the
	 * machine address where the server is (using an overloaded constructor) for
	 * TCPChannel.
	 */
	TCPChannel channel = null;

	/**
	 * Constructor.
	 */
	public Client() {
		try {
			this.channel = new TCPChannel("moore01.cs.purdue.edu", 8888);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
		// inform the channel that when new messages are received forward them
		// to the current client object.
		this.channel.setMessageListener(this);
	}

	@Override
	public void messageReceived(String message, int channelID) {
		// simply print the message.
		System.out.println(message);
	}

	public void sendMessage(String message) {
		// send a message, since we did not specify a client ID, then the
		// message will be sent to the server.
		try {
			channel.sendMessage(message);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

}

/**
 * An example controller that has one server and two clients.
 */
public class Example {

	public static void main(String[] args) throws ChannelException {
		// create the objects.
		Server server = new Server();
		Client client1 = new Client();
		Client client2 = new Client();

		// send messages from clients.
		client1.sendMessage("Hi from 1");
		client2.sendMessage("Hi from 2");

		// wait for the reply.
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// close the client and server channels.
		client1.channel.close();
		client2.channel.close();
		server.channel.close();

	}
}
