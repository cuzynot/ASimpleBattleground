package main;
/** [ChatServer.java]
 * Server class
 * @author Yili
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import data_structures.SimpleLinkedList;
import data_structures.SimpleQueue;
import graphics.Field;

public class Server {

	private ServerSocket serverSocket; // server socket for connection
	private boolean running;  // controls if the server is accepting clients

	private SimpleLinkedList<ClientObject> clients; // list of clients

	private int mapSize;
	private static int[][] map;
	private Field port;

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
		final int WIDTH = 750;
		final int HEIGHT = 300;

		running = true;
		clients = new SimpleLinkedList<ClientObject>(); // list of clients
		mapSize = 10;
		port = new Field(400, 100, 650, 200, 4);

		// make new jcomponents for the user to choose server port number
		JFrame f = new JFrame("Server");
		ServerPanel p = new ServerPanel(WIDTH, HEIGHT);

		// config frame
		f.add(p);
		f.addKeyListener(new ServerKeyListener());
		f.setSize(WIDTH, HEIGHT);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/** Go
	 * Starts the server
	 */
	private void go(int port) {
		randomizeMap();

		Socket client = null; //hold the client connection

		System.out.println("waiting for clients");

		try {
			serverSocket = new ServerSocket(port);  //assigns an port to the server
			serverSocket.setSoTimeout(120000);  // 2 min timeout

			while (running) {  // this loops to accept multiple clients
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

	/** printToAll
	 * Takes in username of client and message to send
	 * and sends it to all active clients
	 */
	private void printToAll(String username, String msg) {
		for (int i = 0; i < clients.size(); i++) {
			ClientObject cur = clients.get(i);
			if (!cur.getUsername().equals(username)) {
				cur.getOutput().println(msg);
				cur.getOutput().flush();
			}
		}
	}

	private void printToOne(String name, String msg) {
		int i = 0;
		boolean found = false;
		while (!found && i < clients.size()) {
			ClientObject cur = clients.get(i);
			if (cur.getUsername().equals(name)) {
				cur.getOutput().println(msg);
				cur.getOutput().flush();
				found = true;
			}
			i++;
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
				cur.getOutput().println("add " + user + " 0");
				cur.getOutput().flush();
			}
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

						} else if (msg.startsWith("hit")) {
							String[] arr = msg.split(" ");
							printToOne(arr[1], msg);

						} else {// if (msg.startsWith("xy ")) { // or score
							if (msg.startsWith("score")) {
								int score = Integer.parseInt(msg.substring(msg.lastIndexOf(" ") + 1));
								client.setScore(score);
							}

							// print message to everybody
							printToAll(client.getUsername(), msg);
						}
					}
				} catch (IOException e) { 
					System.out.println("Failed to receive msg from the client, deleting " + client.getUsername());

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
	private class ServerKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			String s = port.getString();
			if (key == KeyEvent.VK_BACK_SPACE) {
				if (s.length() > 0) {
					port.setString(s.substring(0, s.length() - 1));
				}
			} else if (key == KeyEvent.VK_ENTER) {
				// get the desired port number
				int port = Integer.parseInt(s);
				// start the server on port
				go(port);
			} else if (key != KeyEvent.VK_SPACE) {
				// } else if ((key != KeyEvent.VK_SHIFT) && (key != KeyEvent.VK_CONTROL) && (key != KeyEvent.VK_ALT)) {
				char c = e.getKeyChar();
				if (s.length() < port.getMaxLength()) {
					port.setString((s + c).toLowerCase());
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

	} //end of inner class

	//***** Inner class - Stores client information
	private class ServerPanel extends JPanel {
		private final int WIDTH;
		private final int HEIGHT;
		private Color color;
		private Color alt;
		private Font font;

		ServerPanel(int WIDTH, int HEIGHT) {
			this.WIDTH = WIDTH;
			this.HEIGHT = HEIGHT;
			color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
			alt = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
			font = new Font("Lora", Font.PLAIN, 100);
		}

		@Override
		public void paintComponent(Graphics g) {
			// draw background
			g.setColor(color);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			// draw text field
			g.setColor(Color.WHITE);
			g.fillRect(port.getX1(), port.getY1(), port.getX2() - port.getX1(), port.getY2() - port.getY1());

			// draw strings
			g.setColor(alt);
			g.setFont(font);
			g.drawString("PORT:", 0, port.getY2());

			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString(port.getString(), port.getX1(), port.getY2());

			repaint();
		}
	} //end of inner class

	//***** Inner class - Stores client information
	private class ClientObject {
		private String username; // client username
		private String status = ""; // user status
		private int score;

		private PrintWriter output; // assign printwriter to network stream
		private BufferedReader input; // stream for network input
		private Socket client;  // keeps track of the client socket

		/** Client
		 * Constructor
		 * @param Socket socket, used to establish connection to its client
		 */
		ClientObject(Socket socket) {
			this.client = socket;
			try { // assign all connections to client
				output = new PrintWriter(client.getOutputStream());
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				username = input.readLine();
				score = 0;

				String[] usernames = new String[clients.size()];

				for (int i = 0; i < clients.size(); i++) {
					ClientObject cur = clients.get(i);

					// if there is a person with the same user name
					if (username.equals(cur.getUsername())) {
						// send "duplicate" to client so they can enter a new name
						System.out.println("printed duplicate");

						output.println("duplicate");
						output.flush();

						// return
						return;
					}

					usernames[i] = "add " + cur.getUsername() + " " + cur.getScore();
				}

				// set status to online
				status = "Online";

				// send usernames and statuses of currently active users to client
				output.println("notduplicate");
				for (String s : usernames) {
					System.out.println("printed " + s);
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
		
		/** setScore
		 * This method updates the score of a user
		 * @param int score, new score of a user
		 */
		public void setScore(int score) {
			this.score = score;
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
		
		/** getScore
		 * This method retuns the score of a user
		 * @return int username, current score of client
		 */
		public int getScore() {
			return score;
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