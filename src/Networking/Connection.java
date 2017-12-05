package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import application.BoardGUI;
import application.PlayerController;
import application.mainApp;
import javafx.application.Platform;
import withoutGUI.TileBoard;

class Connection implements Runnable{
	
	Socket conn;
	public DataOutputStream dos;
	public DataInputStream dis;
	int errors=0;
	int idx;
	Network network;
	/**
	 * The constructor function, appends the player to game if not played on, sets up the data streams and sends a signal for the new client's presence to others.
	 * Incase game is already pending, it ignores the current connection.
	 * @author aayush9
	 * @param _conn Socket from where the client attempts the connection
	 * @param _network A reference to the calling network object to send signals to others
	 * @param _idx The index number of the current client given by the server.
	 */
	public Connection(Socket _conn, Network _network, int _idx) {
		this.conn=_conn;
		this.network=_network;
		this.idx=_idx;
		try {
			dos = new DataOutputStream(conn.getOutputStream());
			dis = new DataInputStream(conn.getInputStream());
			System.out.println("CLIENT ACCEPTED...");
			int siz=_idx+2;
			send("init "+siz);
			if(siz>mainApp.numPlayers) {
				System.out.println("Adding: "+siz);
				for(int i=mainApp.numPlayers;i<siz;++i)
					BoardGUI.allPlayers.add(new PlayerController(i+1,BoardGUI.allColours[i]));
				mainApp.numPlayers=siz;
				mainApp.b.numberOfPlayers=siz;
				mainApp.b.tb = new TileBoard(mainApp.numRows,mainApp.numCols,siz);
			}
			if(mainApp.b.networkPlayerNumber==-1) mainApp.b.networkPlayerNumber=siz;
			Network.receivefromAll("init "+siz,_idx);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * An infinite loop to receive messages from client
	 * @author aayush9
	 */
	@Override
	public void run() {
		while(errors<10) {
			recieve();
		}
	}
	/**
	 * If current app is a server, it attempts to read a string from network to check what actions are done on client.
	 * @author aayush9
	 * @param s the string to be sent
	 */
	private void recieve() {
		try {
			String res = dis.readUTF();
			System.out.println(res);
			Network.receivefromAll(res,this.idx);
		} catch (IOException e) {
			//e.printStackTrace();
			++errors;
		}
	}
	/**
	 * If current app is a server, sends the action performed in form of string to the client of current thread.
	 * @author aayush9
	 * @param s the string to be sent
	 */
	public void send(String s) {
		try {
			dos.writeUTF(s);
			dos.flush();
		System.out.println("Sent "+s);
		} catch (IOException e) {
			System.out.println("Data not sent...");
		}
	}
}
