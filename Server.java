/** [ChatServer.java]
 * Server class
 * @author Yili
 */

// imports for network communication
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Server {

	private ServerSocket serverSocket; // server socket for connection
	private static Boolean running = true;  // controls if the server is accepting clients

	private ArrayList<ClientObject> clients = new ArrayList<ClientObject>(); // list of clients
	private JTextField t; // textfield for the port number

	/** Main
	 * @param args parameters from command line
	 */
	public static void main(String[] args) { 
		new Server(); // start the server
	}

	/** init
	 * Makes a new frame for user to enter what port they wish their server to be on
	 */
	public Server() {
		// make new jcomponents for the user to choose server port number
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		t = new JTextField(4);
		t.setText("Port");
		t.addActionListener(new SendActionListener());

		// add textfield to panel, and panel to frame
		p.add(t);
		f.add(p);

		// config frame
		f.setSize(200, 100);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}

	/** Go
	 * Starts the server
	 */
	private void go(int port) { 
		System.out.println("Waiting for a client connection..");

		Socket client = null; //hold the client connection

		try {
			serverSocket = new ServerSocket(port);  //assigns an port to the server
			serverSocket.setSoTimeout(120000);  // 2 min timeout

			while(running) {  // this loops to accept multiple clients
				client = serverSocket.accept();  // wait for connection
				System.out.println("Client connected");
				ClientObject c = new ClientObject(client);

				// if there is no duplicate name
				if (!c.getStatus().equals("")) {
					Thread t = new Thread(new ConnectionHandler(c)); // create a thread for the new client and pass in the socket
					t.start(); // start the new thread
					clients.add(c); // add client to list of active clients
				}
			}
		} catch (Exception e) { 
			System.out.println("Error accepting connection");
			//close all and quit
			try {
				client.close();
				serverSocket.close();
			} catch (Exception e1) { 
				System.out.println("Failed to close socket");
			}
			System.exit(-1);
		}
	}

	/** print
	 * Takes in username of client and message to send
	 * and sends it to all active clients
	 */
	private void print(String msg) {
		for (int i = 0; i < clients.size(); i++) {
			ClientObject cur = clients.get(i);
			cur.getOutput().println(msg);
			cur.getOutput().flush();
		}
	}

	/** delete
	 * Takes in username of client and deletes it from list of active clients
	 */
	private void delete(String user) {
		for (int i = 0; i < clients.size(); i++) {
			ClientObject cur = clients.get(i);
			if (cur.getUsername().equals(user)) {
				clients.remove(i);
				i--;
			} else {
				cur.getOutput().println("delete " + user);
				cur.getOutput().flush();
			}
		}
	}

	/** add
	 * Takes in username of client and adds it to list of active clients
	 */
	private void add(String user) {
		for (int i = 0; i < clients.size(); i++) {
			ClientObject cur = clients.get(i);
			if (!cur.getUsername().equals(user)) {
				cur.getOutput().println("add " + user);
				cur.getOutput().flush();
			}
		}
	}

	/** change
	 * Takes in username of client and their new status
	 * and updates it for every client
	 */
	private void change(String user, String status) {
		for (int i = 0; i < clients.size(); i++) {
			ClientObject cur = clients.get(i);
			if (!cur.getUsername().equals(user)) {
				cur.getOutput().println("change " + user + " " + status);
				cur.getOutput().flush();
			}
		}
	}

	//***** Inner class - ActionListener to get port number
	private class SendActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// get the desired port number
			int port = Integer.parseInt(t.getText());			
			// start the server on port
			go(port);
		}

	}

	//***** Inner class - thread for client connection
	private class ConnectionHandler implements Runnable { 
		private ClientObject client;
		/** ConnectionHandler
		 * Constructor
		 * @param Client c, the client object containing its socket, input and output streams, username and status
		 */    
		ConnectionHandler(ClientObject c) { 
			this.client = c;

		} //end of constructor

		/** run
		 * This method gets client input, interprets it, and update it for all clients 
		 */
		public void run() {  
			String msg = "";
			BufferedReader input = client.getInput(); // stream for network input

			// get a message from the client
			while(!msg.equals("exit")) {  // loop until the user exits
				try {
					if (input.ready()) { // check for an incoming messge
						msg = input.readLine();  // get a message from the client

						if (msg.equals("exit")) { // if a user decides to exit
							delete(client.getUsername()); // remove their reference

						} else if (msg.startsWith("xy ")) {
							// truncate message
							// msg = msg.substring(3);
							// print message to everybody
							print(msg);
						}

					}
				} catch (IOException e) { 
					System.out.println("Failed to receive msg from the client");
					e.printStackTrace();
				}
			}

			// close the socket
			try {
				input.close();
				client.getOutput().close();
				client.getSocket().close();
			} catch (Exception e) { 
				System.out.println("Failed to close socket");
			}

		} // end of run()
	} //end of inner class

	//***** Inner class - Stores client information
	private class ClientObject {
		private String username; // client username
		private String status = ""; // user status
		private PrintWriter output; // assign printwriter to network stream
		private BufferedReader input; // stream for network input
		private Socket client;  // keeps track of the client socket

		/** Client
		 * Constructor
		 * @param Socket socket, used to establish connection to its client
		 */
		ClientObject(Socket socket){
			this.client = socket;
			try { // assign all connections to client
				this.output = new PrintWriter(client.getOutputStream());
				InputStreamReader stream = new InputStreamReader(client.getInputStream());
				this.input = new BufferedReader(stream);
				username = input.readLine();

				String[] usernames = new String[clients.size()];

				for (int i = 0; i < clients.size(); i++) {
					ClientObject cur = clients.get(i);

					// if there is a person with the same user name
					if (username.equals(cur.getUsername())) {
						// send duplicate to client so they can enter a new name
						output.println("duplicate");
						output.flush();

						// return
						return;
					}

					usernames[i] = "add " + cur.getUsername();
				}

				// set status to online
				status = "Online";

				// send usernames and statuses of currently active users to client
				output.println("notduplicate");
				for (String s : usernames) {
					output.println(s);
					output.flush();
				}

				// update lists of active users for every client
				add(username);

			} catch(IOException e) {
				e.printStackTrace();
			}            
		}

		/** setStatus
		 * This method updates the status of a user
		 * @param String s, the new status of client
		 */
		public void setStatus(String s) {
			this.status = s;
		}

		/** getStatus
		 * This method retuns the status of a user
		 * @return String status, current status of client
		 */
		public String getStatus() {
			return status;
		}

		/** getUsername
		 * This method retuns the username of a user
		 * @return String username, current username of client
		 */
		public String getUsername() {
			return username;
		}

		/** getOutput
		 * This method retuns the output stream of a user
		 * @return PrintWriter output, the output stream of client
		 */
		public PrintWriter getOutput() {
			return output;
		}

		/** getInput
		 * This method retuns the input stream of a user
		 * @return BufferedReader input, the input stream of client
		 */
		public BufferedReader getInput() {
			return input;
		}

		/** getSocket
		 * This method retuns the client socket
		 * @return Socket client, the socket of client
		 */
		public Socket getSocket() {
			return client;
		}
	} //end of inner class

} //end of Class