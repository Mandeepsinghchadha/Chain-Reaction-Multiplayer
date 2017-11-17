package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import application.BoardGUI;
import application.PlayerController;
import application.mainApp;
import javafx.application.Platform;
import javafx.scene.Scene;
import withoutGUI.TileBoard;

public class Network {
	public String ip = "localhost";
	private int port = 4242;
	public mainApp app;
	private static Socket socket;
	private static DataOutputStream dos;
	private DataInputStream dis;
	int errors;
	public volatile static boolean accepted, readyToAccept;
	static boolean isServer=false;
	private static List<Thread> threads = new ArrayList<Thread>();
	public static List<Connection> connections = new ArrayList<Connection>();
	public volatile static ServerSocket serverSocket,serverSocketCopy;
	/**
	 * The constructor function, closes already running socket (if server), and resets clients list.
	 * @author aayush9
	 * @param _app reference to the main Application for modifying GUI elements
	 */
	public Network( mainApp _app) {
		socket=new Socket();
		app=_app;
		readyToAccept=false;
		accepted=false;
		threads.clear();
		threads = new ArrayList<Thread>();
		connections.clear();
		connections = new ArrayList<Connection>();
		errors=0;
		if(serverSocket!=null) {
			try {
				serverSocket.close();
		System.out.println("Socket closed.");
			} catch (IOException e) {}
		}
	}
	/**
	 * Attempts to connect to given IP, if it fails, initializes a server at that IP. Also makes required threads to always listen for and accept connections, and messages. 
	 * @author aayush9
	 */
	public void init() {
		if (!connect()) {
			isServer=true;
			readyToAccept=true;
			mainApp.b.networkPlayerNumber=1;
			initializeServer();
		}
		if(isServer){
				new Thread(new Runnable() {
				@Override
				public synchronized void run() {
					while(true) {
						if(readyToAccept)
							acceptConnections();
						else {
							break;
						}
					}
				}
			}).start();
		}
		new Thread(new Runnable() {
			@Override
			public synchronized void run(){
				while(true) {
					//System.out.println(accepted);
					if(accepted) receive();
					else return;
				}
			}
		}).start();	
	}
	/**
	 * Connects to server at given IP, if it fails, returns a false signal to initialize server.
	 * @author aayush9
	 */
	private boolean connect() {
		try {
			socket = new Socket(ip, port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
		} catch (IOException e) {
			System.out.println("Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
			return false;
		}
		System.out.println("Successfully connected to the server.");
		return true;
	}
	/**
	 * Initializes server by setting up serverSocket.
	 * @author aayush9
	 */
	private void initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * An always running method that is listening for incoming connection requests. Rejects connections if a game is in progress.
	 * @author aayush9
	 */
	private void acceptConnections() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			if(!readyToAccept || mainApp.numPlayers==8) {
				readyToAccept=false;
				return;
			}
			Connection new_connection=new Connection(socket,this,threads.size());
			connections.add(new_connection);
			Thread thread=new Thread(new_connection);
			threads.add(thread);
			thread.start();
		} catch (IOException e) {
			System.out.println("Stopped Accepting Conenctions");
			//e.printStackTrace();
		}
	}
	/**
	 * If current app is a client, it attempts to read a string from network to check what actions are done on other machines.
	 * @author aayush9
	 */
	private void  receive() {
		if(isServer) return;
		else if(errors<10) {
			try {
				String res = dis.readUTF();
				System.out.println(res);
				if(res.charAt(0)=='m') {
					StringTokenizer st=new StringTokenizer(res);
					st.nextToken();
					int x=Integer.parseInt(st.nextToken());
					int y=Integer.parseInt(st.nextToken());
					mainApp.b.board[x][y].pleaseSend=false;
					Platform.runLater(new Runnable() {
					    @Override
					    public synchronized void run() {
					    		mainApp.b.board[x][y].handle();
					    }
					});
					
				}
				else if(res.charAt(0)=='u') {
					Platform.runLater(new Runnable() {
					    @Override
					    public synchronized void run() {
					    	mainApp.undoHandle();
					    }
					});
				}
				else if(res.charAt(0)=='i') {
					StringTokenizer st=new StringTokenizer(res);
					st.nextToken();
					int siz=Integer.parseInt(st.nextToken());
					if(siz>mainApp.numPlayers) {
						for(int i=mainApp.numPlayers;i<siz;++i)
							BoardGUI.allPlayers.add(new PlayerController(i+1,BoardGUI.allColours[i]));
						mainApp.numPlayers=siz;
						mainApp.b.numberOfPlayers=siz;
						mainApp.b.tb = new TileBoard(mainApp.numRows,mainApp.numCols,siz);
					}
					if(mainApp.b.networkPlayerNumber==-1) mainApp.b.networkPlayerNumber=siz;
				}
			} catch (IOException e) {
				//e.printStackTrace();
				++errors;
			}
		}
	}
	/**
	 * If current app is a client, sends the action performed in form of string.
	 * @author aayush9
	 * @param s the string to be sent
	 */
	public static void send(String s) {
		if(isServer) {
			receivefromAll(s,-1);
		}
		else if(!accepted) return;
		else {
			try {
				dos.writeUTF(s);
				dos.flush();
			System.out.println("Sent "+s);
			} catch (IOException e) {
				System.out.println("Data not sent...");
			}
		}
	}
	/**
	 * If current app is server, it broadcasts the given signal string to all clients.
	 * @author aayush9
	 * @param s string to be sent
	 * @param ignore the index of machine where action was initiated, and not send signal to it again to avoid repetition
	 */
	public static void receivefromAll(String s, int ignore) {
		if(s.charAt(0)=='m') {
			StringTokenizer st=new StringTokenizer(s);
			st.nextToken();
			int x=Integer.parseInt(st.nextToken());
			int y=Integer.parseInt(st.nextToken());
			mainApp.b.board[x][y].pleaseSend=false;
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    		mainApp.b.board[x][y].handle();
			    }
			});
		}
		if(s.charAt(0)=='u') {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	mainApp.undoHandle();
			    }
			});
		}
		for(int i=0;i<threads.size();++i) if(i!=ignore){
			//DataOutputStream dos=threads.get(i).dos;
			try {
				connections.get(i).dos.writeUTF(s);
				connections.get(i).dos.flush();
			System.out.println("Sent. "+s);
			} catch (IOException e) {
				System.out.println("Data not sent...");
			}
		}
	}
	
}
