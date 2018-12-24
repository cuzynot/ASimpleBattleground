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

	//Init vars and gui
	static Client c;

	Socket socket;
	BufferedReader input;
	PrintWriter output;

	ArrayList<Player> players;
	boolean running = true;
	String name;

	/**
	 * ChatClient 
	 * constructor
	 */
	public Client() {
		//Get address and port and connect, then get username
		players = new ArrayList<Player>();
		
		String address = JOptionPane.showInputDialog("Enter IP Address:");
		int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port (enter a number or else the program will crash):"));
		name = JOptionPane.showInputDialog("Enter username:").replace(" ", "");

		connect(address, port);

		output.println(name);
		output.flush();

		//Check duplicate username
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

		go(); //Start communication with server
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

	public ArrayList<Player> getPlayers(){
		return players;
	}


	/* go
	 * communicate  with server (get messages)
	 */
	private void go() {
		// OutputThread ot = new OutputThread();

		// call a method that connects to the server
		// after connecting loop and keep appending[.append()] to the JTextArea

		Runnable runnable = new Runnable() {
			public void run(){
				try {
					input.readLine();
					while (running) {
						if (input.ready()) { // check for an incoming messge
							String msg = input.readLine(); // read the message
							String[] arr = msg.split(" ");

							String command = arr[0];
							String name = arr[1];

							if (command.equals("add")) { //if add user
								players.add(new Player(name));
								// reAddUsers();
							} else if (command.equals("delete")) { //if delete user
								// users.remove(arr[1]);
								// reAddUsers();
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

							msg = "";
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