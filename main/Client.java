/**
 * [Client].java
 * Client is a class used to 
 * communicate to the server
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import data_structures.SimpleLinkedList;
import exceptions.DuplicateException;
import player.Player;
import player.builds.Assassin;
import player.builds.Guard;
import player.builds.Sniper;
import player.builds.Soldier;

public class Client {

	// init vars
	// socket, reader, and writer for communication
	// to the server
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	// player info
	private Player player;
	private SimpleLinkedList<Player> players;
	private boolean running;
	private String build;

	// info for the map
	private int mapSize;
	private int[][] map;

	// name of user
	private String name;

	// final static values
	private final static int KILL_CREDIT = 50;
	private final static int DIE_CREDIT = 10;

	/**
	 * Client
	 * constructor
	 * @param address, ip address
	 * @param port, port number
	 * @param name, name of user
	 * @param build, build of player
	 * @throws DuplicateException
	 * @throws IOException
	 */
	public Client(String address, int port, String name, int build) throws DuplicateException, IOException {		
		running = true;
		this.name = name;

		// get address and port and connect, then get username
		players = new SimpleLinkedList<Player>();
		
		// attempt to connect to the server
		connect(address, port);

		// print name
		output.println(name);
		output.flush();

		// check duplicate username
		String msg = input.readLine();

		// if the current username is already taken
		if (msg.equals("duplicate")) {
			throw new DuplicateException();
		}

		// make new player
		player = new Player(name);
		
		// set player's build
		if (build == 0) {
			player.setBuild(new Assassin());
		} else if (build == 1) {
			player.setBuild(new Guard());
		} else if (build == 2) {
			player.setBuild(new Sniper());
		} else if (build == 3) {
			player.setBuild(new Soldier());
		}

		// start communication with server
		go();
	}

	/**
	 * getPlayer
	 * this method returns the player this client holds
	 * @return player, the player this client holds 
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * getName
	 * this method returns the name of the client
	 * @return name, name of user
	 */
	public String getName() {
		return name;
	}

	/**
	 * getBuild
	 * this method returns the build the user has chosen
	 * @return build, build the user has chosen
	 */
	public String getBuild() {
		return build;
	}

	/**
	 * getPlayers
	 * this method returns a linked list of 
	 * players from the server
	 * @return players, list of players
	 */
	public SimpleLinkedList<Player> getPlayers(){
		return players;
	}

	/**
	 * getMap
	 * this method returns a 2d int array
	 * representing the map
	 * @return map, 2d int array of the map
	 */
	public int[][] getMap(){
		return map;
	}

	/**
	 * update
	 * this method updates player info to server
	 */
	public void update() {
		// print location and score of the player
		output.println("xy " + player.getName() + " " + player.getX() + " " + player.getY());
		output.println("score " + player.getName() + " " + player.getScore());
		output.flush();
	}

	/**
	 * println
	 * this methods sends a message to server
	 * @param msg, message to be sent to server
	 */
	public void println(String msg) {
		output.println(msg);
		output.flush();
	}

	/**
	 * disconnect
	 * this method disconnects client from server
	 */
	public void disconnect() {
		// tells server client has exited
		output.println("exit");
		output.flush();
		
		// close components
		try {
			socket.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.close();
	}

	/**
	 * go
	 * communicate  with server (get messages)
	 */
	private void go() {
		// call a method that connects to the server
		// after connecting loop and keep appending[.append()] to the JTextArea
		Runnable runnable = new Runnable() {
			// message received
			// in a string and in a string array
			String msg;
			String[] arr;

			public void run(){
				try {
					// while the client is active
					while (running) {
						if (input.ready()) { // check for an incoming messge
							msg = input.readLine(); // read the message
							arr = msg.split(" ");

							String command = arr[0];
							String second = "";
							if (arr.length > 1) {
								second = arr[1];
							}

							// used for looping through list of players
							boolean found = false;
							int count = 0;

							if (command.equals("map")) { // if send map
								// set map of the client
								mapSize = Integer.parseInt(second);
								map = new int[mapSize][mapSize];
								for (int i = 0; i < mapSize; i++) {
									msg = input.readLine();
									arr = msg.split(" ");
									for (int j = 0; j < arr.length; j++) {
										map[i][j] = Integer.parseInt(arr[j]);
									}
								}
							} else if (command.equals("add")) { // if add user
								// add player to list of player
								players.add(new Player(second));
								int score = Integer.parseInt(arr[2]);
								player.setScore(score);
								
							} else if (command.equals("delete")) { // if delete user
								// delete a disconnected player
								while (!found && count < players.size()) {
									if (players.get(count).getName().equals(second)) {
										players.remove(count);
										found = true;
									}
									count++;
								}
								
							} else if (command.equals("xy")) {
								double x = Double.parseDouble(arr[2]);
								double y = Double.parseDouble(arr[3]);

								// find updated player and
								// update their info
								while (!found && count < players.size()) {
									Player p = players.get(count);

									if (p.getName().equals(second)) {
										p.setX(x);
										p.setY(y);
										found = true;
									}
									count++;
								}
								
							} else if (command.equals("score")) {
								int newScore = Integer.parseInt(arr[2]);

								// update score of a player
								if (player.getName().equals(second)) {
									player.setScore(newScore);
								} else {
									// find appropriate player
									// and update their info
									while (!found && count < players.size()) {
										Player p = players.get(count);

										if (p.getName().equals(second)) {
											p.setScore(newScore);
											found = true;
										}
										count++;
									}
								}
							} else if (command.equals("hit")) {
								double damage = Double.parseDouble(arr[2]);
								player.getBuild().setHealth(player.getBuild().getHealth() - damage);

								// if the player is killed from this shot
								if (player.getBuild().getHealth() + damage > 0 && player.getBuild().getHealth() <= 0) {
									String killer = arr[3];
									
									int newScore = 0;
									// find killer's original score
									while (!found && count < players.size()) {
										Player p = players.get(count);

										if (p.getName().equals(killer)) {
											p.setScore(p.getScore() + KILL_CREDIT);
											newScore = p.getScore();
											found = true;
										}
										count++;
									}
									output.println("score " + killer + " " + newScore);
									output.flush();

									// decrement player score
									player.setScore(player.getScore() - DIE_CREDIT);
									player.setEliminator(killer);
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

		// run input thread
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * connect
	 * @param address, ip address of the server
	 * @param port, port of the server
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