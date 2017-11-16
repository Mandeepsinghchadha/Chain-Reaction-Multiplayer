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
	
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	int errors=0;
	private boolean accepted = false;
	boolean isServer=false;
	private List<Thread> threads = new ArrayList<Thread>();
	private List<Connection> connections = new ArrayList<Connection>();
	private ServerSocket serverSocket;
	private Scanner in;
	public boolean readyToAccept=true;
	public Network() {}
	public void init() {
		in = new Scanner(System.in);
		//System.out.println("Input IP");
		//ip = "192.168.48.138";//in.nextLine();
		if (!connect()) {
			isServer=true;
			initializeServer();
		}
		if(isServer){
				new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						acceptConnections();
					}
				}
			}).start();
		}
		new Thread(new Runnable() {
			@Override
			public void run(){
				while(true) {
					//System.out.println(accepted);
					if(accepted) recieve();
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
		if(!readyToAccept) return;
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			Connection new_connection=new Connection(socket,this,threads.size());
			connections.add(new_connection);
			Thread thread=new Thread(new_connection);
			threads.add(thread);
			thread.start();
			System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void  recieve() {
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
				if(res.charAt(0)=='u') {
					System.out.println("olol");
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					    	mainApp.undoHandle();
					    }
					});
				}
			} catch (IOException e) {
				//e.printStackTrace();
				++errors;
			}
		}
	}
	
	public void send(String s) {
		if(isServer) {
			recievefromAll(s,-1);
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
	
	public void recievefromAll(String s, int ignore) {
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
			System.out.println("olol");
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
