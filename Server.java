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

import data_structures.SimpleQueue;

public class Server {

	private ServerSocket serverSocket; // server socket for connection
	private static Boolean running = true;  // controls if the server is accepting clients

	private ArrayList<ClientObject> clients = new ArrayList<ClientObject>(); // list of clients
	private JTextField t; // textfield for the port number
	
	private int mapSize = 10;
	private static int[][] map;

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
		t.addActionListener(new SendActionListener());

		// add textfield to panel, and panel to frame
		p.add(t);
		f.add(p);

		// config frame
		f.setSize(200, 100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/** Go
	 * Starts the server
	 */
	private void go(int port) {
		randomizeMap();
		
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
	
	private void randomizeMap() {
		// instantiate map
		map = new int[mapSize][mapSize];

		// randomize the walls
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[0].length - 1; j++) {
				map[i][j] = (int)(Math.random() * 2) + 1; // either 1 (a wall) or 0 (open)
			}
		}

		// make sure the exterior are walls
		for (int i = 0; i < map.length; i++) {
			map[0][i] = 1;
			map[map[0].length - 1][i] = 1;
		}
		for (int i = 0; i < map[0].length; i++) {
			map[i][0] = 1;
			map[i][map.length - 1] = 1;
		}

		int prevx = 1;
		int prevy = 1;

		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map[0].length - 1; j++) {
				if (map[i][j] == 0) {
					prevx = i;
					prevy = j;
				} else if (map[i][j] == 2) {
					for (int k = prevx; k < i; k++) {
						map[k][j] = 0;
					}
					for (int l = Math.min(prevy, j); l < Math.max(prevy, j); l++) {
						map[prevx][l] = 0;
					}

					bfs(i, j);
					prevx = i;
					prevy = j;
				}
			}
		}
	}

	private void bfs(int i, int j) {
		SimpleQueue<Integer> queue = new SimpleQueue<Integer>();
		queue.enqueue(i); // add x
		queue.enqueue(j); // and y coordinates of the staring point

		// bfs to make sure every cell in the map is connected
		while (!queue.isEmpty()) {
			int x = queue.dequeue();
			int y = queue.dequeue();

			// if current coordinate has not been visited yet
			if (map[x][y] == 2) {
				// make current cell blank and randomize adj cells
				map[x][y] = 0;

				// if any adj cell is open, they get added to the queue
				if (x - 1 > 0) {
					queue.enqueue(x - 1);
					queue.enqueue(y);
				}

				if (y - 1 > 0) {
					queue.enqueue(x);
					queue.enqueue(y - 1);
				}

				if (x + 1 < map.length) {
					queue.enqueue(x + 1);
					queue.enqueue(y);
				}

				if (y + 1 < map[0].length) {
					queue.enqueue(x);
					queue.enqueue(y + 1);
				}
			}
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
					
					// disconnect from server
					delete(client.getUsername());
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
				
				output.println("map " + mapSize);
				for (int i = 0; i < map.length; i++) {
					String s = "";
					for (int j = 0; j < map[0].length; j++) {
						s += map[i][j] + " ";
					}
					output.println(s);
				}
				output.flush();

			} catch(IOException e) {
				e.printStackTrace();
			}            
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