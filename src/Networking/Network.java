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

import application.mainApp;
import javafx.application.Platform;

public class Network {
	public String ip = "localhost";
	private int port = 4242;
	
	private static Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	int errors;
	public volatile static boolean accepted, readyToAccept,wasServer=false;
	boolean isServer=false;
	private static List<Thread> threads = new ArrayList<Thread>();
	public static List<Connection> connections = new ArrayList<Connection>();
	public static ServerSocket serverSocket;
	
	public Network() {
		socket=new Socket();
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
	public void init() {
		//ip = "192.168.48.138";
		if (!connect() || wasServer) {
			isServer=true;
			wasServer=true;
			readyToAccept=true;
			mainApp.b.networkPlayerNumber=1;
			initializeServer();
		}
		if(isServer){
				new Thread(new Runnable() {
				@Override
				public void run() {
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
			public void run(){
				while(true) {
					//System.out.println(accepted);
					if(accepted) receive();
					else return;
				}
			}
		}).start();
		
	}
	
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

	private void initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void acceptConnections() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			Connection new_connection=new Connection(socket,this,threads.size());
			connections.add(new_connection);
			Thread thread=new Thread(new_connection);
			threads.add(thread);
			thread.start();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
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
					    public void run() {
					    		mainApp.b.board[x][y].handle();
					    }
					});
					
					//mainApp.b.board[x][y].drawSphere(false);
					mainApp.b.board[x][y].pleaseSend=true;
				}
				else if(res.charAt(0)=='u') {
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					    	mainApp.undoHandle();
					    }
					});
				}
				else if(res.charAt(0)=='i') {
					StringTokenizer st=new StringTokenizer(res);
					st.nextToken();
					mainApp.b.networkPlayerNumber=Integer.parseInt(st.nextToken());
				}
			} catch (IOException e) {
				//e.printStackTrace();
				++errors;
			}
		}
	}
	
	public void send(String s) {
		if(isServer) {
			receivefromAll(s,-1);
		}
		else if(!accepted) return;
		else {
			try {
				dos.writeUTF(s);
				dos.flush();
			System.out.println("Sent.");
			} catch (IOException e) {
				System.out.println("Data not sent...");
			}
		}
	}
	
	public void receivefromAll(String s, int ignore) {
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
			
			//mainApp.b.board[x][y].drawSphere(false);
			mainApp.b.board[x][y].pleaseSend=true;
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
			System.out.println("Sent.");
			} catch (IOException e) {
				System.out.println("Data not sent...");
			}
		}
	}
	
}
