package main;
/** [ChatClient.java]
 * Group chat client class
 * @author Josh Cai
 */


//Imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Client {

	// init vars and gui
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	private ArrayList<Player> players;
	private boolean running;
	private String build;

	private int mapSize;
	private static int[][] map;

	private String address;
	private int port;
	private String name;

	/**
	 * ChatClient 
	 * constructor
	 */
	public Client() {
		running = true;

		// get address and port and connect, then get username
		players = new ArrayList<Player>();

		address = "localhost"; // JOptionPane.showInputDialog("Enter IP Address:");
		port = 5001; //Integer.parseInt(JOptionPane.showInputDialog("Enter port (enter a number or else the program will crash):"));
		name = JOptionPane.showInputDialog("Enter username:").replace(" ", "");

		connect(address, port);

		output.println(name);
		output.flush();

		// check duplicate username
		try {
			if (input.ready()) {
				String msg = input.readLine();
				System.out.println("first msg " + msg);

				while (msg.equals("duplicate")) { //Loop until name is not duplicate
					JOptionPane.showMessageDialog(null, "Username already exists");

					connect(address, port);

					name = JOptionPane.showInputDialog("Enter username:").replace(" ", "");

					output.println(name);
					output.flush();

					msg = input.readLine();
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// start communication with server
		go();
	}
	/**
	 * Main
	 * @param args parameters from command line
	 */
	//	public static void main(String[] args) {
	//		c = new Client();
	//	}

	// getters
	public PrintWriter getOutput() {
		return output;
	}

	public String getName() {
		return name;
	}

	public String getBuild() {
		return build;
	}

	public ArrayList<Player> getPlayers(){
		return players;
	}

	public int[][] getMap(){
		return map;
	}

	public void update(Player player) {
		output.println("xy " + player.getName() + " " + player.getX() + " " + player.getY());
	}


	/* go
	 * communicate  with server (get messages)
	 */
	private void go() {
		// OutputThread ot = new OutputThread();

		// call a method that connects to the server
		// after connecting loop and keep appending[.append()] to the JTextArea

		Runnable runnable = new Runnable() {
			String msg;
			String[] arr;

			public void run(){
				try {
					input.readLine();
					
					while (running) {
						if (input.ready()) { // check for an incoming messge
							msg = input.readLine(); // read the message
							arr = msg.split(" ");

							String command = arr[0];
							String name = arr[1];

							if (command.equals("map")) { // if send map
								mapSize = Integer.parseInt(name);
								map = new int[mapSize][mapSize];
								for (int i = 0; i < mapSize; i++) {
									msg = input.readLine();
									arr = msg.split(" ");
									for (int j = 0; j < arr.length; j++) {
										map[i][j] = Integer.parseInt(arr[j]);
									}
								}
							} else if (command.equals("add")) { // if add user
								players.add(new Player(name));
								// reAddUsers();
							} else if (command.equals("delete")) { // if delete user
								boolean found = false;
								int count = 0;
								
								while (!found && count < players.size()) {
									if (players.get(count).getName().equals(name)) {
										players.remove(count);
										found = true;
									}
									count++;
								}
							} else if (command.equals("xy")) {
								double x = Double.parseDouble(arr[2]);
								double y = Double.parseDouble(arr[3]);

								boolean found = false;
								int count = 0;

								while (!found && count < players.size()) {
									Player p = players.get(count);

									if (p.getName().equals(name)) {
										p.setX(x);
										p.setY(y);
										found = true;
									}
									count++;
								}
							}
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				try { // after leaving the main loop we need to close all the sockets
					input.close();
					output.close();
					socket.close();
				} catch (Exception l) {}
			}
		};

		Thread thread = new Thread(runnable);

		thread.start();
	}

	/*
	 * connect
	 * Connect to server
	 */
	private void connect(String address, int port) {
		try {
			socket = new Socket(address, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}