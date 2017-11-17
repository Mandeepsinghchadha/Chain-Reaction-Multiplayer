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

class Connection implements Runnable{
	
	Socket conn;
	public DataOutputStream dos;
	public DataInputStream dis;
	int errors=0;
	int idx;
	Network network;
	public Connection(Socket _conn, Network _network, int _idx) {
		//if(!Network.readyToAccept) return;
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
				BoardGUI.allPlayers.add(new PlayerController(siz,BoardGUI.allColours[siz-1]));
				mainApp.numPlayers=siz;
				mainApp.b.numberOfPlayers=siz;
			}
			
			Network.receivefromAll("init "+siz,_idx);
			//mainApp.b = new BoardGUI(mainApp.numRows,mainApp.numCols,siz);
			//mainApp.b.networkPlayerNumber=1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(errors<10) {
			recieve();
		}
	}
	private void recieve() {
		try {
			String res = dis.readUTF();
			System.out.println(res);
			network.receivefromAll(res,this.idx);
		} catch (IOException e) {
			//e.printStackTrace();
			++errors;
		}
	}
	
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
